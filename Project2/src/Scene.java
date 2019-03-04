import java.awt.*;

/**
 * Represents a 3D scene of Surfaces.
 */
public class Scene {
    final Color backgroundColor = Color.BLACK;
    private Group surfaces;
    // light source

    public Scene() {
        surfaces = new Group();
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
}
