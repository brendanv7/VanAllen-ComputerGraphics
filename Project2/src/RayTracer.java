import javax.vecmath.Vector3d;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A rudimentary implementation of the Ray-Tracing algorithm
 * in Chapter 4 of Fundamentals of Computer Graphics.
 *
 * Outputs a PNG image with resolution = 1280x720 pixels
 *
 * @author Brendan Van Allen
 */
public class RayTracer {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int L = -1; //
    public static final int R = 1;  // View plane
    public static final int B = -1; //   bounds
    public static final int T = 1;  //
    public static final int D = 1; // Distance from viewpoint to
//    public static final Vector3d U = new Vector3d(1,0,0);
//    public static final Vector3d V = new Vector3d(0,1,0);   I know these 3 should be vectors, but the results I was getting
//    public static final Vector3d W = new Vector3d(0,0,-1);  with these as vectors were very weird.
    public static final int U = 1;
    public static final int V = 1;
    public static final int W = -1;
    public static final Vector3d VIEWPOINT = new Vector3d(0,0,0);

    public static void main(String args[]) {
        // Create the scene
        Scene scene = createScene();
        Group surfaces = scene.getSurfaces();

        // The image we will manipulate
        BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

        // Iterate through each pixel left to right, then top to bottom
        for(int i=0;i<HEIGHT;i++) {
            for(int j=0;j<WIDTH;j++) {
                Ray viewRay = computeViewingRay(i,j);
                HitRecord hit = surfaces.hit(viewRay);
                // Determine if there was a hit
                if(hit != null) {
                    // evaluate shading model and set pixel to the resulting color
                    img.setRGB(j,i, computeShading(scene, hit));
                } else {
                    // set pixel to the background color
                    img.setRGB(j,i,scene.BACKGROUND_COLOR.getRGB());
                }
            }
        }

        // All pixels are set, now we just need to write the image to a file
        try {
            File file = new File("Scene.png");
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    /**
     * Helper method to compute the viewing ray for each pixel
     * @param i current pixel y value
     * @param j current pixel x value
     * @return
     */
    private static Ray computeViewingRay(int i, int j) {
        double u = L + (R - L)*(j + 0.5)/WIDTH;
        double v = B + (T - B)*(i + 0.5)/HEIGHT;
        Vector3d direction = new Vector3d(u*U,v*V,-D*W);

        // Attempt at making U V W vectors, very weird results
//        // -d * W
//        Vector3d d1 = new Vector3d(W);
//        d1.scale(-D);
//
//        // u * U
//        Vector3d d2 = new Vector3d(U);
//        d1.scale(u);
//
//        // v * V
//        Vector3d d3 = new Vector3d(V);
//        d1.scale(v);
//
//        // Add them all up for the final direction
//        Vector3d direction = new Vector3d(d1);
//        direction.add(d2);
//        direction.add(d3);
        return new Ray(VIEWPOINT,direction);
    }

    /*
     * Hard-coded scene
     */
    private static Scene createScene() {
        Light lightSource = new Light(new Vector3d(-5,-3,3), Color.WHITE);
        Scene scene = new Scene(lightSource);

        scene.add(new Sphere(new Vector3d(0,0,2),.5, new Material(100,Color.YELLOW)));
        scene.add(new Sphere(new Vector3d(-1,1,3),.75, new Material(1000,Color.MAGENTA)));
        scene.add(new Sphere(new Vector3d(-2,2,4), .5, new Material(10000, Color.CYAN)));

        return scene;
    }

    /*
     * Computes the RGB color value for the current pixel
     */
    private static int computeShading(Scene scene, HitRecord hit) {
        //L = ka Ia + kd I max(0, n · l) + ks I max(0, n · h)^n

        // First, we need to compute light vector l
        // Copy the light position vector so we don't change its state
        Vector3d l = new Vector3d(scene.getLightSource().getPosition());

        // To get the actual light vector, we need the difference of the intersection point and light position
        l.sub(hit.getPoint());

        // Must be a unit vector
        l.normalize();

        // Next, compute the view vector v in the same fashion
        // This vector is named h because it will become the halfway vector in the next step
        Vector3d h = new Vector3d(VIEWPOINT);
        h.sub(hit.getPoint());
        h.normalize();

        // Now we can compute the halfway vector h
        h.add(l);
        h.normalize();

        Vector3d normal = hit.getNormal(); // No need to make a copy since we are only computing dot products

        int[] diffuseCo = new int[3]; // This will hold the diffuse coefficients for R, G, and B
        diffuseCo[0] = ((Sphere) hit.getSurface()).getMaterial().getColor().getRed();
        diffuseCo[1] = ((Sphere) hit.getSurface()).getMaterial().getColor().getGreen();
        diffuseCo[2] = ((Sphere) hit.getSurface()).getMaterial().getColor().getBlue();

        // From the textbook, the specular color will be gray
        int[] specularCo = new int[3]; // This will hold the specular coefficients for R, G, B
        specularCo[0] = Color.GRAY.getRed();
        specularCo[1] = Color.GRAY.getGreen();
        specularCo[2] = Color.GRAY.getBlue();

        // From the textbook, surface ambient color can be set to the diffuse color
        int[] ambientCo = diffuseCo;

        // Separate the light color into its RGB values
        int[] lightColor = new int[3];
        lightColor[0] = scene.getLightSource().getColor().getRed();
        lightColor[1] = scene.getLightSource().getColor().getGreen();
        lightColor[2] = scene.getLightSource().getColor().getBlue();

        int phongExp = ((Sphere) hit.getSurface()).getMaterial().getShininess();

        int ambientIntensity = 1;

        // Now we are ready to compute the final values for RGB
        // Red
        int red = (int) ((ambientCo[0] * lightColor[0]) // ambient
                + (diffuseCo[0] * lightColor[0] * Math.max(0, normal.dot(l))) // diffuse
                + (specularCo[0] * lightColor[0] * Math.pow(Math.max(0, normal.dot(h)), phongExp))); // specular
        // Green
        int green = (int) ((ambientCo[1] * lightColor[1]) // ambient
                + (diffuseCo[1] * lightColor[1] * Math.max(0, normal.dot(l))) // diffuse
                + (specularCo[1] * lightColor[1] * Math.pow(Math.max(0, normal.dot(h)), phongExp))); // specular
        // Blue
        int blue = (int) ((ambientCo[2] * lightColor[2]) // ambient
                + (diffuseCo[2] * lightColor[2] * Math.max(0, normal.dot(l))) // diffuse
                + (specularCo[2] * lightColor[2] * Math.pow(Math.max(0, normal.dot(h)), phongExp))); // specular

        // Normalize the color values to be in the range (0 - 255)
        red = normalizeColor(red);
        green = normalizeColor(green);
        blue = normalizeColor(blue);

        // Now we just need to compute the final color
        Color color = new Color(red, green, blue);

        return color.getRGB();
    }

    /**
     * Normalizes each component of this color to between [0, 255]
     *
     * @return an array of integers containing the normalized RGB values
     */
    private static int normalizeColor(int color) {
        return Math.max(Math.min(color, 255), 0);
    }

}
