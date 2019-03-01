import javax.vecmath.Matrix4d;
import javax.imageio.ImageIO;

/**
 * A rudimentary implementation of the Ray-Tracing algorithm
 * in Chapter 4 of Fundamentals of Computer Graphics.
 *
 * Output: PNG or JPEG image with resolution = 1280x720 pixels
 */
public class RayTracer {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    Matrix4d test = new Matrix4d();

    public static void main(String args[]) {

        for(int i=0;i<HEIGHT;i++) {
            for(int j=0;j<WIDTH;j++) {
                // 1. Compute viewing ray (p. 73 & 76, formulas 4.1)
                // if ray hits an object with t in [0,inf) then
                    // compute normal vector n
                    // evaluate shading model and set pixel to the resulting color
                // else
                    // set pixel to the background color
            }
        }
    }
}
