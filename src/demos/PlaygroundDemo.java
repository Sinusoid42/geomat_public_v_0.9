package demos;

import com.benwinter.geomat.api.Renderable;
import com.benwinter.geomat.api.Renderable.FLAGS;
import com.benwinter.geomat.math.Mat3;
import com.benwinter.geomat.math.Mat4;
import com.benwinter.geomat.math.Vec2;
import com.benwinter.geomat.math.Vec3;
import com.benwinter.geomat.renderables.skinned_mesh.Math.SM3D;
import com.benwinter.geomat.renderables.skinned_mesh.Math.SV3D;
import com.benwinter.geomat.renderables.skinned_mesh.Math.SkinnedVector3D;
import com.benwinter.geomat.runtime.PlaygroundTemplate;
import com.benwinter.geomat.utils.FunctionUtils;

public class PlaygroundDemo extends PlaygroundTemplate{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new PlaygroundDemo();
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
		
		
//		FunctionUtils.buildFunctionGradientVisualizer(this);
//		getScene().addSkinnedMesh(new SV3D(v1).set(FLAGS.GM_MESH_RENDERER, GM_TRUE).setDiffuseColorRGB(new Vec3(1,0,0)));
//		
//		Vec3 v2 = new Vec3(v).transform(m.inverse());
//		getScene().addSkinnedMesh(new SV3D(v2).set(FLAGS.GM_MESH_RENDERER, GM_TRUE).setDiffuseColorRGB(new Vec3(2,2,0)));
	}

}
