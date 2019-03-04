import javax.vecmath.Vector3d;

public class Ray {
    private Vector3d viewpoint;
    private Vector3d direction;

    public Ray(Vector3d viewpoint, Vector3d direction) {
        this.viewpoint = viewpoint;
        this.direction = direction;
    }

    public Ray(Ray r) {
        this.viewpoint = new Vector3d(r.viewpoint);
        this.direction = new Vector3d(r.direction);
    }

    public Vector3d getViewpoint() {
        return viewpoint;
    }

    public Vector3d getDirection() {
        return direction;
    }

    public Vector3d getPointOnRay(double time) {
        // p = e + td

        // Make a copy of the viewpoint and directions vectors so we don't change their state
        Vector3d vp = new Vector3d(viewpoint);
        Vector3d dir = new Vector3d(direction);

        // td
        dir.scale(time);

        // e + td
        vp.add(dir);

        return vp;
    }
}
