import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes the Mesh information to a file.
 * @author Brendan Van Allen (with help from www.codejava.net)
 *
 */
public class WriteFile {

    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("MyFile.obj", true);
            CylinderMesh cm = new CylinderMesh(16);
            writer.write(cm.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}