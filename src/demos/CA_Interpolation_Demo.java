package demos;

import com.benwinter.geomat.animation.AnimationClip;
import com.benwinter.geomat.animation.AnimationState;
import com.benwinter.geomat.animation.KeyFrame;
import com.benwinter.geomat.api.Renderable;
import com.benwinter.geomat.api.Renderable.FLAGS;
import com.benwinter.geomat.components.AnimationController;
import com.benwinter.geomat.gui.GUIComponent;
import com.benwinter.geomat.gui.GUIWindow;
import com.benwinter.geomat.gui.components.GUISlider;
import com.benwinter.geomat.gui.components.GUIText;
import com.benwinter.geomat.math.InterpolationCurve;
import com.benwinter.geomat.math.Mat4;
import com.benwinter.geomat.math.Vec3;
import com.benwinter.geomat.math.Vec4;
import com.benwinter.geomat.renderables.skinned_mesh.Math.SkinnedMatrix3D;
import com.benwinter.geomat.runtime.Camera;
import com.benwinter.geomat.runtime.CameraController;
import com.benwinter.geomat.runtime.PlaygroundTemplate;
import com.benwinter.geomat.runtime.Scene;
import com.benwinter.geomat.utils.MeshUtils;
import com.benwinter.geomat.utils.Time;

public class CA_Interpolation_Demo extends PlaygroundTemplate{

	public static void main(String[] args) {
		new CA_Interpolation_Demo();
	}

	public void setup() {
        
		setScene(Scene.getDefault(new Camera(getListenerUtils(), CameraController.getOrbitController()).setVisible(true)));
		getScene().addDefaultsMeshContainer(MeshUtils.getUserGrid().set(FLAGS.GM_MESH_RENDERER, GM_TRUE).setDiffuseColor(new Vec3(0.4f,0.4f,0.4f)));
		getScene().setBackgroundColor(new Vec4(0.05f,0.05f,0.05f,1));

		
		
		final SkinnedMatrix3D mati_3D = new SkinnedMatrix3D();
		SkinnedMatrix3D mat0_3D;
		SkinnedMatrix3D mat1_3D;

		Mat4 mat0 = new Mat4(
				1,0,0,-3,
				0,1,0,0,
				0,0,1,0,
				0,0,0,1);
		Mat4 mat1 = new Mat4(
				1,0,0,3,
				0,1,0,0,
				0,0,1,0,
				0,0,0,1);
		Mat4 mati = new Mat4(mat1).interpolate(mat0, 0);
		
		float[] delta = {0.0f};

		getScene().setMeshOnclickMoveable(true);

		

		
		GUIWindow window = new GUIWindow(0,0,200,180);
		window.set(GUIComponent.FLAGS.OFFSET_FIXED_CENTER_X,Renderable.GM_TRUE);
		window.set(GUIComponent.FLAGS.OFFSET_FIXED_TOP,Renderable.GM_TRUE,50);
		getGUI().addGUIWindow(window);
		final GUIText t = new GUIText(5,25,12,mati.toString());
		window.addComponent(t);

		GUIWindow window0 = new GUIWindow(0,0,200,140);
		window0.set(GUIComponent.FLAGS.OFFSET_FIXED_LEFT,Renderable.GM_TRUE,150);
		window0.set(GUIComponent.FLAGS.OFFSET_FIXED_TOP,Renderable.GM_TRUE,50);
		getGUI().addGUIWindow(window0);
		final GUIText t0 = new GUIText(5,5,12,mat0.toString());		
		window0.addComponent(t0);
		
		GUIWindow window1 = new GUIWindow(0,0,200,140);
		window1.set(GUIComponent.FLAGS.OFFSET_FIXED_RIGHT,Renderable.GM_TRUE,150);
		window1.set(GUIComponent.FLAGS.OFFSET_FIXED_TOP,Renderable.GM_TRUE,50);
		getGUI().addGUIWindow(window1);
		final GUIText t1 = new GUIText(5,5,12,mat1.toString());
		window1.addComponent(t1);

		
		InterpolationCurve curve = new InterpolationCurve(0,0,1,1,0,0);
		
		window.addComponent(new GUISlider(5,5,180,10) {
			public void invokeSlider(float d) {
				delta[0] = curve.get(d);
				mati.set(mat1).interpolate(mat0, delta[0]);
				mati_3D.set(mati);
			}
		});
		
		mat0_3D = new SkinnedMatrix3D() {
			public void onTransform() {
				mat0.set(this.getMat4());
				mati.set(mat1).interpolate(mat0, delta[0]);
				t0.setText(mat0.toString());
				t1.setText(mat1.toString());
			}
		};
		mat0_3D.set(mat0);
		mat1_3D = new SkinnedMatrix3D() {
			public void onTransform() {
				mat1.set(this.getMat4());
				mati.set(mat1).interpolate(mat0, delta[0]);
				t0.setText(mat0.toString());
				t1.setText(mat1.toString());
			}
		};
		
		mat1_3D.set(mat1);

		mat0_3D.set(FLAGS.GM_COLOR_MODE, GM_COLOR_VERTEX);
		mat1_3D.set(FLAGS.GM_COLOR_MODE, GM_COLOR_VERTEX);

		
		mati_3D.set(mati);
		mati_3D.set(FLAGS.GM_COLOR_MODE, GM_COLOR_VERTEX);


		getScene().addSkinnedMesh(mat0_3D.set(FLAGS.GM_MESH_RENDERER, GM_TRUE));
		getScene().addSkinnedMesh(mat1_3D.set(FLAGS.GM_MESH_RENDERER, GM_TRUE));
		getScene().addSkinnedMesh(mati_3D.set(FLAGS.GM_MESH_RENDERER, GM_TRUE));
	
		
		AnimationController controller = new AnimationController();
		mati_3D.setComponent(GM_ANIMATIONCONTROLLER, controller, 0);
		AnimationState state = new AnimationState();
		controller.addAnimationState(state);
		state.setLooped(true);
		
		state.addAnimationClip(new AnimationClip(new KeyFrame(0,0), new KeyFrame(5,0.5f)) {
			public void value(float time) {
				delta[0] = curve.get(time);
				mati.set(mat1).interpolate(mat0, delta[0]);
				mati_3D.set(mati);
				t0.setText(mat0.toString());
				t1.setText(mat1.toString());
			}
		});	
	}
}
