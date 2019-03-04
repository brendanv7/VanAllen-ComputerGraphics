import java.awt.*;

/**
 * Represents a 3D scene of Surfaces.
 *
 * @author Brendan Van Allen
 */
public class Scene {
    public final Color BACKGROUND_COLOR = Color.BLACK;
    private Group surfaces;
    private Light lightSource;

    /**
     * Creates a scene with the given light source
     * @param lightSource
     */
    public Scene(Light lightSource) {
        surfaces = new Group();
        this.lightSource = lightSource;
    }

    /**
     * Adds the given Surface to the Scene.
     *
     * @param s Surface to be added
     */
    public void add(Surface s) {
        surfaces.add(s);
    }

    public Group getSurfaces() {
        return surfaces;
    }

    public Light getLightSource() {
        return lightSource;
    }
}
