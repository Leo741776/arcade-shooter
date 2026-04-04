// Shared data container passed between managers so each system can read/write
// game state without needing direct references to one another.

package core;

public class GameState {

    public int life = 3;
    public int score = 0;
    public int highScore = 0;
    // Millisecond timestamp used to enforce an invincibility window after a hit
    public long timeSinceLastTookDamage = 0;
}