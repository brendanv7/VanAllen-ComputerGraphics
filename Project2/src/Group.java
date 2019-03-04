import java.util.ArrayList;

public class Group implements Surface{
    private ArrayList<Surface> otherSurfaces;

    public Group() {
        otherSurfaces = new ArrayList<>();
    }

    public void add(Surface s) {
        otherSurfaces.add(s);
    }

    @Override
    /**
     * Determines if the given Ray intersects any Surface in the Group.
     *
     * @param vr the viewing ray
     * @return If an intersection occurs, a HitRecord is returned with the
     * closest intersection to the origin of the Ray (time), the normal vector at
     * the point of intersection, and the Surface that was intersected. If no
     * intersection occurs, then the return value is null.
     */
    public HitRecord hit(Ray vr) {
        double closestT = Integer.MAX_VALUE;
        HitRecord closestHit = null;

        // Loop through all of the surfaces in the group
        for(Surface s : otherSurfaces) {
            // Get the HitRecord for the surface
            closestHit = s.hit(vr);

            // Determine if there was actually a hit
            if(closestHit != null) {
                // If the hit was closer than the current closest, then this hit is now the closest.
                closestT = Math.min(closestT, closestHit.getTime());
            }
        }

        return closestHit;
    }
}
