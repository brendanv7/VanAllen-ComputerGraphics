import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.imageio.ImageIO;
import java.awt.*;

/**
 * A rudimentary implementation of the Ray-Tracing algorithm
 * in Chapter 4 of Fundamentals of Computer Graphics.
 *
 * Output: PNG or JPEG image with resolution = 1280x720 pixels
 */
public class RayTracer {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int L = -1; //
    public static final int R = 1;  // View plane
    public static final int B = -1; //   bounds
    public static final int T = 1;  //
    public static final int D = 1; // Distance from viewpoint to
    public static final int U = 1;
    public static final int V = 1;
    public static final int W = -1;
    public static final Vector3d VIEWPOINT = new Vector3d(0,0,0);
    Matrix4d test = new Matrix4d();

    public static void main(String args[]) {
        // Create the scene
        Scene scene = createScene();
        Group surfaces = scene.getSurfaces();

        int[] pixels = new int[WIDTH*HEIGHT];
        int pixel = 0;

        // Iterate through each pixel left to right, then top to bottom
        for(int i=0;i<HEIGHT;i++) {
            for(int j=0;j<WIDTH;j++) {
                Ray viewRay = computeViewingRay(i,j);
                // if ray hits an object with t in [0,inf) then
                if(surfaces.hit(viewRay) != null) {
                    // evaluate shading model and set pixel to the resulting color
                } else {
                    // set pixel to the background color
                    pixels[pixel] = scene.backgroundColor.getRGB();
                }
                pixel++;
            }
        }
    }

    private static Ray computeViewingRay(int i, int j) {
        double u = L + (R - L)*(i + 0.5)/WIDTH;
        double v = B + (T - B)*(j + 0.5)/HEIGHT;
        Vector3d direction = new Vector3d(u*U,v*V,-D*W);
        return new Ray(VIEWPOINT,direction);
    }

    private static Scene createScene() {
        Scene scene = new Scene();
        scene.add(new Sphere(new Vector3d(0,0,2),1,Color.RED));
        scene.add(new Sphere(new Vector3d(0,1,2),1,Color.BLUE));



        return scene;
    }

}
