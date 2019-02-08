import java.util.ArrayList;

/**
 * This class represents a triangle mesh of a 3D cylinder in the
 * Wavefront OBJ format.
 */
public class CylinderMesh {
    public final double DEGREES_OF_CIRCLE = 2 * Math.PI;
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
    }

    private void generateVertices() {
        vertices.add(new double[]{0,1,0});

        for(int i=0;i<divisionsU;i++) {
            double x = Math.round(Math.cos(angle * i) * 1000.0) / 1000.0;
            double z = Math.round(Math.sin(angle * i) * 1000.0) / 1000.0;
            vertices.add(new double[]{x,1,z});
        }
    }

    public static void main(String args[]) {
        CylinderMesh test = new CylinderMesh(32);
        test.generateVertices();
        int times = 1;
        for(double[] n : vertices) {
            System.out.println(times + " " + n[0] + " " + n[1] + " " + n[2]);
            times++;
        }
    }
}
