import java.awt.*;

/**
 * Represents a 3D scene of Surfaces.
 */
public class Scene {
    public final Color BACKGROUND_COLOR = Color.BLACK;
    private Group surfaces;
    private Light lightSource;

    public Scene(Light lightSource) {
        surfaces = new Group();
        this.lightSource = lightSource;
    }

    /**
     * Adds the given Surface to the Scene.
     *
     * @param s
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
