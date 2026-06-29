package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica che CrystalDialogue restituisca messaggi coerenti con lo stato morale
 * del gioco, sia prima che dopo l'assegnazione del cristallo.
 */
class CrystalDialogueTest {

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

    // -------------------------------------------------------------------------
    // Pre-crystal messages (before assignCrystal is called)
    // -------------------------------------------------------------------------

    @Test
    void preMessageForAllKillsIsNotEmpty() {
        GameState state = stateWithMoral(3, 0);
        assertFalse(CrystalDialogue.getPreMessage(state).isBlank());
    }

    @Test
    void preMessageForAllSparesIsNotEmpty() {
        GameState state = stateWithMoral(0, 3);
        assertFalse(CrystalDialogue.getPreMessage(state).isBlank());
    }

    @Test
    void preMessageForMixedIsNotEmpty() {
        GameState state = stateWithMoral(2, 2);
        assertFalse(CrystalDialogue.getPreMessage(state).isBlank());
    }

    @Test
    void allKillsPreMessageDiffersFromAllSpares() {
        String kills = CrystalDialogue.getPreMessage(stateWithMoral(3, 0));
        String spares = CrystalDialogue.getPreMessage(stateWithMoral(0, 3));
        assertNotEquals(kills, spares);
    }

    @Test
    void allSparesPreMessageDiffersFromMixed() {
        String spares = CrystalDialogue.getPreMessage(stateWithMoral(0, 3));
        String mixed  = CrystalDialogue.getPreMessage(stateWithMoral(2, 2));
        assertNotEquals(spares, mixed);
    }

    @Test
    void allKillsPreMessageDiffersFromMixed() {
        String kills = CrystalDialogue.getPreMessage(stateWithMoral(3, 0));
        String mixed = CrystalDialogue.getPreMessage(stateWithMoral(2, 2));
        assertNotEquals(kills, mixed);
    }

    // -------------------------------------------------------------------------
    // Post-crystal messages (after assignCrystal is called)
    // -------------------------------------------------------------------------

    @Test
    void postMessageForRedIsNotEmpty() {
        GameState state = stateWithCrystal(3, 0); // all kills → RED
        assertFalse(CrystalDialogue.getPostMessage(state).isBlank());
    }

    @Test
    void postMessageForBlueIsNotEmpty() {
        GameState state = stateWithCrystal(0, 3); // all spares → BLUE
        assertFalse(CrystalDialogue.getPostMessage(state).isBlank());
    }

    @Test
    void postMessageForGreenIsNotEmpty() {
        GameState state = stateWithCrystal(2, 2); // mixed → GREEN
        assertFalse(CrystalDialogue.getPostMessage(state).isBlank());
    }

    @Test
    void postMessageForRedMentionsAssassin() {
        GameState state = stateWithCrystal(3, 0);
        assertTrue(CrystalDialogue.getPostMessage(state).contains("Assassin"));
    }

    @Test
    void postMessageForBlueMentionsProtector() {
        GameState state = stateWithCrystal(0, 3);
        assertTrue(CrystalDialogue.getPostMessage(state).contains("Protector"));
    }

    @Test
    void postMessageForGreenMentionsArcher() {
        GameState state = stateWithCrystal(2, 2);
        assertTrue(CrystalDialogue.getPostMessage(state).contains("Archer"));
    }

    @Test
    void postMessageForRedMentionsCrystalColor() {
        GameState state = stateWithCrystal(3, 0);
        String msg = CrystalDialogue.getPostMessage(state).toLowerCase();
        assertTrue(msg.contains("red") || msg.contains("crimson") || msg.contains("wrath"),
            "Expected a red crystal reference in: " + msg);
    }

    @Test
    void postMessageForBlueMentionsCrystalColor() {
        GameState state = stateWithCrystal(0, 3);
        String msg = CrystalDialogue.getPostMessage(state).toLowerCase();
        assertTrue(msg.contains("blue") || msg.contains("mercy"),
            "Expected a blue crystal reference in: " + msg);
    }

    @Test
    void postMessageForGreenMentionsCrystalColor() {
        GameState state = stateWithCrystal(2, 2);
        String msg = CrystalDialogue.getPostMessage(state).toLowerCase();
        assertTrue(msg.contains("green") || msg.contains("balance"),
            "Expected a green crystal reference in: " + msg);
    }

    @Test
    void postMessagesDifferByCrystalType() {
        String red   = CrystalDialogue.getPostMessage(stateWithCrystal(3, 0));
        String blue  = CrystalDialogue.getPostMessage(stateWithCrystal(0, 3));
        String green = CrystalDialogue.getPostMessage(stateWithCrystal(2, 2));
        assertNotEquals(red, blue);
        assertNotEquals(blue, green);
        assertNotEquals(red, green);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private GameState stateWithMoral(int kills, int spares) {
        GameState state = GameState.createNew("Test");
        state.killCount = kills;
        state.spareCount = spares;
        return state;
    }

    private GameState stateWithCrystal(int kills, int spares) {
        GameState state = stateWithMoral(kills, spares);
        state.assignCrystal();
        return state;
    }
}
