package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Verifica il contratto del Factory Method GameState.createNew():
 * ogni invocazione deve restituire un'istanza distinta con tutti i campi
 * inizializzati ai valori predefiniti corretti.
 */
class GameStateFactoryTest {

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

    // --- Il factory non restituisce mai null ---

    @Test
    void createNewReturnsNonNull() {
        assertNotNull(GameState.createNew("Hero"));
    }

    @Test
    void createNewNoArgReturnsNonNull() {
        assertNotNull(GameState.createNew());
    }

    // --- Ogni chiamata produce un'istanza distinta ---

    @Test
    void twoCallsProduceDifferentInstances() {
        GameState a = GameState.createNew("A");
        GameState b = GameState.createNew("B");
        assertNotSame(a, b);
    }

    // --- Valori iniziali del modello ---

    @Test
    void initialFloorIsZero() {
        assertEquals(0, GameState.createNew("Hero").currentFloor);
    }

    @Test
    void initialKillCountIsZero() {
        assertEquals(0, GameState.createNew("Hero").killCount);
    }

    @Test
    void initialSpareCountIsZero() {
        assertEquals(0, GameState.createNew("Hero").spareCount);
    }

    @Test
    void initialPlayTimeIsZero() {
        assertEquals(0f, GameState.createNew("Hero").playTime, 0.0001f);
    }

    @Test
    void initialSavedAtIsZero() {
        assertEquals(0L, GameState.createNew("Hero").savedAt);
    }

    // --- Stato dei giocatori ---

    @Test
    void player1IsInitialized() {
        assertNotNull(GameState.createNew("Hero").getPlayer1());
    }

    @Test
    void player2IsNullBeforeCrystalAssignment() {
        assertNull(GameState.createNew("Hero").getPlayer2());
    }

    // --- Cristallo non ancora assegnato ---

    @Test
    void noCrystalBeforeAssignment() {
        assertFalse(GameState.createNew("Hero").hasCrystal());
    }

    // --- La variante senza argomenti delega alla variante con stringa ---

    @Test
    void noArgOverloadDelegatesToStringOverload() {
        GameState fromNoArg = GameState.createNew();
        GameState fromDefault = GameState.createNew("Player");

        assertEquals(fromDefault.playerName,   fromNoArg.playerName);
        assertEquals(fromDefault.currentFloor, fromNoArg.currentFloor);
        assertEquals(fromDefault.killCount,    fromNoArg.killCount);
        assertEquals(fromDefault.spareCount,   fromNoArg.spareCount);
        assertEquals(fromDefault.playTime,     fromNoArg.playTime, 0.0001f);
        assertEquals(fromDefault.savedAt,      fromNoArg.savedAt);
        assertEquals(fromDefault.hasCrystal(), fromNoArg.hasCrystal());
    }
}
