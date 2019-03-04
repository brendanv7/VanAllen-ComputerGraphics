import javax.vecmath.Vector3d;
import java.awt.Color;

/**
 * A simple Light object to be used in a Scene
 *
 * @author Brendan Van Allen
 */
public class Light {
    private Vector3d position;
    private Color color;

    public Light (Vector3d position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Vector3d getPosition() {
        return position;
    }
}
