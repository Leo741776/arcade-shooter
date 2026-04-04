// Represents a single life indicator in the HUD.
// Reuses the player's pizza.png sprite at a smaller size to visually tie the icon to the player.

package ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LifeIcon extends ImageView {

    public LifeIcon(double x, double y) {
        Image lifeIconImage = new Image(getClass().getResource("/assets/sprite/pizza.png").toExternalForm());
        this.setImage(lifeIconImage);
        this.setPreserveRatio(true);
        this.setFitWidth(50);

        this.setX(x);
        this.setY(y);
    }
}