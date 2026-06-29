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
 * Verifica che GameState.predictCrystal() restituisca il tipo corretto
 * in base ai contatori morali, senza effetti collaterali (player2, abilità player1).
 */
class CrystalPredictionTest {

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
    // Correct crystal prediction from moral counts
    // -------------------------------------------------------------------------

    @Test
    void allKillsPredictRed() {
        assertEquals(CrystalType.RED, stateWithMoral(5, 0).predictCrystal());
    }

    @Test
    void allSparesPredictBlue() {
        assertEquals(CrystalType.BLUE, stateWithMoral(0, 5).predictCrystal());
    }

    @Test
    void mixedPredictGreen() {
        assertEquals(CrystalType.GREEN, stateWithMoral(2, 3).predictCrystal());
    }

    @Test
    void noEncountersPredictGreen() {
        assertEquals(CrystalType.GREEN, stateWithMoral(0, 0).predictCrystal());
    }

    @Test
    void oneKillOneSparePredictGreen() {
        assertEquals(CrystalType.GREEN, stateWithMoral(1, 1).predictCrystal());
    }

    @Test
    void multipleKillsOneSparePredictGreen() {
        assertEquals(CrystalType.GREEN, stateWithMoral(4, 1).predictCrystal());
    }

    @Test
    void oneKillMultipleSparesPredictGreen() {
        assertEquals(CrystalType.GREEN, stateWithMoral(1, 4).predictCrystal());
    }

    // -------------------------------------------------------------------------
    // predictCrystal is consistent with assignCrystal
    // -------------------------------------------------------------------------

    @Test
    void predictMatchesAssignForRed() {
        GameState state = stateWithMoral(3, 0);
        CrystalType predicted = state.predictCrystal();
        state.assignCrystal();
        assertEquals(predicted, state.getCrystal());
    }

    @Test
    void predictMatchesAssignForBlue() {
        GameState state = stateWithMoral(0, 3);
        CrystalType predicted = state.predictCrystal();
        state.assignCrystal();
        assertEquals(predicted, state.getCrystal());
    }

    @Test
    void predictMatchesAssignForGreen() {
        GameState state = stateWithMoral(2, 2);
        CrystalType predicted = state.predictCrystal();
        state.assignCrystal();
        assertEquals(predicted, state.getCrystal());
    }

    // -------------------------------------------------------------------------
    // No side effects: crystal and player2 must stay null after prediction
    // -------------------------------------------------------------------------

    @Test
    void predictCrystalHasNoCrystalSideEffect() {
        GameState state = stateWithMoral(3, 0);
        state.predictCrystal();
        assertFalse(state.hasCrystal());
    }

    @Test
    void predictCrystalHasNoPlayer2SideEffect() {
        GameState state = stateWithMoral(3, 0);
        state.predictCrystal();
        assertNull(state.getPlayer2());
    }

    @Test
    void predictCrystalDoesNotAlterPlayer1Abilities() {
        GameState state = stateWithMoral(3, 0);
        boolean canAttackBefore = state.getPlayer1().canAttack;
        boolean canTalkBefore   = state.getPlayer1().canTalk;
        boolean canSpareBefore  = state.getPlayer1().canSpare;
        state.predictCrystal();
        assertEquals(canAttackBefore, state.getPlayer1().canAttack);
        assertEquals(canTalkBefore,   state.getPlayer1().canTalk);
        assertEquals(canSpareBefore,  state.getPlayer1().canSpare);
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
}
