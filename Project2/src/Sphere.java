import javax.vecmath.Vector3d;
import java.awt.Color;

public class Sphere implements Surface{
    private Vector3d center;
    private double radius;
    private Color color;

    public Sphere(Vector3d center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    @Override
    /**
     * Determines if the given Ray intersects this sphere.
     *
     * @param vr the viewing ray
     * @return If an intersection occurs, a HitRecord is returned with the
     * closest intersection to the origin of the Ray (time), the normal vector at
     * the point of intersection, and this Surface. If no intersection occurs,
     * then the return value is null.
     */
    public HitRecord hit(Ray vr) {
        // Make a copy of the viewpoint vector so we don't change its state
        Vector3d temp = new Vector3d(vr.getViewpoint());

        temp.sub(center); // (e - c)

        Vector3d direction = vr.getDirection();

        double part1 = direction.dot(temp); // (d · (e-c))
        part1 *= part1; // square it

        // (d · d)((e-c) · (e-c) - R^2)
        double part2 = direction.dot(direction) * (temp.dot(temp) - radius*radius);

        double discriminant = part1 - part2;

        HitRecord hr; // This is what we will eventually return

        if(discriminant >= 0) {
            double t1 = ( -(direction.dot(temp)) + Math.sqrt(discriminant)) / direction.dot(direction);
            double t2 = ( -(direction.dot(temp)) - Math.sqrt(discriminant)) / direction.dot(direction);

            double time = Math.min(t1,t2);
            // Normal n at point p = 2(p - c)
            Vector3d normal = vr.getPointOnRay(time); // p
            normal.sub(center); // p - c
            normal.scale(2); // 2(p - c)

            hr = new HitRecord(time,normal,this);
        } else {
            hr = null;
        }

        return hr;
    }

    public Color getColor() {
        return color;
    }
}
