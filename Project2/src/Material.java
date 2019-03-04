import java.awt.*;

/**
 * A Material represents the material properties
 * of a Surface.
 *
 * @author Brendan Van Allen
 */
public class Material {
    private int shininess;
    private Color color;

    public Material(int shininess, Color color) {
        this.shininess = shininess;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getShininess() {
        return shininess;
    }
}
