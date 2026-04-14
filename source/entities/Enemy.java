package entities;

import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

import java.util.Set;

public class Enemy extends ImageView {

    private int randomValueForStartingX = (int) (Math.random() * 100);
    private int randomValueForDirection = (int) (Math.random() * 100);
    private int startingX = 0;

    private long timeSinceLastFired = 0;
    private final long FIRE_COOLDOWN = 750;

    public Enemy() {
        Image enemyImage = new Image(getClass().getResource("/assets/sprite/enemy.png").toExternalForm());
        this.setImage(enemyImage);
        this.setPreserveRatio(true);
        this.setFitWidth(75);
    }

    public void spawn(Pane gamePane) {

        startingX = (randomValueForStartingX <= 50) ? 256 : 512;

        this.setX(startingX);
        this.setY(-100);

        gamePane.getChildren().add(this);

        double minX = 0;
        double maxX = 768 - this.getFitWidth();
        double amplitude = 250;

        Polyline zigzagPattern;

        if (randomValueForDirection <= 50) {
            zigzagPattern = new Polyline(
                    clamp(startingX, minX, maxX), -100,
                    clamp(startingX + amplitude, minX, maxX), 128,
                    clamp(startingX, minX, maxX), 256,
                    clamp(startingX - amplitude, minX, maxX), 384,
                    clamp(startingX, minX, maxX), 512,
                    clamp(startingX + amplitude, minX, maxX), 640,
                    clamp(startingX, minX, maxX), 768,
                    clamp(startingX - amplitude, minX, maxX), 896,
                    clamp(startingX, minX, maxX), 1024);
        } else {
            zigzagPattern = new Polyline(
                    clamp(startingX, minX, maxX), -100,
                    clamp(startingX - amplitude, minX, maxX), 128,
                    clamp(startingX, minX, maxX), 256,
                    clamp(startingX + amplitude, minX, maxX), 384,
                    clamp(startingX, minX, maxX), 512,
                    clamp(startingX - amplitude, minX, maxX), 640,
                    clamp(startingX, minX, maxX), 768,
                    clamp(startingX + amplitude, minX, maxX), 896,
                    clamp(startingX, minX, maxX), 1024);
        }

        PathTransition zigzagMovement = new PathTransition(Duration.seconds(15), zigzagPattern, this);
        zigzagMovement.setOnFinished(e -> gamePane.getChildren().remove(this));
        zigzagMovement.play();
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public void fire(Pane gamePane, Set<EnemyProjectile> projectileSet) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - timeSinceLastFired >= FIRE_COOLDOWN) {

            var scenePosition = this.localToScene(this.getBoundsInLocal());
            // localToScene is needed because PathTransition moves the node via transforms,
            // so getX()/getY() no longer reflect the actual on-screen position
            double currentX = scenePosition.getMinX() + (this.getFitWidth() / 2);
            double currentY = scenePosition.getMaxY();

            // Guard against firing while the enemy is still off-screen above the viewport
            if (currentY > 0 && currentY < gamePane.getHeight()) {
                // Re-check cooldown here because multiple enemies share the same game loop tick
                if (currentTime - timeSinceLastFired >= FIRE_COOLDOWN) {
                    EnemyProjectile projectile = new EnemyProjectile();
                    projectile.fire(currentX, currentY, gamePane);
                    projectileSet.add(projectile);
                    timeSinceLastFired = currentTime;
                }
            }
        }
    }
}