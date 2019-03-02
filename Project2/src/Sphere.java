import javax.vecmath.Vector3d;

public class Sphere extends Surface{
    public Vector3d center;
    public double radius;

    public Sphere(Vector3d center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public boolean hit(Ray vr) {
        Vector3d temp = new Vector3d(vr.viewpoint);
        temp.sub(center);
        double part1 = vr.direction.dot(temp);
        part1 *= part1;

        double part2 = vr.direction.dot(vr.direction) * (temp.dot(temp) - radius*radius);

        return part1-part2 >= 0;
    }
}
