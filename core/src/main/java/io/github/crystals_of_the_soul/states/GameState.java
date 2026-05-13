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
    public Player2State player2; // null until floor 2

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
        // Determine crystal
        if (killCount > 0 && spareCount == 0) {
            crystal = CrystalType.RED;
        } else if (killCount == 0 && spareCount > 0) {
            crystal = CrystalType.BLUE;
        } else {
            crystal = CrystalType.GREEN;
        }

        // Spawn Player 2 with matching class
        switch (crystal) {
            case RED:
                player2 = Player2State.create(Player2Class.ASSASSIN);
                player1.canSpare = false;
                break;
            case GREEN:
                player2 = Player2State.create(Player2Class.ARCHER);
                break;
            case BLUE:
                player2 = Player2State.create(Player2Class.PROTECTOR);
                player1.canAttack = false;
                break;
        }

        com.badlogic.gdx.Gdx.app.log("GameState", "Crystal assigned: " + crystal);
        com.badlogic.gdx.Gdx.app.log("GameState", "Player 2 spawned as: " + player2.playerClass);
        com.badlogic.gdx.Gdx.app.log("GameState", "Player 1 canAttack: " + player1.canAttack + " canSpare: " + player1.canSpare);
    }
}