import javax.vecmath.Vector3d;

/**
 * HitRecord is used to determine if a Ray intersects
 * with a given object, and helps with pixel computations
 * so that objects nearer to the camera are placed in front
 * of objects that are further from the camera.
 *
 * @author Brendan Van Allen
 */
public class HitRecord {
    private double time;
    private Vector3d normal;
    private Vector3d point;
    private Surface surface;

    public HitRecord (double time, Vector3d normal, Vector3d point, Surface surface) {
        this.time = time;
        this.normal = normal;
        this.point = point;
        this.surface = surface;
    }

    public double getTime() {
        return time;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public Surface getSurface() {
        return surface;
    }

    public Vector3d getPoint() {
        return point;
    }
}
