import java.util.ArrayList;

public class Group {
    ArrayList<Surface> otherSurfaces;

    public Group() {
        otherSurfaces = new ArrayList<>();
    }

    public void add(Surface s) {
        otherSurfaces.add(s);
    }
}
