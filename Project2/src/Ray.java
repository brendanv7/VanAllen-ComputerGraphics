import javax.vecmath.Vector3d;

/**
 * Ray is actually a viewing ray to be used to calculate
 * pixels values for an image.
 *
 * @author Brendan Van Allen
 */
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

    /**
     * Gets a 3D point that lies on the Ray at t = time
     *
     * @param time the time on the Ray where the point is
     * @return 3D vector representing the point
     */
    public Vector3d getPointOnRay(double time) {
        // Make a copy of the viewpoint and directions vectors so we don't change their state
        Vector3d vp = new Vector3d(viewpoint);
        Vector3d dir = new Vector3d(direction);

        // t*d
        dir.scale(time);

        // e + t*d
        vp.add(dir);

        return vp;
    }
}
