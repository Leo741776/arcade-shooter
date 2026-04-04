# 2D Arcade Shooter

This is a vertical arcade shoot 'em up game where the player sprite battles waves of spawning enemies.

## Quick Start

- Clone and open with your preferred IDE, making sure to add the JavaFX library to your project and set SDK to JDK 24.
- Configure VM options for the run configuration by adding the following to your VM options: `--module-path "\path\to\javafx-sdk-26\lib" --add-modules javafx.controls,javafx.fxml` making sure to replace `"\path\to\javafx-sdk-26\lib"` with your actual JavaFX lib directory.
- Finally, set the main class to source.core.Launcher and run.

## How It's Made

**Language:** Java  
**Toolkit:** JavaFX

- **Game Loop:** The core game loop has GameManager.java drive an AnimationTimer each frame - updating difficulty, handling input, spawning enemies/power-ups, moving projectiles, and invoking CollisionManager.java which handles collision detection.

- **Player:** PizzaSprite.java moves left/right with arrow keys at the bottom of the screens, firing projectiles upward automatically based on a cooldown (default 750ms). A 1-second invincibility window and flash animation triggers on hits. Spawn rate starts at 1700ms and decreases by 100ms every 15 seconds.

- **Enemies:** Enemy.java spawns from the top on a randomized zigzag PathTransition path, fired EnemyProjectile downward every 750ms, and self-removes when it exists the screen. Spawn rate starts at 1700ms and decreases by 100ms every 15 seconds.

- **Power-ups:** Three types spawn every 20 seconds, drifting down slowly: salt - faster fire rate for 15s (45% chance), pepper - triple-shot (two pizza clones) for 15s (45% chance)

- **Collisions:** CollisionUtils.java does padded bounding-box checks. Player projectiles hitting enemies yield +5 score and spawn a 300ms explosion. Enemy/projectile hits on the player cost 1 life.

- **UI & Audio:** UIManager.java handles the start screen (pulsing prompt, logo), in-game HUD (life icons, score), and game-over overlay. SoundManager.java uses preloaded AudioClip objects (25% volume) for all effects including blaster, explosion, power-up, and game-over sounds.

- **State Flow:** GameState.java holds lives (start: 3), score, and high score. The game cycles through: Menu (press Enter) → Playing → Game Over (press Enter to restart, Esc to quit).

## Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/0c53d460-6b98-41db-8b2c-ad4ca6223624" width="30%"/>
  <img src="https://github.com/user-attachments/assets/7a84f2ff-b769-4944-b11d-978c50f03582" width="30%"/>
  <img src="https://github.com/user-attachments/assets/5fa92cac-5c53-45c6-bf68-660ed343c6d6" width="30%"/>
</p>
