<h1>GeoMat</h1> <h5>3D Maths, Animation and Physics - Game Engine</h5>

<i>geomat_v_0.9</i>
The geomat framework is a java based 3D rendering framework to create 3D animations, physically based animations, matrix and analytic functions representations in 3D, simulate raytracing, display colorspaces, apply blending and skinning to different default implemented meshes, display differential equations with pointclouds, diffent cameras, ui elements, sounds, lights, image and texture processing, 3D text and 2D text

This framework comes with an already imported openGL mac and windows library such that the code, once imported into an eclipse project can instantly run within a default demo application

BEWARE:
  THIS FRAMEWORK IS IN A DEVELOPMENT STATE, NOT ALL FUNCTIONS ARE IMPLEMENTED FULLY, SOME LIBRARIES ARE LACKING FULL IMPLEMENTATIONS, SOME MESHES, RENDERABLES OR           
  FUNCTIONS DONT WORK PROPERLY OR AT ALL, SOME CLASSES ARE PROGRAMMED TO BE PLACEHOLDERS FOR FUTURE IDEAS

Comes with:
  
    A MathLibrary 
        Implements a Linear Algebra Library 
             (ParentClass Mat) Implements not all, but many matrix/vector operations
        Interpolations using a <i>InterpolationCurve</i> for easeIn easeOut interpolations
        Quaternions (in dev)
        Functions (can be rendered in 3D)(Using a custom written interpreter for mathematical notations <b>eg</b><i>sin(x)*sin(y)</i>
          They do NOT support exponentiation => <i>x^3</i>, use multiplcation for that => <i>x^3</i> <b>=</b> <i>x*x*x</i>
          => Functions can be abstracted to <i>periodic functions</i> and <i>fourierseries</i>
            => Signaltheory
            
    A Mesh Library
    
      => Many eucledian primitives
        => Solids like <i>Cube, Sphere, Plane, Torus, HumbleSphere, MobiusStrip, Cylinder, Tube, Circle, Capsule etc</i>
        => MathObjects like <i>Matrix3D, Vector3D</i>
      => SkinnedMesh primitives
        => Multiple primitive meshes with different geometry nodes that can manipulate the meshes on gpu side while rendering
        
    A UI Library
    
      => Windows can be created using the GUIWindow(...) class
        => GUIWindows can contain sliders, buttons, textfields, textinputfields, 2D Sliders, Icons 
        => All UI elements come with callback functions (see examples) such that the callback can be used to perform custom actions based on user input
        
    A physics and animtion library
      => transforms, meshes and skinnedmeshes can be animated using matrix animations, keyframeanimations or physically based animations using <i>PhysicsController</i> or <i>AnimationController</i> (See the examples)
      
    Lightsources can be created using the <i>LightSource</i> class
      Lights come with different setters and getters to be manipulated into global, spot and point lights (area lights are not implemented (yet))
    
    SHADERS:
      There are some default shaders implemented to create 3D and 2D UI elements, render meshes with diffuse and specular blinn-phong shading etc.
      Custom shaders can be implemented by extending the drawables and layer2D classes, they come with abstract methods to extend corresponding functionalities and work by simply adding them to a scene instance in the runtime
      


SOURCES:

  The sources are code i am working on my own at the moment, i am planning to make the sources open to the public, though as of right now this is a study project i am making available to be tested from outside using the eclipse project and jars containing the native libraries and java implementations
  The sources are approx. 70k lines of code atm
  
  
  <h2>To Run and use this framework</h2>
  Run <i>git clone https://github.com/Sinusoid42/geomat_public_v_0.9</i> in your local shell
  Import the created folder as an Eclipse Project from your local file system and create create a TestClient.java file in your source folder
  
  To create a running application by extending the abstract <i>PlaygroundTemplate</i> class with its single <i>public void setup(){}</i> method
  To Run your application create a new instance of your class eg.
    <i> public class myFrameworkClass extends PlaygroundTemplate{
        
        public static void main(String[] args){
          new myFrameworkClass();
        }
  
        public void setup(){.....}
        }
  

  </i><b>MAC OS X</b>
    To run your application on mac os x, you will need to run your application with some jvm commands to properly run openGL 
    
      Add:
        -XstartOnFirstThread
      and
        -Djava.awt.headless=true
      
      To your eclipse runtime configuration using 
        Project => Run As => Run Configurations => VM Arguments

      
  <b>Windows</b>
    On Windows, to properly control camera movement, the openGL interface and glfw callbacks give different scaling values such that scrolling is calculated          differently
    To compensate for that, use the <i>getListenerUtils().setWinFix(enabled);</i> method available through the GLFWListenerUtils class implementation
