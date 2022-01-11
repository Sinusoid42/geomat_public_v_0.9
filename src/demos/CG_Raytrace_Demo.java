package demos;

import com.benwinter.geomat.api.Renderable;
import com.benwinter.geomat.api.Renderable.DEFAULTS;
import com.benwinter.geomat.api.Renderable.FLAGS;
import com.benwinter.geomat.gui.GUIComponent;
import com.benwinter.geomat.gui.GUIWindow;
import com.benwinter.geomat.gui.components.GUISlider;
import com.benwinter.geomat.gui.components.GUIText;
import com.benwinter.geomat.math.Mat3;
import com.benwinter.geomat.math.Vec2;
import com.benwinter.geomat.math.Vec3;
import com.benwinter.geomat.math.Vec4;
import com.benwinter.geomat.physics.raycast_utils.FaceHit;
import com.benwinter.geomat.physics.raycast_utils.RayCast;
import com.benwinter.geomat.physics.raycast_utils.RayCastUtils;
import com.benwinter.geomat.renderables.Utils.Text3D;
import com.benwinter.geomat.renderables.mesh.Mesh;
import com.benwinter.geomat.renderables.mesh.Primitive.Plane;
import com.benwinter.geomat.renderables.mesh.Primitive.Sphere;
import com.benwinter.geomat.renderables.mesh.Primitive.Tube;
import com.benwinter.geomat.renderables.skinned_mesh.Math.SkinnedVector3D;
import com.benwinter.geomat.runtime.Camera;
import com.benwinter.geomat.runtime.CameraController;
import com.benwinter.geomat.runtime.PlaygroundTemplate;
import com.benwinter.geomat.runtime.Scene;
import com.benwinter.geomat.utils.KeyList;
import com.benwinter.geomat.utils.MeshUtils;
import com.benwinter.geomat.utils.fileutils.OBJUtils;
import com.benwinter.geomat.window.GLFWKeyCallback;
import com.benwinter.geomat.window.GLFWListenerUtils;
import com.benwinter.geomat.window.GLFWMouseCallback;

public class CG_Raytrace_Demo extends PlaygroundTemplate{

	public static void main(String[] args) {
		new CG_Raytrace_Demo();
	}

