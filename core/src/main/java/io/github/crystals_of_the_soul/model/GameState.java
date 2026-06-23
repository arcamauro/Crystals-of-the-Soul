package io.github.crystals_of_the_soul.model;

public class GameState {

    // --- Floors ---
    public int currentFloor;

    // --- Moral counter ---
    public int killCount;
    public int spareCount;
    public int gold;

    // --- Crystal ---
    private CrystalType crystal;

    // --- Players ---
    private PlayerState player1;
    private Player2State player2;

    // --- Session ---
    public float playTime;
    public long savedAt;
    public String playerName;

    /**
     * Crea un nuovo stato di gioco con valori iniziali predefiniti.
     * @return
     */
    public static GameState createNew() {
        return createNew("Player");
    }

    /**
     * Crea un nuovo stato di gioco con il nome del giocatore specificato.
     * @param playerName nome scelto dal giocatore
     * @return
     */
    public static GameState createNew(String playerName) {
        GameState state = new GameState();
        state.currentFloor = 0;
        state.killCount = 0;
        state.spareCount = 0;
        state.gold = 20;
        state.crystal = null;
        state.player1 = new PlayerState();
        state.player2 = null;
        state.playTime = 0f;
        state.savedAt = 0L;
        state.playerName = (playerName != null && !playerName.trim().isEmpty()) ? playerName.trim() : "Player";
        com.badlogic.gdx.Gdx.app.log("GameState", "New game started with playerName: " + state.playerName);
        return state;
    }

    /**
     * Assegna un cristallo al giocatore in base ai valori di uccisioni e risparmi.
     */
    public void assignCrystal() {
        // Determine crystal
        if (killCount > 0 && spareCount == 0) {
            crystal = CrystalType.RED;
        } else if (killCount == 0 && spareCount > 0) {
            crystal = CrystalType.BLUE;
        } else {
            crystal = CrystalType.GREEN;
        }

        // --- Generazione del giocatore 2 in base al cristallo assegnato e modifiche alle azioni eseguibili dal giocatore 1 ---
        switch (crystal) {
            case RED:
                player2 = Player2State.create(Player2Class.ASSASSIN);
                player1.canTalk = false;
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

    /**
     * Determina il finale del gioco in base al cristallo assegnato e ai contatori morali.
     */
    public EndingType determineEnding() {
        if (!hasCrystal()) return null;

        switch (crystal) {
            case RED:
                return EndingType.GUARDIAN;
            case BLUE:
                return EndingType.HERO;
            case GREEN:
                if(killCount > spareCount) return EndingType.WANDERER;
                if(spareCount > killCount) return EndingType.CITIZEN;
                return EndingType.CHOICE; // killCount == spareCount
            default:
                return null;
        }
    }

    /**
     * Metodo di supporto che verifica se il cristallo è stato assegnato
     */
    public boolean hasCrystal() {
        return crystal != null;
    }

    /**
     * Metodo di supporto che verifica se il secondo giocatore è stato generato
     */
    public boolean hasPlayer2() {
        return player2 != null;
    }

    /**
     * Restituisce il tipo di cristallo assegnato in base alle scelte morali del giocatore.
     */
    public CrystalType getCrystal() {
        return crystal;
    }

    /**
     * Restituisce lo stato del primo giocatore, sempre presente.
     *
     */
    public PlayerState getPlayer1() {
        return player1;
    }

    /**
     * Restituisce lo stato del secondo giocatore, se presente. Può essere null se il cristallo non è ancora stato assegnato o se siamo al primo piano.
     * @return
     */
    public Player2State getPlayer2() {
        return player2;
    }
    /**
     * Restituisce il percorso della mappa corrispondente al piano
     * e al cristallo attivo. Usato da GameScreen per caricare la mappa corretta.
     */
    public String getCurrentMapPath() {
        switch (currentFloor) {
            case 0: return "maps/lvl0.tmx";
            case 1: return "maps/lvl1_0.tmx"; // section 0 by default
            case 2: return "maps/lvl2.tmx";
            case 3: return "maps/lvl3_" + crystalSuffix() + ".tmx";
            case 4: return "maps/lvl4_" + crystalSuffix() + ".tmx";
            case 5: return "maps/lvl5_" + crystalSuffix() + ".tmx";
            default: return "maps/lvl0.tmx";
        }
    }

    private String crystalSuffix() {
        if (!hasCrystal()) return "v"; // default to green if somehow missing
        switch (crystal) {
            case RED:   return "r";
            case GREEN: return "v";
            case BLUE:  return "b";
            default:    return "v";
        }
    }
}
