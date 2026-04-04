// Creates an infinite scrolling background by using two ImageViews stacked
// vertically — when one scrolls off the bottom it is repositioned above the other.

package managers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BackgroundManager {

    private final Pane gamePane;
    private final ImageView background1;
    private final ImageView background2;

    public BackgroundManager(Pane gamePane) {
        this.gamePane = gamePane;

        Image backgroundImage = new Image(getClass().getResource("/assets/background/background_image.png").toExternalForm());

        this.background1 = new ImageView(backgroundImage);
        this.background2 = new ImageView(backgroundImage);

        setupBackground();
        gamePane.getChildren().addAll(background1, background2);
    }

    private void setupBackground() {
        // preserveRatio must be false so the image stretches to fill the pane exactly
        background1.setPreserveRatio(false);
        background1.fitWidthProperty().bind(gamePane.widthProperty());
        background1.fitHeightProperty().bind(gamePane.heightProperty());
        background1.setY(0);

        background2.setPreserveRatio(false);
        background2.fitWidthProperty().bind(gamePane.widthProperty());
        background2.fitHeightProperty().bind(gamePane.heightProperty());
        // Start directly below background1 so the two tiles form a seamless vertical strip
        background2.setY(gamePane.getPrefHeight());
    }

    public void startScrolling() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            double paneHeight = gamePane.getHeight();

            background1.setY(background1.getY() + 2);
            background2.setY(background2.getY() + 2);

            if (background1.getY() >= paneHeight) {
                background1.setY(background2.getY() - paneHeight);
            }
            if (background2.getY() >= paneHeight) {
                background2.setY(background1.getY() - paneHeight);
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}