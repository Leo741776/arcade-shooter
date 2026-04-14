package entities;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import managers.SoundManager;

public class EnemyProjectile extends ImageView {

    private AnimationTimer behaviorTimer;

    public EnemyProjectile() {
        Image enemyProjectileImage = new Image(getClass().getResource("/assets/projectile/enemy_projectile.png").toExternalForm());
        this.setImage(enemyProjectileImage);
        this.setPreserveRatio(true);
        this.setFitWidth(50);

        // Stops the timer when the node is removed externally (e.g. by CollisionManager)
        // to prevent a leaked AnimationTimer from running after the projectile is gone
        this.parentProperty().addListener((unusedObs, unusedOldParent, newParent) -> {
            if (newParent == null) {
                stopAnimation();
            }
        });
    }

    public void fire(double currentX, double currentY, Pane gamePane) {
        this.setX(currentX - this.getFitWidth() / 2);
        this.setY(currentY);

        gamePane.getChildren().add(this);

        behaviorTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                setY(getY() + 2.5);

                if (getY() > 1024) {
                    gamePane.getChildren().remove(EnemyProjectile.this);
                    stopAnimation();
                }
            }
        };
        behaviorTimer.start();
        SoundManager.playEnemyBlasterSound();
    }

    public void stopAnimation() {
        if (behaviorTimer != null) {
            behaviorTimer.stop();
        }
    }
}