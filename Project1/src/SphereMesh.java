import java.util.ArrayList;

/**
 * This class represents a triangle mesh of a 3D sphere in the
 * Wavefront OBJ format.
 */
public class SphereMesh {
    public final double DEGREES_OF_CIRCLE = 2 * Math.PI; // in Radians because Math.sin and Math.cos use radians
    private int divisionsU;
    private int divisionsV;
    private static ArrayList<double[]> vertices;
    private static ArrayList<String> faces;
    private static ArrayList<double[]> normals;
    private static ArrayList<double[]> textures;

    public SphereMesh(int divU, int divV) {
        divisionsU = divU;
        divisionsV = divV;
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        normals = new ArrayList<>();
        textures = new ArrayList<>();
        this.generateVertices();
        this.generateFaces();
        this.generateNormals();
        this.generateTextures();
    }

    private void generateVertices() {
        double x,y,z;
        double angleU = DEGREES_OF_CIRCLE / divisionsU;
        double angleV = (DEGREES_OF_CIRCLE/ 2) / divisionsV;

        // Vertex 1 will always be the top of the sphere (North pole)
        vertices.add(new double[]{0,1,0});

        // Generate all vertices for the sphere, excluding the poles
        for(int i=divisionsV/2-1;i>(-divisionsV/2);i--) {
            y = Math.sin(angleV*i);
            for(int j=0;j<divisionsU;j++) {
                double radius = Math.sqrt(1-y*y);
                x = radius*Math.cos(angleU*j);
                z = radius*Math.sin(angleU*j);
                vertices.add(new double[]{x,y,z});
            }
        }

        // Last vertex will always be the bottom of the sphere (South pole)
        vertices.add(new double[]{0,-1,0});
    }

    private void generateFaces() {
        // Generate top triangle fan
        for(int i=2;i<divisionsU+1;i++) {
            faces.add("f "+1+"/"+1+"/"+1+" "+i+"/"+i+"/"+i+" "+(i+1)+"/"+(i+1)+"/"+(i+1));
        }

        // Last triangles of fans are special case since they connect back to starting vertex
        faces.add("f "+1+"/"+1+"/"+1+" "+(divisionsU+1)+"/"+(divisionsU+1)+"/"+(divisionsU+1)+" "+2+"/"+2+"/"+2);

        // Generate bottom triangle fan
        int startIndex = vertices.size()-32;
        for(int i=startIndex;i<vertices.size()-1;i++) {
            faces.add("f "+vertices.size()+"/"+vertices.size()+"/"+vertices.size()+" "+i+"/"+i+"/"+i+" "+(i+1)+"/"+(i+1)+"/"+(i+1));
        }

        // Last triangles of each fan are special case since they connect back to starting vertex
        faces.add("f "+vertices.size()+"/"+vertices.size()+"/"+vertices.size()+" "
                +(vertices.size()-1)+"/"+(vertices.size()-1)+"/"+(vertices.size()-1)+" "
                +startIndex+"/"+startIndex+"/"+startIndex);

        // Generate triangle strips for the top half starting at the equator
        int passes = divisionsU*(divisionsV-2)+2;
        for(int i=2;i<passes;i++) {
            if((i-1)%divisionsU != 0) {
                faces.add("f "+i+"/"+i+"/"+i+" "+(i+1)+"/"+(i+1)+"/"+(i+1)+" "+(i+divisionsU)+"/"+(i+divisionsU)+"/"+(i+divisionsU));
                faces.add("f "+(i+1+divisionsU)+"/"+(i+1+divisionsU)+"/"+(i+1+divisionsU)+" "
                        +(i+divisionsU)+"/"+(i+divisionsU)+"/"+(i+divisionsU)+" "
                        +(i+1)+"/"+(i+1)+"/"+(i+1));
            } else {
                faces.add("f "+i+"/"+i+"/"+i+" "
                        +(i+1-divisionsU)+"/"+(i+1-divisionsU)+"/"+(i+1-divisionsU)+" "
                        +(i+divisionsU)+"/"+(i+divisionsU)+"/"+(i+divisionsU));
                faces.add("f "+(i+1)+"/"+(i+1)+"/"+(i+1)+" "
                        +(i+divisionsU)+"/"+(i+divisionsU)+"/"+(i+divisionsU)+" "
                        +(i+1-divisionsU)+"/"+(i+1-divisionsU)+"/"+(i+1-divisionsU));
            }
        }
    }

    private void generateNormals() {
        // Vertex normals for every vertex can be represented by its Cartesian coordinates
        for(double[] v : vertices) {
            double length = Math.sqrt((v[0] * v[0]) + (v[1] * v[1]) + (v[2] * v[2]));
            normals.add(new double[]{v[0]/length,v[1]/length,v[2]/length});
        }
    }

    private void generateTextures() {
        // First texture is always the North pole
        textures.add(new double[]{0,1});

        // Generate the rest of texture coordinates, excluding the South pole
        double offsetU = 1.0/divisionsU;
        double offsetV = 1.0/divisionsV;
        for(int i=1;i<divisionsV;i++) {
            for(int j=0;j<divisionsU;j++) {
                textures.add(new double[]{j*offsetU,i*offsetV});
            }
        }

        // Last texture is always the South pole
        textures.add(new double[]{0,0});
    }

    /**
     * Creates a string representation of the SphereMesh in
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
