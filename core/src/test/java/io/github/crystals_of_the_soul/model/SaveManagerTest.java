package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import io.github.crystals_of_the_soul.model.GameState;
import io.github.crystals_of_the_soul.model.SaveManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test del sistema di salvataggio (automatico e manuale).
 *
 * SaveManager usa Gdx.files e Gdx.app, quindi serve un backend libGDX attivo:
 * qui viene avviato il backend headless (senza finestra/grafica) una sola volta
 * per tutta la classe. I file di salvataggio vengono ripuliti prima e dopo ogni
 * test per garantire l'isolamento.
 */
class SaveManagerTest {

    private static final String AUTO_SAVE_PATH = "saves/autosave.json";
    private static final String MANUAL_SAVE_PATH = "saves/manualsave.json";

    private static HeadlessApplication app;

    @BeforeAll
    static void initLibGdx() {
        if (Gdx.app == null) {
            app = new HeadlessApplication(new ApplicationAdapter() {}, new HeadlessApplicationConfiguration());
        }
    }

    @AfterAll
    static void disposeLibGdx() {
        if (app != null) {
            app.exit();
            app = null;
        }
    }

    @BeforeEach
    void cleanBefore() {
        deleteAllSaves();
    }

    @AfterEach
    void cleanAfter() {
        deleteAllSaves();
    }

    private void deleteAllSaves() {
        for (String path : new String[]{AUTO_SAVE_PATH, MANUAL_SAVE_PATH}) {
            Gdx.files.local(path).delete();
            Gdx.files.local(path + ".hash").delete();
        }
    }

    // --- Pattern Singleton ---

    @Test
    void getInstanceAlwaysReturnsTheSameInstance() {
        assertSame(SaveManager.getInstance(), SaveManager.getInstance());
    }

    @Test
    void getInstanceNeverReturnsNull() {
        assertNotNull(SaveManager.getInstance());
    }

    // --- Esistenza dei salvataggi ---

    @Test
    void noSaveExistsInitially() {
        SaveManager manager = SaveManager.getInstance();

        assertFalse(manager.anySaveExists());
        assertFalse(manager.autoSaveExists());
        assertFalse(manager.manualSaveExists());
    }

    @Test
    void manualSaveCreatesOnlyTheManualFile() {
        SaveManager manager = SaveManager.getInstance();

        manager.manualSave(GameState.createNew("Hero"));

        assertTrue(manager.manualSaveExists());
        assertFalse(manager.autoSaveExists());
    }

    @Test
    void autoSaveCreatesOnlyTheAutoFile() {
        SaveManager manager = SaveManager.getInstance();

        manager.autoSave(GameState.createNew("Hero"));

        assertTrue(manager.autoSaveExists());
        assertFalse(manager.manualSaveExists());
    }

    // --- Round-trip: salvataggio + caricamento ---

    @Test
    void manualSaveAndLoadPreservesState() {
        SaveManager manager = SaveManager.getInstance();

        GameState state = GameState.createNew("Arcangelo");
        state.currentFloor = 3;
        state.killCount = 4;
        state.spareCount = 1;
        state.playTime = 123.5f;

        manager.manualSave(state);
        GameState loaded = manager.loadMostRecent();

        assertEquals(3, loaded.currentFloor);
        assertEquals(4, loaded.killCount);
        assertEquals(1, loaded.spareCount);
        assertEquals("Arcangelo", loaded.playerName);
        assertEquals(123.5f, loaded.playTime, 0.001f);
        assertNotNull(loaded.getPlayer1());
    }

    // --- loadMostRecent: sceglie il salvataggio più recente ---

    @Test
    void loadMostRecentReturnsTheNewerSave() throws InterruptedException {
        SaveManager manager = SaveManager.getInstance();

        GameState older = GameState.createNew("Old");
        older.currentFloor = 1;
        manager.manualSave(older);

        // Garantisce un timestamp (savedAt) successivo per il secondo salvataggio.
        Thread.sleep(5);

        GameState newer = GameState.createNew("New");
        newer.currentFloor = 2;
        manager.autoSave(newer);

        GameState loaded = manager.loadMostRecent();

        assertEquals(2, loaded.currentFloor);
        assertEquals("New", loaded.playerName);
    }

    @Test
    void loadMostRecentWithNoSavesReturnsANewGame() {
        GameState loaded = SaveManager.getInstance().loadMostRecent();

        assertNotNull(loaded);
        assertEquals(0, loaded.currentFloor);
        assertNotNull(loaded.getPlayer1());
    }

    // --- Integrità: un file manomesso viene scartato ---

    @Test
    void tamperedSaveFailsIntegrityCheck() {
        SaveManager manager = SaveManager.getInstance();

        manager.manualSave(GameState.createNew("Victim"));

        // Modifica il contenuto del salvataggio senza aggiornare l'hash:
        // l'integrità deve fallire e il salvataggio manomesso deve essere scartato.
        Gdx.files.local(MANUAL_SAVE_PATH).writeString("{\"currentFloor\":999}", false);

        // Nessun autosave valido => si riparte da un nuovo stato (piano 0), non dal valore manomesso.
        GameState loaded = manager.loadMostRecent();

        assertNotNull(loaded);
        assertEquals(0, loaded.currentFloor);
    }

    @Test
    void tamperedManualSaveFallsBackToValidAutoSave() {
        SaveManager manager = SaveManager.getInstance();

        GameState auto = GameState.createNew("AutoHero");
        auto.currentFloor = 2;
        manager.autoSave(auto);

        manager.manualSave(GameState.createNew("ManualHero"));

        // Corrompe solo il salvataggio manuale.
        Gdx.files.local(MANUAL_SAVE_PATH).writeString("not even valid json", false);

        GameState loaded = manager.loadMostRecent();

        // Deve ripiegare sull'autosave valido.
        assertEquals(2, loaded.currentFloor);
        assertEquals("AutoHero", loaded.playerName);
    }
}
