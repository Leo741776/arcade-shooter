package core;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import managers.BackgroundManager;
import managers.GameManager;
import managers.SoundManager;
import managers.UIManager;

public class Main extends Application {

    private GameState gameState;
    private Font pixelFont;

    private BackgroundManager backgroundManager;
    private UIManager uiManager;
    private GameManager gameManager;

    private boolean gameHasStarted = false;
    private boolean gameOver = false;

    @Override
    public void start(Stage primaryStage) {

        gameState = new GameState();

        Pane gamePane = new Pane();
        gamePane.setPrefSize(768, 1024);
        gamePane.setMinSize(768, 1024);
        gamePane.setMaxSize(768, 1024);
        // Clip prevents sprites from rendering outside the fixed game boundary
        gamePane.setClip(new Rectangle(768, 1024));

        Pane uiPane = new Pane();
        uiPane.setPrefSize(768, 1024);
        uiPane.setMinSize(768, 1024);
        uiPane.setMaxSize(768, 1024);

        Group gameGroup = new Group(gamePane, uiPane);
        StackPane root = new StackPane(gameGroup);

        pixelFont = Font.loadFont(getClass().getResource("/assets/custom_font/arcade_font.TTF").toExternalForm(), 35);

        backgroundManager = new BackgroundManager(gamePane);
        uiManager = new UIManager(gamePane, uiPane, gameState, pixelFont);

        // Callback keeps GameManager decoupled from Main; signals game-over without a direct dependency
        Runnable showContinueScreenCallback = () -> {
            uiManager.showContinueScreen();
            gameOver = true;
        };

        gameManager = new GameManager(gamePane, uiPane, uiManager, gameState, showContinueScreenCallback);

        backgroundManager.startScrolling();
        uiManager.setupInitialScreen();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Pizza Time");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(768);
        primaryStage.setHeight(1024);
        primaryStage.setFullScreen(false);
        primaryStage.show();

        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            gameManager.handleKeyPress(code);

            if (!gameHasStarted && e.getCode() == KeyCode.ENTER) {
                SoundManager.playGameStartSound();
                gameHasStarted = true;
                gameManager.startGame();
                return;
            }

            if (gameOver) {
                if (e.getCode() == KeyCode.ENTER) {
                    SoundManager.playGameStartSound();
                    gameManager.resetGame();
                    gameOver = false;
                } else if (e.getCode() == KeyCode.ESCAPE) {
                    System.exit(0);
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            gameManager.handleKeyRelease(e.getCode());
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
