// Represents the player's pizza ship. Also reused as visual clones
// for the pepper triple-shot power-up rather than creating a separate class.

package entities;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class PizzaSprite extends ImageView {

    public PizzaSprite(double x, double y) {
        Image pizzaSpriteImage = new Image(getClass().getResource("/assets/sprite/pizza.png").toExternalForm());
        this.setImage(pizzaSpriteImage);
        this.setPreserveRatio(true);
        this.setFitWidth(75);
        this.setX(x);
        this.setY(y);
    }

    public void flash() {
        // 50ms * 10 cycles = 500ms total; fast enough to read as a hit without obscuring gameplay
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(50), this);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(10);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }
}