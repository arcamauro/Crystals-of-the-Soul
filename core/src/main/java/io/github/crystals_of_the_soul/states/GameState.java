package io.github.crystals_of_the_soul.states;

public class GameState {

    // --- Floors ---
    public int currentFloor;

    // --- Moral counter ---
    public int killCount;
    public int spareCount;

    // --- Crystal ---
    public CrystalType crystal;

    // --- Players ---
    public PlayerState player1;
    public PlayerState player2;

    // --- Session ---
    public float playTime;
    public long savedAt;

    public static GameState createNew() {
        GameState state = new GameState();
        state.currentFloor = 0;
        state.killCount = 0;
        state.spareCount = 0;
        state.crystal = null;
        state.player1 = new PlayerState();
        state.player2 = null;
        state.playTime = 0f;
        state.savedAt = 0L;
        return state;
    }

    public void assignCrystal() {
        if (killCount > 0 && spareCount == 0) {
            crystal = CrystalType.RED;
        } else if (killCount == 0 && spareCount > 0) {
            crystal = CrystalType.BLUE;
        } else {
            crystal = CrystalType.GREEN;
        }
    }
}