import java.util.ArrayList;

public class SurfaceGroup {
    ArrayList<Surface> surfaces;

    public SurfaceGroup() {
        surfaces = new ArrayList<>();
    }

    public void add(Surface s) {
        surfaces.add(s);
    }
}
