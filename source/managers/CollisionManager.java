package managers;
import entities.PizzaSprite;
import ui.LifeIcon;
import core.GameState;
import entities.Enemy;
import systems.CollisionUtils;
import entities.EnemyProjectile;
import entities.Salt;
import entities.Projectile;
import entities.ExtraLife;
import entities.Pepper;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Set;
import java.util.function.Consumer;

public class CollisionManager {

    public static void update(
            UIManager uiManager,
            Pane gamePane,
            Pane uiPane,
            PizzaSprite pizzaMain,
            GameState state,
            Text scoreText,
            Text highScoreText,
            LifeIcon lifeIcon1,
            LifeIcon lifeIcon2,
            LifeIcon lifeIcon3,
            AnimationTimer timer,
            Runnable onGameOver,
            Set<Enemy> activeEnemies,
            Consumer<Long> setFireCooldown,
            Runnable enablePepperShot,
            Runnable disablePepperShot) {

        // 1000ms window prevents multiple hits registering in the same frame or rapid succession
        long damageCooldown = 1000;

        for (var node : gamePane.getChildren()) {

            if (node instanceof Enemy enemy) {
                if (CollisionUtils.intersects(pizzaMain, enemy) && System.currentTimeMillis() - state.timeSinceLastTookDamage >= damageCooldown) {
                    SoundManager.playExplosionSound();
                    pizzaMain.flash();
                    state.life--;
                    state.timeSinceLastTookDamage = System.currentTimeMillis();
                }
            }

            if (node instanceof EnemyProjectile enemyProjectile) {
                if (CollisionUtils.intersects(pizzaMain, enemyProjectile) && System.currentTimeMillis() - state.timeSinceLastTookDamage >= damageCooldown) {
                    SoundManager.playExplosionSound();
                    pizzaMain.flash();
                    state.life--;
                    state.timeSinceLastTookDamage = System.currentTimeMillis();
                }
            }

            if (node instanceof Projectile projectile) {
                for (Enemy enemy : activeEnemies) {

                    // minY >= 0 prevents scoring points on enemies still above the visible viewport
                    if (enemy.getBoundsInParent().getMinY() >= 0 && CollisionUtils.intersects(projectile, enemy)) {
                        state.score += 5;
                        state.highScore = Math.max(state.highScore, state.score);
                        CollisionManager.spawnExplosion(gamePane, enemy.getBoundsInParent().getCenterX(), enemy.getBoundsInParent().getCenterY());
                        SoundManager.playExplosionSound();
                        gamePane.getChildren().removeAll(enemy, projectile);
                        break;
                    }
                }
            }

            if (node instanceof Salt salt) {
                if (CollisionUtils.intersects(salt, pizzaMain)) {
                    SoundManager.playPowerUpSound();
                    pizzaMain.flash();
                    setFireCooldown.accept(250L);
                    gamePane.getChildren().remove(salt);
                    PauseTransition reset = new PauseTransition(Duration.seconds(15));
                    reset.setOnFinished(e -> setFireCooldown.accept(750L));
                    reset.play();
                }
            }

            if (node instanceof Pepper pepper) {
                if (CollisionUtils.intersects(pepper, pizzaMain)) {
                    SoundManager.playPowerUpSound();
                    pizzaMain.flash();
                    gamePane.getChildren().remove(pepper);
                    enablePepperShot.run();
                    PauseTransition reset = new PauseTransition(Duration.seconds(15));
                    reset.setOnFinished(e -> disablePepperShot.run());
                    reset.play();
                }
            }

            if (node instanceof ExtraLife extraLife) {
                if (CollisionUtils.intersects(extraLife, pizzaMain)) {
                    if (state.life > 0 && state.life < 3) {
                        // life > 0 guard prevents granting a life on the same frame the player dies
                        state.life++;;
                        uiManager.updateLives();
                        SoundManager.playPowerUpSound();
                        pizzaMain.flash();
                    }
                    gamePane.getChildren().remove(extraLife);
                }
            }
        }

        scoreText.setText("score\t\t" + state.score);
        highScoreText.setText("hi score\t\t" + state.highScore);

        switch (state.life) {
            case 3 -> {
                lifeIcon1.setVisible(true);
                lifeIcon2.setVisible(true);
                lifeIcon3.setVisible(true);
            }
            case 2 -> {
                lifeIcon1.setVisible(true);
                lifeIcon2.setVisible(true);
                lifeIcon3.setVisible(false);
            }
            case 1 -> {
                lifeIcon1.setVisible(true);
                lifeIcon2.setVisible(false);
                lifeIcon3.setVisible(false);
            }
            case 0 -> {
                lifeIcon1.setVisible(false);
                lifeIcon2.setVisible(false);
                lifeIcon3.setVisible(false);

                pizzaMain.setVisible(false);
                CollisionManager.spawnExplosion(gamePane, pizzaMain.getBoundsInParent().getCenterX(), pizzaMain.getBoundsInParent().getCenterY());
                SoundManager.playGameOverSound();

                // removeIf excludes pizzaMain itself because its bounds are still needed
                // to position the game-over explosion on the same frame
                gamePane.getChildren().removeIf(node -> node instanceof PizzaSprite && node != pizzaMain);
                timer.stop();
                onGameOver.run();
            }
        }
    }

    public static void spawnExplosion(Pane gamePane, double centerX, double centerY) {
        ImageView explosion = new ImageView(new Image(CollisionManager.class.getResource("/assets/effect/explosion.png").toExternalForm()));
        explosion.setFitWidth(64);
        explosion.setFitHeight(64);
        explosion.setPreserveRatio(true);
        explosion.setX(centerX - explosion.getFitWidth() / 2);
        explosion.setY(centerY - explosion.getFitHeight() / 2);

        gamePane.getChildren().add(explosion);

        PauseTransition explosion1Animation = new PauseTransition(Duration.millis(300));
        explosion1Animation.setOnFinished(e -> gamePane.getChildren().remove(explosion));
        explosion1Animation.play();
    }
}
