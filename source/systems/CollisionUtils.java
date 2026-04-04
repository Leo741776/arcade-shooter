// Provides pixel-level collision detection for ImageView nodes.
// Padding support allows hitboxes to be shrunk for more forgiving, gameplay-feel collisions.

package systems;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;

public class CollisionUtils {

    public static boolean intersects(ImageView a, ImageView b, double padding) {
        Bounds b1 = a.getBoundsInParent();
        Bounds b2 = b.getBoundsInParent();

        // Only b's bounds are shrunk so the caller controls leniency on the target (e.g. enemy, power-up)
        // without affecting the source's (e.g. player's) effective size
        Bounds shrunkBounds = new BoundingBox(
                b2.getMinX() + padding,
                b2.getMinY() + padding,
                b2.getWidth() - 2 * padding,
                b2.getHeight() - 2 * padding);

        return b1.intersects(shrunkBounds);
    }

    public static boolean intersects(ImageView a, ImageView b) {
        return intersects(a, b, 0);
    }
}