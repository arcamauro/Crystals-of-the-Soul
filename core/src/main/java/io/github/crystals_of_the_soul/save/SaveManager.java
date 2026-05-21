package io.github.crystals_of_the_soul.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import io.github.crystals_of_the_soul.states.GameState;
import io.github.crystals_of_the_soul.states.PlayerState;

/**
 *Classe che gestisce il salvataggio e il caricamento dello stato di gioco, con supporto per salvataggi automatici e manuali.
 * Implementa anche la validazione dei dati per prevenire errori di caricamento da file corrotti o modificati manualmente.
 * Utilizza JSON per la serializzazione, con una struttura semplice e facilmente estendibile per future aggiunte allo stato di gioco.
 * Fornisce metodi per verificare l'esistenza dei salvataggi e per caricare lo stato più recente tra autosave e manual save.
 */
public class SaveManager {

    private static SaveManager instance;

    private static final String AUTO_SAVE_PATH = "saves/autosave.json";
    private static final String MANUAL_SAVE_PATH = "saves/manualsave.json";

    private final Json json;

    private SaveManager() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        json.addClassTag("PlayerState", PlayerState.class);
    }

    /**
     * Restituisce l'istanza unica di SaveManager.
     * La crea al primo accesso.
     */
    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    /**
     * Salvataggio automatico — chiamato ad ogni cambio di piano.
     */
    public void autoSave(GameState state) {
        state.savedAt = System.currentTimeMillis();
        save(state, AUTO_SAVE_PATH);
        Gdx.app.log("SaveManager", "Auto saved at floor " + state.currentFloor);
    }

    /**
     * Salvataggio manuale — chiamato dal giocatore tramite il menu di pausa.
     */
    public void manualSave(GameState state) {
        state.savedAt = System.currentTimeMillis();
        save(state, MANUAL_SAVE_PATH);
        Gdx.app.log("SaveManager", "Manual saved at floor " + state.currentFloor);
    }

    /**
     * Carica il salvataggio più recente tra automatico e manuale.
     * Se entrambi sono corrotti, restituisce un nuovo GameState.
     */
    public GameState loadMostRecent() {
        GameState auto = load(AUTO_SAVE_PATH);
        GameState manual = load(MANUAL_SAVE_PATH);

        if (auto == null && manual == null) {
            Gdx.app.log("SaveManager", "No valid save found, starting new game");
            return GameState.createNew();
        }
        if (auto == null) {
            Gdx.app.log("SaveManager", "Autosave corrupt, falling back to manual save");
            return manual;
        }
        if (manual == null) {
            Gdx.app.log("SaveManager", "Manual save corrupt, falling back to autosave");
            return auto;
        }

        return auto.savedAt >= manual.savedAt ? auto : manual;
    }

    /**
     * Verifica se esiste almeno un salvataggio valido.
     */
    public boolean anySaveExists() {
        return autoSaveExists() || manualSaveExists();
    }

    /**
     * Verifica se esiste un salvataggio automatico.
     */
    public boolean autoSaveExists() {
        return Gdx.files.local(AUTO_SAVE_PATH).exists();
    }

    /**
     * Verifica se esiste un salvataggio manuale.
     */
    public boolean manualSaveExists() {
        return Gdx.files.local(MANUAL_SAVE_PATH).exists();
    }

    // --- Internal helpers ---

    private void save(GameState state, String path) {
        try {
            FileHandle file = Gdx.files.local(path);
            String serialized = json.toJson(state);
            file.writeString(serialized, false);
            Gdx.files.local(path + ".hash").writeString(
                SaveIntegrity.generateHash(serialized), false
            );
            Gdx.app.log("SaveManager", "Saved successfully to " + path);
        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Failed to save to " + path + ": " + e.getMessage());
        }
    }

    private GameState load(String path) {
        try {
            FileHandle file = Gdx.files.local(path);
            if (!file.exists()) return null;

            String content = file.readString();

            FileHandle hashFile = Gdx.files.local(path + ".hash");
            if (hashFile.exists()) {
                String expectedHash = hashFile.readString();
                if (!SaveIntegrity.verify(content, expectedHash)) {
                    Gdx.app.error("SaveManager", "Integrity check FAILED for " + path);
                    return null;
                }
                Gdx.app.log("SaveManager", "Integrity check passed for " + path);
            }

            GameState state = json.fromJson(GameState.class, content);

            if (!isValid(state)) {
                Gdx.app.error("SaveManager", "Validation failed for " + path);
                return null;
            }

            Gdx.app.log("SaveManager", "Loaded successfully from " + path);
            return state;

        } catch (Exception e) {
            Gdx.app.error("SaveManager", "Failed to load from " + path + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Metodo che verifica la validità del salvataggio caricato
     *
     */
    private boolean isValid(GameState state) {
        if (state == null) return false;
        if (state.currentFloor < 0 || state.currentFloor > 5) return false;
        if (state.killCount < 0 || state.spareCount < 0) return false;
        if (state.getPlayer1() == null) return false;
        if (state.playTime < 0) return false;
        return true;
    }
}
