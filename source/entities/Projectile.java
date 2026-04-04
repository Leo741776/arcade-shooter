// Projectile fired upward by the player. Uses a Timeline instead of
// AnimationTimer so the firing rate stays consistent with the game's frame budget.

package entities;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Projectile extends ImageView {

    public Projectile() {
        Image projectileImage = new Image(getClass().getResource("/assets/projectile/projectile.png").toExternalForm());
        this.setImage(projectileImage);
        this.setPreserveRatio(true);
        this.setFitWidth(50);
    }

    public void fire(double currentX, double currentY, Pane gamePane) {
        this.setX(currentX - this.getFitWidth() / 2);
        // Offset upward so the projectile appears to emerge from the top of the sprite
        this.setY(currentY - 25);

        gamePane.getChildren().add(this);

        // 16ms matches ~60 FPS; keeps projectile speed frame-rate independent
        Timeline projectileTimeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> {
            this.setY(this.getY() - 10);
            // -100 gives a small buffer past the top edge to avoid a visible pop on removal
            if (this.getY() < -100) {
                gamePane.getChildren().remove(this);
            }
        }));
        projectileTimeline.setCycleCount(Animation.INDEFINITE);
        projectileTimeline.play();
    }
}