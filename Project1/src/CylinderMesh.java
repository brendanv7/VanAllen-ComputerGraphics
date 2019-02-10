import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a triangle mesh of a 3D cylinder in the
 * Wavefront OBJ format.
 */
public class CylinderMesh {
    public final double DEGREES_OF_CIRCLE = 2 * Math.PI; // in Radians because Math.sin and Math.cos use radians
    private int divisionsU = 32;
    private double angle;
    private static ArrayList<double[]> vertices;
    private static ArrayList<String> faces;
    private static ArrayList<double[]> normals;

    /**
     * Creates a CylinderMesh object with the given number
     * of U divisions.
     *
     */
    public CylinderMesh() {
        angle = DEGREES_OF_CIRCLE / divisionsU;
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        normals = new ArrayList<>();
        this.generateVertices();
        this.generateNormals();
        this.generateFaces();
    }

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
        faces = new ArrayList<>();
        normals = new ArrayList<>();
        this.generateVertices();
        this.generateNormals();
        this.generateFaces();
    }

    private void generateVertices() {
        // Vertex 1 will always be the center of the top circle
        vertices.add(new double[]{0,1,0});

        for(int i=0;i<divisionsU;i++) {
            // Compute x and z coordinates counter-clockwise starting from Θ = 0
            double x = Math.round(Math.cos(angle * i) * 1000.0) / 1000.0;
            double z = Math.round(Math.sin(angle * i) * 1000.0) / 1000.0;
//
//            double x = Math.cos(angle * i);
//            double z = Math.sin(angle * i);

            // The vertices of the top and bottom circles have the same x and z values, but with opposite y values
            vertices.add(new double[]{x,1,z});
            vertices.add(new double[]{x,-1,z});
        }

        // The last vertex will always be the center of the bottom circle
        vertices.add(new double[]{0,-1,0});
    }

    private void generateFaces() {
        // Order of faces does not matter, so generate both triangle fans
        int topNormal = 1;
        int bottomNormal = normals.size()-1;
        for(int i=1;i<divisionsU*2-1;i+=2) {
            faces.add("f "+1+"//"+topNormal+" "+(i+1)+"//"+topNormal+" "+(i+3)+"//"+topNormal);
            faces.add("f "+(divisionsU*2+2)+"//"+bottomNormal+" "+(i+2)+"//"+bottomNormal+" "+(i+4)+"//"+bottomNormal);
//            faces.add(new int[]{1,i+1,i+3});
//            faces.add(new int[]{divisionsU*2+2,i+2,i+4});
        }
        // Last triangles of each fan are special case since they connect back to starting vertex
        faces.add("f "+1+"//"+topNormal+" "+(divisionsU*2)+"//"+topNormal+" "+2+"//"+topNormal);
        faces.add("f "+(divisionsU*2+2)+"//"+bottomNormal+" "+(divisionsU*2-1)+"//"+bottomNormal+" "+3+"//"+bottomNormal);
//        faces.add(new int[]{1,divisionsU*2,2});
//        faces.add(new int[]{divisionsU*2+2,divisionsU*2-1,3});

        // Generate triangle strip
        for(int i=2;i<divisionsU*2;i++) {
//            // Vertex indices
//            int v1 = i;
//            int v2 = i+1;
//            int v3 = i+2;
//
//            // Vertex normal indices
//            int n1 = v1/2+1;
//            int n2 = v2/2+1;
//            int n3 = v3/2+1;

//            faces.add("f "+v1+"//"+n1+" "+v2+"//"+n2+" "+v3+"//"+n3);
//            faces.add(new int[]{i,i+1,i+2});
            faces.add("f "+i+"//"+i+" "+(i+1)+"//"+i+" "+(i+2)+"//"+i);
        }
        // Last two triangles of the strip are special case since they connect back to the starting vertices
        faces.add("f "+(divisionsU*2)+"//"+(normals.size()-3)+" "+(divisionsU*2+1)+"//"+(normals.size()-3)+" "+2+"//"+(normals.size()-3));
        faces.add("f "+(divisionsU*2+1)+"//"+(normals.size()-2)+" "+2+"//"+(normals.size()-2)+" "+3+"//"+(normals.size()-2));
//        faces.add(new int[]{divisionsU*2,divisionsU*2+1,2});
//        faces.add(new int[]{divisionsU*2+1,2,3});
    }

    private void generateNormals() {
        // First normal is shared by all faces of the top circle
        normals.add(new double[]{0,1,0});

        // Generate normals for the triangle strip
        double sumX;
        double sumZ;
        for(int i=1;i<vertices.size()-3;i++){
            sumX = vertices.get(i)[0] + vertices.get(i+1)[0] + vertices.get(i+2)[0];
            sumZ = vertices.get(i)[2] + vertices.get(i+1)[2] + vertices.get(i+2)[2];
            normals.add(new double[]{sumX/3,0,sumZ/3});
        }
        // Last two normals of strip are special case since the vertices are shared with the starting triangles
        sumX = vertices.get(divisionsU*2)[0] + vertices.get(divisionsU*2+1)[0] + vertices.get(2)[0];
        sumZ = vertices.get(divisionsU*2)[2] + vertices.get(divisionsU*2+1)[2] + vertices.get(2)[2];
        normals.add(new double[]{sumX/3,0,sumZ/3});

        sumX = vertices.get(divisionsU*2+1)[0] + vertices.get(2)[0] + vertices.get(3)[0];
        sumZ = vertices.get(divisionsU*2+1)[2] + vertices.get(2)[2] + vertices.get(3)[2];
        normals.add(new double[]{sumX/3,0,sumZ/3});

        // Last normal is shared by all faces of the bottom circle
        normals.add(new double[]{0,-1,0});
    }

    public static void main(String args[]) {
        CylinderMesh test = new CylinderMesh(32);
        System.out.println(test.toString());
    }

    @Override
    public String toString() {
        String obj = "";
        for(double[] v : vertices) {
            obj += "v " + v[0] + " " + v[1] + " " + v[2] + "\n";
        }
        for(double[] n : normals) {
            obj += "vn " + n[0] + " " + n[1] + " " + n[2] + "\n";
        }
        for(String f : faces) {
            obj += f + "\n";
        }
        return obj;
    }
}
