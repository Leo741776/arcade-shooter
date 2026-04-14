package core;

public class GameState {

    public int life = 3;
    public int score = 0;
    public int highScore = 0;
    // Millisecond timestamp used to enforce an invincibility window after a hit
    public long timeSinceLastTookDamage = 0;
}