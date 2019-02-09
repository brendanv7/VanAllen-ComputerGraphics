import java.util.ArrayList;

/**
 * This class represents a triangle mesh of a 3D cylinder in the
 * Wavefront OBJ format.
 */
public class CylinderMesh {
    public final double DEGREES_OF_CIRCLE = 2 * Math.PI; // in Radians because Math.sin and Math.cos use radians
    private int divisionsU;
    private double angle;
    private static ArrayList<double[]> vertices;

    /**
     * Creates a CylinderMesh object with the given number
     * of U divisions.
     *
     * @param divU the number of U divisions
     */
    public CylinderMesh(int divU) {
        divisionsU = divU;
        angle = DEGREES_OF_CIRCLE / divisionsU;
        vertices = new ArrayList<>();
        this.generateVertices();
    }

    private void generateVertices() {
        // Vertex 1 will always be the center of the top circle
        vertices.add(new double[]{0,1,0});

        for(int i=0;i<divisionsU;i++) {
            // Compute x and z coordinates counter-clockwise starting from Θ = 0
            double x = Math.round(Math.cos(angle * i) * 1000.0) / 1000.0;
            double z = Math.round(Math.sin(angle * i) * 1000.0) / 1000.0;

            // The vertices of the top and bottom circles have the same x and z values, but with opposite y values
            vertices.add(new double[]{x,1,z});
            vertices.add(new double[]{x,-1,z});
        }

        // The last vertex will always be the center of the bottom circle
        vertices.add(new double[]{0,-1,0});
    }

    public static void main(String args[]) {
        CylinderMesh test = new CylinderMesh(32);
        int times = 1;
        for(double[] n : vertices) {
            System.out.println(times + " " + n[0] + " " + n[1] + " " + n[2]);
            times++;
        }
    }
}
