import apple.laf.JRSUIConstants;

import java.util.ArrayList;

/**
 * Group is simply a collection of Surfaces. Having
 * all Surfaces in a Scene in a single Group allows
 * for easier computations of pixel values.
 *
 * @author Brendan Van Allen
 */
public class Group implements Surface{
    private ArrayList<Surface> otherSurfaces;

    public Group() {
        otherSurfaces = new ArrayList<>();
    }

    /**
     * Adds the given Surface to the Group.
     *
     * @param s Surface to be added
     */
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
            HitRecord hit = s.hit(vr);

            // Determine if there was actually a hit
            if(hit != null) {
                // If the hit was closer than the current closest, then this hit is now the closest.
                closestT = Math.min(closestT, hit.getTime());
                closestHit = hit;
            }
        }

        return closestHit;
    }
}
