import javax.vecmath.Matrix4d;
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

    public static void main(String args[]) {
        // Create the scene
        Scene scene = createScene();
        Group surfaces = scene.getSurfaces();

        BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

        // Iterate through each pixel left to right, then top to bottom
        for(int i=0;i<HEIGHT;i++) {
            for(int j=0;j<WIDTH;j++) {
                Ray viewRay = computeViewingRay(i,j);
                // if ray hits an object with t in [0,inf) then
                HitRecord hit = surfaces.hit(viewRay);
                if(hit != null) {
                    img.setRGB(j,i, computeShading(scene, hit));
                    // evaluate shading model and set pixel to the resulting color
                } else {
                    // set pixel to the background color
                    img.setRGB(j,i,scene.BACKGROUND_COLOR.getRGB());
                }
            }
        }

        try {
            File file = new File("Scene.jpg");
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private static Ray computeViewingRay(int i, int j) {
        double u = L + (R - L)*(i + 0.5)/WIDTH;
        double v = B + (T - B)*(j + 0.5)/HEIGHT;
        Vector3d direction = new Vector3d(u*U,v*V,-D*W);
        return new Ray(VIEWPOINT,direction);
    }

    private static Scene createScene() {
        Light lightSource = new Light(new Vector3d(-2,2,1), Color.WHITE);
        Scene scene = new Scene(lightSource);

        scene.add(new Sphere(new Vector3d(0,0,2),1, new Material(10,Color.RED)));
        scene.add(new Sphere(new Vector3d(0,1,2),1, new Material(1000,Color.BLUE)));



        return scene;
    }

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

        System.out.println(red + " " + green + " " + blue);
        // Now we just need to compute the final color
        Color color = new Color(red, green, blue);

        return color.getRGB();
    }

}
