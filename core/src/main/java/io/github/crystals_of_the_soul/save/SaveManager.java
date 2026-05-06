package io.github.crystals_of_the_soul.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import io.github.crystals_of_the_soul.states.GameState;
import io.github.crystals_of_the_soul.states.PlayerState;

public class SaveManager {

    private static final String AUTO_SAVE_PATH = "saves/autosave.json";
    private static final String MANUAL_SAVE_PATH = "saves/manualsave.json";

    private static final Json json = new Json();
    static {
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        json.addClassTag("PlayerState", PlayerState.class);
    }

    // --- Auto Save (#7) ---
    public static void autoSave(GameState state) {
        state.savedAt = System.currentTimeMillis();
        save(state, AUTO_SAVE_PATH);
        Gdx.app.log("SaveManager", "Auto saved at floor " + state.currentFloor);
    }

    // --- Manual Save (#8) ---
    public static void manualSave(GameState state) {
        state.savedAt = System.currentTimeMillis();
        save(state, MANUAL_SAVE_PATH);
        Gdx.app.log("SaveManager", "Manual saved at floor " + state.currentFloor);
    }

    // --- Load most recent ---
    public static GameState loadMostRecent() {
        GameState auto = load(AUTO_SAVE_PATH);
        GameState manual = load(MANUAL_SAVE_PATH);

        if (auto == null) return manual;
        if (manual == null) return auto;

        return auto.savedAt >= manual.savedAt ? auto : manual;
    }

    // --- Existence checks ---
    public static boolean anySaveExists() {
        return autoSaveExists() || manualSaveExists();
    }

    public static boolean autoSaveExists() {
        return Gdx.files.local(AUTO_SAVE_PATH).exists();
    }

    public static boolean manualSaveExists() {
        return Gdx.files.local(MANUAL_SAVE_PATH).exists();
    }

    // --- Internal helpers ---
    private static void save(GameState state, String path) {
        FileHandle file = Gdx.files.local(path);
        file.writeString(json.toJson(state), false);
    }

    private static GameState load(String path) {
        FileHandle file = Gdx.files.local(path);
        if (!file.exists()) return null;
        return json.fromJson(GameState.class, file);
    }
}