	public void setup() {
		//INIT
		setScene(Scene.getDefault(new Camera(getListenerUtils(), CameraController.getOrbitController()).setVisible(true)));
		getScene().addDefaultsMeshContainer(MeshUtils.getUserGrid().set(FLAGS.GM_MESH_RENDERER, GM_TRUE).setDiffuseColor(new Vec3(0.2f,0.2f,0.2f)));

		//MAX
		int RayCast_Count = 10;
		int RaysReflection = 42;
		int[] RayCount_Curr = {2};

		//ENABLE UI
		getScene().setMeshOnclickMoveable(true);
		getScene().setMeshOnclickRotateable(true);

		//DESIGN & IA
		getScene().setBackgroundColor(new Vec4(0.05f,0.05f,0.05f,1));
		SkinnedVector3D[] Vectors3D = new SkinnedVector3D[RayCast_Count];
		SkinnedVector3D[] Normals3D = new SkinnedVector3D[RayCast_Count];
		SkinnedVector3D[] ReflectionRays = new SkinnedVector3D[RayCast_Count*RaysReflection];

		//Mesh Init
		Text3D[] texts = new Text3D[RayCast_Count];
		for (int i=0;i<RayCast_Count;i++) {
			texts[i] = new Text3D();
			texts[i].getTransform().setLocalScale(0.05f,0.1f,1f);
			texts[i].setVisible(true);
			texts[i].set(DEFAULTS.GM_ORIENTATION_MODE, GM_ORIENTATION_MAIN_CAMERA);
			getScene().addText3D(texts[i]);

			SkinnedVector3D vector2 = new SkinnedVector3D();
			vector2.setDiffuseColorRGB(new Vec3(2,0,2));
			vector2.set(		FLAGS.GM_MESH_RENDERER, GM_TRUE);
			getScene().addSkinnedMesh(vector2);
			vector2.setClickable(false);
			Vectors3D[i] = vector2;
			Vectors3D[i].set(FLAGS.GM_COLOR_MODE, Renderable.GM_COLOR_DIFFUSE);

			SkinnedVector3D vector3 = new SkinnedVector3D();
			vector3.setDiffuseColorRGB(new Vec3(2,2,2));
			vector3.set(		FLAGS.GM_MESH_RENDERER, GM_TRUE);
			getScene().addSkinnedMesh(vector3);
			vector3.setClickable(false);
			Normals3D[i] = vector3;
			Normals3D[i].set(FLAGS.GM_COLOR_MODE, Renderable.GM_COLOR_DIFFUSE);
		}

		for (int i=0;i<RayCast_Count*RaysReflection;i++) {
			SkinnedVector3D vector4 = new SkinnedVector3D();
			vector4.setDiffuseColorRGB(new Vec3(2,2,0));
			vector4.set(FLAGS.GM_MESH_RENDERER, GM_TRUE);
			getScene().addSkinnedMesh(vector4);
			vector4.setClickable(false);
			ReflectionRays[i] = vector4;
			ReflectionRays[i].set(FLAGS.GM_COLOR_MODE, Renderable.GM_COLOR_DIFFUSE);
		}

		//Build a random Scene
		Mesh plane1 = new Plane(25);
		plane1.set(FLAGS.GM_LINE_RENDERER, GM_TRUE).set(FLAGS.GM_LIGHT_MODE, Renderable.GM_LIGHT_DIFFUSE);
		getScene().addMesh(plane1);
		plane1.set(FLAGS.GM_COLOR_MODE, GM_COLOR_DIFFUSE);
		plane1.getTransform().setLocalScale(15, 15, 15);
		plane1.getTransform().setLocalRotationMatrix(3.14159265f/2f, 0, 0);
		plane1.getTransform().setLocalPosition(0, 0, -15.5f);

		Mesh plane2 = new Plane(25);
		plane2.set(FLAGS.GM_LINE_RENDERER, GM_TRUE).set(FLAGS.GM_LIGHT_MODE, Renderable.GM_LIGHT_DIFFUSE);
		getScene().addMesh(plane2);
		plane2.set(FLAGS.GM_COLOR_MODE, GM_COLOR_DIFFUSE);
		plane2.getTransform().setLocalScale(15, 15, 15);
		plane2.getTransform().setLocalRotationMatrix(3.14159265f/2f, 0, 0);
		plane2.getTransform().setLocalPosition(0, 0, 15.5f);

		Mesh plane3 = new Plane(25);
		plane3.set(FLAGS.GM_LINE_RENDERER, GM_TRUE).set(FLAGS.GM_LIGHT_MODE, Renderable.GM_LIGHT_DIFFUSE);
		getScene().addMesh(plane3);
		plane3.set(FLAGS.GM_COLOR_MODE, GM_COLOR_DIFFUSE);
		plane3.setDiffuseColorRGB(new Vec3(1,1,1));
		plane3.getTransform().setLocalScale(15, 15, 15);
		plane3.getTransform().setLocalRotationMatrix(0, 0,3.14159265f/2f);
		plane3.getTransform().setLocalPosition(15.5f, 0, 0);


		Mesh plane4 = new Plane(25);
		plane4.set(FLAGS.GM_LINE_RENDERER, GM_TRUE).set(FLAGS.GM_LIGHT_MODE, Renderable.GM_LIGHT_DIFFUSE);
		getScene().addMesh(plane4);
		plane4.set(FLAGS.GM_COLOR_MODE, GM_COLOR_DIFFUSE);
		plane4.getTransform().setLocalScale(15, 15, 15);
		plane4.getTransform().setLocalRotationMatrix(0, 0,3.14159265f/2f);
		plane4.getTransform().setLocalPosition(-15.5f, 0, 0);

		Mesh plane5 = new Plane(25);
		plane5.set(FLAGS.GM_LINE_RENDERER, GM_TRUE).set(FLAGS.GM_LIGHT_MODE, Renderable.GM_LIGHT_DIFFUSE);
		getScene().addMesh(plane5);
		plane5.set(FLAGS.GM_COLOR_MODE, GM_COLOR_DIFFUSE);
		plane5.getTransform().setLocalScale(15, 15, 15);
		plane5.getTransform().setLocalRotationMatrix(0, 0,0);
		plane5.getTransform().setLocalPosition(0, -15.5f, 0);

		Mesh plane6 = new Plane(25);
		plane6.set(FLAGS.GM_LINE_RENDERER, GM_TRUE).set(FLAGS.GM_LIGHT_MODE, Renderable.GM_LIGHT_DIFFUSE);
		getScene().addMesh(plane6);
		plane6.set(FLAGS.GM_COLOR_MODE, GM_COLOR_DIFFUSE);
		plane6.getTransform().setLocalScale(15, 15, 15);
		plane6.getTransform().setLocalRotationMatrix(0, 0,0);
		plane6.getTransform().setLocalPosition(0, 15.5f, 0);

		//getScene().addMesh(OBJUtils.getOBJFile(Renderable.OBJECT_FILE_DIR + "teapot.obj").set(FLAGS.GM_MESH_RENDERER, GM_TRUE));
		Mesh sphere = new Sphere().set(FLAGS.GM_LINE_RENDERER, GM_TRUE);
		getScene().addMesh(sphere);
		sphere.getTransform().setLocalScale(3);
		sphere.setDiffuseColorRGB(new Vec3(2,2,2));


		Tube[] T = new Tube[8];
		Vec3[] p = {new Vec3(-10,0,-10),new Vec3(-10,0,10),new Vec3(10,0,10),new Vec3(10,0,-10),
				new Vec3(-3,0,-3),new Vec3(-3,0,3),new Vec3(3,0,3),new Vec3(3,0,-3)};

		for (int i=0;i<T.length;i++) {
			T[i] = new Tube();
			T[i].getTransform().setLocalRotationMatrix(0, 0, 3.1415927f/2f);
			T[i].getTransform().setLocalScale(10, 1, 1);
			T[i].set(FLAGS.GM_MESH_RENDERER, GM_TRUE);
			T[i].setDiffuseColorRGB(new Vec3(2,0,0));
			T[i].getTransform().setLocalPosition(p[i]);
			getScene().addMesh(T[i]);
		}


		//CB on CMD Click + LMB
		getListenerUtils().addMouseCallback(
				new GLFWMouseCallback(GLFWListenerUtils.MOUSEBUTTON_LEFT) {
					public void invoke(int x, int y, int mod) {
						if (mod == GLFWListenerUtils.KEYSTROKE_MOD_COMMAND) {

							//First Raycast from the Camera

							RayCast raycast = RayCastUtils.getScreenToRayCast(
									x, 
									y, 
									getWindow().getWidth(), 
									getWindow().getHeight(), 
									getScene().getActiveCamera().getProjectionMatrix(), 
									getScene().getActiveCamera().getViewMatrix());

							//Any Mesh was hit ?

							Mesh mesh = getScene().checkForRayCast(raycast);
							if (mesh != null) {
								FaceHit hit = mesh.checkForHit(raycast);
								RayCastUtils.setSkinnedMeshVectorFromRayCast(
										raycast,
										Vectors3D[0],
										hit.getDistance());	

								if (hit!=null) {	

									//Edit the first rayhit
									RayCast raycast_rec = new RayCast(hit);
									RayCastUtils.setSkinnedMeshVectorFromRayCast(
											raycast_rec,
											Vectors3D[1]);
									texts[0].getTransform().setLocalPosition(hit.getHitPosition());
									texts[0].setText(hit.toStringLayered());

									Normals3D[0].getRoot().setLocalPosition(hit.getHitPosition());
									Normals3D[0].set(hit.getHitNormal());

									Vec2 normal_orientation_in_sphere_coords = new Vec3(raycast_rec.getDirection()).normalize().getRotation();
									Mat3 RotationMatrix = 
											Mat3.rotationalY(-normal_orientation_in_sphere_coords.x).mul(
													Mat3.rotationalZ(normal_orientation_in_sphere_coords.y-3.1415927f/2f));

									//Edit the 3D Vectors to match the reflection geometry
									for (int i=0;i<6;i++) {
										for (int j=0;j<7;j++) {
											float theta = (i+3)/(9f)*3.141592f/2f;
											float phi = (j+1)/(7f)*2*3.141592f;
											Vec3 k = new Vec3((float)(Math.cos(theta)*Math.cos(phi)),(float)(Math.sin(theta)),(float)(Math.cos(theta)*Math.sin(phi)));
											k.transform(RotationMatrix);

											Vec3 r = new Vec3(k).projectOnto(new Vec3(raycast_rec.getDirection()));

											ReflectionRays[i*7+j+0*RaysReflection].set(k.mul(r.length()*2*new Vec3(raycast_rec.getDirection()).dot(k)));
											ReflectionRays[i*7+j+0*RaysReflection].getRoot().setLocalPosition(hit.getHitPosition());
										}
									}

									Mesh mesh_rec = null;
									mesh_rec = getScene().checkForRayCast(mesh_rec, raycast_rec);

									boolean exit=false;
									for (int i=0;i<RayCast_Count-1;i++) {
										if (exit || RayCount_Curr[0]<i) {
											raycast_rec = new RayCast();
											RayCastUtils.setSkinnedMeshVectorFromRayCast(
													raycast_rec,
													Vectors3D[i+1]);
											texts[i+1].getTransform().setLocalPosition(0,0,0);
											texts[i+1].setText("");
											Normals3D[i+1].getRoot().setLocalPosition(0,0,0);
											Normals3D[i+1].set(new Vec3(0,0,0));
											continue;
										}
										if (mesh_rec != null) {
											hit = mesh_rec.checkForHit(raycast_rec);

											//Edit the first rayhit
											if (hit!=null) {
												RayCastUtils.setSkinnedMeshVectorFromRayCast(
														raycast_rec,
														Vectors3D[i+1],
														hit.getDistance());	
												raycast_rec = new RayCast(hit);
												mesh_rec = getScene().checkForRayCast(raycast_rec);
												texts[i+1].getTransform().setLocalPosition(hit.getHitPosition());
												texts[i+1].setText(hit.toStringLayered());
												Normals3D[i+1].getRoot().setLocalPosition(hit.getHitPosition());
												Normals3D[i+1].set(hit.getHitNormal());

												normal_orientation_in_sphere_coords = new Vec3(raycast_rec.getDirection()).normalize().getRotation();
												RotationMatrix = 
														Mat3.rotationalY(-normal_orientation_in_sphere_coords.x).mul(
																Mat3.rotationalZ(normal_orientation_in_sphere_coords.y-3.1415927f/2f));

												//Edit the 3D Vectors to match the reflection geometry
												for (int l=0;l<6;l++) {
													for (int j=0;j<7;j++) {
														float theta = (l+3)/(9f)*3.141592f/2f;
														float phi = (j+1)/(7f)*2*3.141592f;
														Vec3 k = new Vec3((float)(Math.cos(theta)*Math.cos(phi)),(float)(Math.sin(theta)),(float)(Math.cos(theta)*Math.sin(phi)));
														k.transform(RotationMatrix);

														Vec3 r = new Vec3(k).projectOnto(new Vec3(raycast_rec.getDirection()));

														ReflectionRays[l*7+j+(i+1)*RaysReflection].set(k.mul(r.length()*2*new Vec3(raycast_rec.getDirection()).dot(k)));
														ReflectionRays[l*7+j+(i+1)*RaysReflection].getRoot().setLocalPosition(hit.getHitPosition());
													}
												}
											}
											else {
												//Default, just make a simlpe raycast
												RayCastUtils.setSkinnedMeshVectorFromRayCast(raycast_rec,Vectors3D[i+1]);
												exit = true;
											}
										}
										else{
											//Default, just make a simlpe raycast
											RayCastUtils.setSkinnedMeshVectorFromRayCast(raycast_rec,Vectors3D[i+1]);
											exit = true;
										}
									}
								}
								else {
									//Default, just make a simlpe raycast
									RayCastUtils.setSkinnedMeshVectorFromRayCast(raycast,Vectors3D[0]);
								}
							}
							else{
								for (int i=1;i<RayCast_Count-1;i++) {
									RayCast raycast_rec = new RayCast();
									//Default, just make a simlpe raycast
									RayCastUtils.setSkinnedMeshVectorFromRayCast(
											raycast_rec,
											Vectors3D[i+1]);
								}
								RayCastUtils.setSkinnedMeshVectorFromRayCast(raycast,Vectors3D[0]);
							}
						}
					}
				});

		getListenerUtils().addKeyCallback(new GLFWKeyCallback(GLFWListenerUtils.KEYSTROKE_X) {
			@Override
			public void invoke() {
				for (int i=0;i<RayCast_Count;i++) {
					RayCast raycast_rec = new RayCast();
					RayCastUtils.setSkinnedMeshVectorFromRayCast(
							raycast_rec,
							Vectors3D[i]);
				}
			}
		});

		GUIWindow controlsWindow = new GUIWindow(15,15,220,150);
		controlsWindow.addComponent(new GUIText(5,5,15,"Controls"));

		controlsWindow.addComponent(new GUIText(5,20,15,"Ray-Count"));
		GUIText t = new GUIText(200,20,15,25,"2");
		t.setParentBound(false);
		controlsWindow.addComponent(new GUISlider(90,20,100,15) {
			public void invokeSlider(float delta) {
				RayCount_Curr[0] = (int)(delta*RayCast_Count);
				t.setText("" +RayCount_Curr[0]);
			}
		});
		controlsWindow.addComponent(t);

		getGUI().addGUIWindow(controlsWindow);

		GUIWindow info = new GUIWindow(5,5,300,120);
		info.set(GUIComponent.FLAGS.OFFSET_FIXED_CENTER_X,GM_TRUE, 0);
		getGUI().addGUIWindow(info);
		info.addComponent(new GUIText(5,5,15,
				"ComputerGrafik Raytrace Demo\n"
						+ "Diese Demo stellt eine OpenGL Scene zur\n"
						+ "Verfügung, um mittels spielerischer\n"
						+ "Interaktion Raytracing\n"
						+ "zu demonstrieren!"));
		GUIText vectoren_toggle_button = new GUIText(5,80,15,"Vector_Opaque") {
			int i=0;
			public void onClick(KeyList mouseButtons, float x, float y) {
				// TODO Auto-generated method stub
				i++;
				i%=4;
				for (int k=0;k<RayCast_Count;k++) {
					switch(i) {
					case 0:{
						Vectors3D[k].set(Renderable.FLAGS.GM_LINE_RENDERER, GM_TRUE);
						Vectors3D[k].set(Renderable.FLAGS.GM_MESH_RENDERER, GM_FALSE);
						continue;
					}
					case 1:{
						Vectors3D[k].set(Renderable.FLAGS.GM_LINE_RENDERER, GM_TRUE);
						Vectors3D[k].set(Renderable.FLAGS.GM_MESH_RENDERER, GM_TRUE);
						continue;
					}
					case 2:{
						Vectors3D[k].set(Renderable.FLAGS.GM_LINE_RENDERER, GM_FALSE);
						Vectors3D[k].set(Renderable.FLAGS.GM_MESH_RENDERER, GM_TRUE);
						continue;
					}
					case 3:{
						Vectors3D[0].set(Renderable.FLAGS.GM_LINE_RENDERER, GM_FALSE);
						Vectors3D[0].set(Renderable.FLAGS.GM_MESH_RENDERER, GM_FALSE);
						continue;
					}
					}
				}
			}
		};

		vectoren_toggle_button.setBackground(true);
		vectoren_toggle_button.set(GUIComponent.FLAGS.GUI_CLICKABLE, GM_TRUE);
		info.addComponent(vectoren_toggle_button);

		getScene().addCamera(new Camera(getListenerUtils(), CameraController.getWasdController()).setWorldPosition(new Vec4(3,3,3,1)));
		getScene().setCameraToggle(getListenerUtils(), GLFWListenerUtils.KEYSTROKE_TAB);

		getListenerUtils().addKeyCallback(new GLFWKeyCallback(GLFWListenerUtils.KEYSTROKE_Y) {
			@Override
			public void invoke() {
				// TODO Auto-generated method stub
				getScene().getActiveCamera().getCameraController().toggleViewPortProjection();
			}
		});
	}
}
