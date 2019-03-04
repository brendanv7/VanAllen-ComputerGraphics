import javax.vecmath.Vector3d;

public class HitRecord {
    private double time;
    private Vector3d normal;
    private Surface object;

    public HitRecord (double time, Vector3d normal, Surface object) {
        this.time = time;
        this.normal = normal;
        this.object = object;
    }

    public double getTime() {
        return time;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public Surface getObject() {
        return object;
    }
}
