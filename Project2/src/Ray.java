import javax.vecmath.Vector3d;

public class Ray {
    public Vector3d viewpoint;
    public Vector3d direction;

    public Ray(Vector3d viewpoint, Vector3d direction) {
        this.viewpoint = viewpoint;
        this.direction = direction;
    }

    public Ray(Ray r) {
        this.viewpoint = new Vector3d(r.viewpoint);
        this.direction = new Vector3d(r.direction);
    }
}
