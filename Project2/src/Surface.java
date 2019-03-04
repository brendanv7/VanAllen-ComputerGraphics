/**
 * A simple interface that represents any object
 * in a Scene.
 *
 * @author Brendan Van Allen
 */
public interface Surface {

    /**
     * The only required method for a Surface.
     *
     * @param vr the viewing Ray
     * @return HitRecord that holds information about Ray-Surface intersection.
     */
    HitRecord hit(Ray vr);
}
