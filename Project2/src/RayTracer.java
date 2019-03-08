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
    public static final double L = -1.77778; //
    public static final double R = 1.77778;  // View plane
    public static final double B = -1.0; //   bounds
    public static final double T = 1.0;  //
    public static final int D = 1; // Distance from viewpoint to image plane
    public static final Vector3d U = new Vector3d(1,0,0);
    public static final Vector3d V = new Vector3d(0,1,0);
    public static final Vector3d W = new Vector3d(0,0,-1);
    public static final Vector3d VIEWPOINT = new Vector3d(0,0,0);

    public static void main(String args[]) {
        // Create the scene
        Scene scene = createScene();
        Group surfaces = scene.getSurfaces();

        // The image we will manipulate
        BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

        // Iterate through each pixel left to right, then top to bottom
        for(int j=0;j<HEIGHT;j++) {
            for(int i=0;i<WIDTH;i++) {
                Ray viewRay = computeViewingRay(i,j);
                HitRecord hit = surfaces.hit(viewRay);
                // Determine if there was a hit
                if(hit != null) {
                    // evaluate shading model and set pixel to the resulting color
                    img.setRGB(i,j, computeShading(scene, hit));
                } else {
                    // set pixel to the background color
                    img.setRGB(i,j,scene.BACKGROUND_COLOR.getRGB());
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
     * @param i current pixel x value
     * @param j current pixel y value
     * @return
     */
    private static Ray computeViewingRay(int i, int j) {
        double u = L + (R - L)*(i + 0.5)/WIDTH;
        double v = B + (T - B)*(j + 0.5)/HEIGHT;

        // -d * W
        Vector3d d1 = new Vector3d(W);
        d1.scale(-D);

        // u * U
        Vector3d d2 = new Vector3d(U);
        d2.scale(u);

        // v * V
        Vector3d d3 = new Vector3d(V);
        d3.scale(v);

        // Add them all up for the final direction
        Vector3d direction = new Vector3d(d1);
        direction.add(d2);
        direction.add(d3);

        return new Ray(VIEWPOINT,direction);
    }

    /*
     * Hard-coded scene
     */
    private static Scene createScene() {
        Light lightSource = new Light(new Vector3d(0,-3,2), Color.WHITE);
        Scene scene = new Scene(lightSource);

        scene.add(new Sphere(new Vector3d(0,0,2),.5, new Material(1,Color.RED)));
        scene.add(new Sphere(new Vector3d(1,0,3),.5, new Material(10,Color.MAGENTA)));
        scene.add(new Sphere(new Vector3d(0,-1,3), .5, new Material(100, Color.DARK_GRAY)));

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

        double[] diffuseCo = { // The diffuse coefficients for R, G, and B normalized to [0,1]
                ((Sphere) hit.getSurface()).getMaterial().getColor().getRed() / 255.0,
                ((Sphere) hit.getSurface()).getMaterial().getColor().getGreen() / 255.0,
                ((Sphere) hit.getSurface()).getMaterial().getColor().getBlue() /  255.0
        };

        // From the textbook, the specular color will be gray
        double[] specularCo = { // The specular coefficients for R, G, B normalized to [0,1]
                Color.GRAY.getRed() / 255.0,
                Color.GRAY.getGreen() / 255.0,
                Color.GRAY.getBlue() / 255.0
        };

        // From the textbook, surface ambient color can be set to the diffuse color
        double[] ambientCo = diffuseCo;

        // Separate the light color into its RGB values, normalized to [0,1]
        double[] lightColor = {
                scene.getLightSource().getColor().getRed() / 255.0,
                scene.getLightSource().getColor().getGreen() / 255.0,
                scene.getLightSource().getColor().getBlue() / 255.0
        };

        int phongExp = ((Sphere) hit.getSurface()).getMaterial().getShininess();

        // Now we are ready to compute the final values for RGB
        // Red
        double red = ((ambientCo[0] * lightColor[0]) // ambient
                + (diffuseCo[0] * lightColor[0] * Math.max(0, normal.dot(l))) // diffuse
                + (specularCo[0] * lightColor[0] * Math.pow(Math.max(0, normal.dot(h)), phongExp))); // specular
        // Green
        double green = ((ambientCo[1] * lightColor[1]) // ambient
                + (diffuseCo[1] * lightColor[1] * Math.max(0, normal.dot(l))) // diffuse
                + (specularCo[1] * lightColor[1] * Math.pow(Math.max(0, normal.dot(h)), phongExp))); // specular
        // Blue
        double blue = ((ambientCo[2] * lightColor[2]) // ambient
                + (diffuseCo[2] * lightColor[2] * Math.max(0, normal.dot(l))) // diffuse
                + (specularCo[2] * lightColor[2] * Math.pow(Math.max(0, normal.dot(h)), phongExp))); // specular

        // Denormalize the colors into the range [0,255]
        red *= 255;
        green *= 255;
        blue *= 255;

        // Cap the RGB values at 255 so Color doesn't  yell at us
        if(red > 255) {
            red = 255;
        }
        if(green > 255) {
            green = 255;
        }
        if(blue > 255) {
            blue = 255;
        }

        int redInt = (int) Math.round(red);
        int greenInt = (int) Math.round(green);
        int blueInt = (int) Math.round(blue);
        System.out.println(redInt + " " + greenInt + " "+ blueInt);

        // Now we just need to compute the final color
        Color color = new Color(redInt, greenInt, blueInt);

        return color.getRGB();
    }

}
