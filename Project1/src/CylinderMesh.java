import java.util.ArrayList;

/**
 * This class represents a triangle mesh of a 3D cylinder in the
 * Wavefront OBJ format.
 */
public class CylinderMesh {
    public final double DEGREES_OF_CIRCLE = 2 * Math.PI; // in Radians because Math.sin and Math.cos use radians
    private int divisionsU = 32; // Default value set to 32 as per project instructions
    private double angle;
    private static ArrayList<double[]> vertices;
    private static ArrayList<String> faces;
    private static ArrayList<double[]> normals;
    private static ArrayList<double[]> textures;

    /**
     * Creates a CylinderMesh object with the default
     * number of U divisions.
     */
    public CylinderMesh() {
        angle = DEGREES_OF_CIRCLE / divisionsU;
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        normals = new ArrayList<>();
        textures = new ArrayList<>();
        this.generateVertices();
        this.generateTextures();
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
        textures = new ArrayList<>();
        this.generateVertices();
        this.generateTextures();
        this.generateNormals();
        this.generateFaces();
    }

    private void generateVertices() {
        // Vertex 1 will always be the center of the top circle
        vertices.add(new double[]{0,1,0});

        // Generate vertices counter-clockwise starting from Θ = 0
        // Note: The indexing scheme I chose indexes the vertices in order for the triangle strip
        for(int i=0;i<divisionsU;i++) {
            // Compute the x and z coordinates
            double x = Math.cos(angle * i);
            double z = Math.sin(angle * i);

            // The vertices of the top and bottom circles have the same x and z values, but with opposite y values
            vertices.add(new double[]{x,1,z});
            vertices.add(new double[]{x,-1,z});
        }

        // The last vertex will always be the center of the bottom circle
        vertices.add(new double[]{0,-1,0});
    }

    private void generateFaces() {
        int topNormal = 1; // Index of vertex normal for top cap
        int bottomNormal = normals.size()-1; // Index of vertex normal for bottom cap

        // Order of faces does not matter, so generate both triangle fans
        for(int i=1;i<divisionsU*2-1;i+=2) {
            faces.add("f "+1+"//"+topNormal+" "+(i+1)+"//"+topNormal+" "+(i+3)+"//"+topNormal);
            faces.add("f "+(divisionsU*2+2)+"//"+bottomNormal+" "+(i+2)+"//"+bottomNormal+" "+(i+4)+"//"+bottomNormal);
        }

        // Last triangles of each fan are special case since they connect back to starting vertex
        faces.add("f "+1+"//"+topNormal+" "+(divisionsU*2)+"//"+topNormal+" "+2+"//"+topNormal);
        faces.add("f "+(divisionsU*2+2)+"//"+bottomNormal+" "+(divisionsU*2+1)+"//"+bottomNormal+" "+3+"//"+bottomNormal);

        // Generate triangle strip
        for(int i=2;i<divisionsU*2-1;i+=2) {
            faces.add("f "+i+"/"+(i-1)+"/"+i+" "+(i+1)+"/"+i+"/"+i+" "+(i+2)+"/"+(i+1)+"/"+i);
            faces.add("f "+(i+1)+"/"+i+"/"+i+" "+(i+2)+"/"+(i+1)+"/"+i+" "+(i+3)+"/"+(i+2)+"/"+i);
        }

        // Last two triangles of the strip are special case since they connect back to the starting vertices
        faces.add("f "+(divisionsU*2)+"/"+(divisionsU*2-1)+"/"+(normals.size()-3)+" "
                +(divisionsU*2+1)+"/"+(divisionsU*2)+"/"+(normals.size()-3)+" "
                +2+"/"+(textures.size()-2)+"/" +(normals.size()-3));
        faces.add("f "+(divisionsU*2+1)+"/"+(divisionsU*2)+"/"+(normals.size()-2)+" "
                +2+"/"+(textures.size()-2)+"/"+(normals.size()-2)+" "
                +3+"/"+(textures.size()-1)+"/"+(normals.size()-2));
    }

    private void generateNormals() {
        // First normal is shared by all faces of the top circle
        normals.add(new double[]{0,1,0});

        // Generate normals for the triangle strip
        double normX;
        double normZ;
        double length;
        for(int i=1;i<vertices.size()-3;i++) {
            normX = (vertices.get(i)[0] + vertices.get(i+1)[0] + vertices.get(i+2)[0])/3;
            normZ = (vertices.get(i)[2] + vertices.get(i+1)[2] + vertices.get(i+2)[2])/3;
            length = Math.sqrt((normX*normX)+(normZ*normZ));
            normals.add(new double[]{normX/length,0,normZ/length});
        }
        // Last two normals of strip are special case since the vertices are shared with the starting triangles
        normX = (vertices.get(divisionsU*2)[0] + vertices.get(divisionsU*2+1)[0] + vertices.get(2)[0])/3;
        normZ = (vertices.get(divisionsU*2)[2] + vertices.get(divisionsU*2+1)[2] + vertices.get(2)[2])/3;
        length = Math.sqrt((normX*normX)+(normZ*normZ));
        normals.add(new double[]{normX/length,0,normZ/length});

        normX = (vertices.get(divisionsU*2+1)[0] + vertices.get(2)[0] + vertices.get(3)[0])/3;
        normZ = (vertices.get(divisionsU*2+1)[2] + vertices.get(2)[2] + vertices.get(3)[2])/3;
        length = Math.sqrt((normX*normX)+(normZ*normZ));
        normals.add(new double[]{normX/length,0,normZ/length});

        // Last normal is shared by all faces of the bottom circle
        normals.add(new double[]{0,-1,0});
    }

    private void generateTextures() {
        double offset = 1.0/divisionsU;
        for(int i=0;i<divisionsU+1;i++){
            textures.add(new double[]{i*offset,1});
            textures.add(new double[]{i*offset,0});
        }
    }

    /**
     * Creates a string representation of the CylinderMesh in
     * Wavefront OBJ format.
     */
    @Override
    public String toString() {
        String obj = "";
        for(double[] v : vertices) {
            obj += "v " + v[0] + " " + v[1] + " " + v[2] + "\n";
        }
        for(double[] t : textures) {
            obj += "vt " + t[0] + " " + t[1] + "\n";
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
