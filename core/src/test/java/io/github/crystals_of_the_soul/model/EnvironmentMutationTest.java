package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifica che getCurrentMapPath() restituisca la mappa corretta
 * per ogni combinazione piano + cristallo.
 *
 * Piani 0-2: mappa fissa, indipendente dal cristallo.
 * Piani 3-5: mappa con suffisso cristallo (r/v/b).
 */
class EnvironmentMutationTest {

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

    // --- Piani fissi (0-2): nessuna variante per cristallo ---

    @Test
    void floor0ReturnsFixedMap() {
        GameState state = GameState.createNew("Test");
        state.currentFloor = 0;
        assertEquals("maps/lvl0.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor1ReturnsFixedMap() {
        GameState state = GameState.createNew("Test");
        state.currentFloor = 1;
        assertEquals("maps/lvl1_0.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor2ReturnsFixedMap() {
        GameState state = GameState.createNew("Test");
        state.currentFloor = 2;
        assertEquals("maps/lvl2.tmx", state.getCurrentMapPath());
    }

    // --- Piano 3: mappa varia con il cristallo ---

    @Test
    void floor3RedCrystalReturnsRedMap() {
        GameState state = stateWithCrystal(3, 5, 0); // kills > 0, spares == 0 → RED
        assertEquals("maps/lvl3_r.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor3GreenCrystalReturnsGreenMap() {
        GameState state = stateWithCrystal(3, 1, 1); // both > 0 → GREEN
        assertEquals("maps/lvl3_v.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor3BlueCrystalReturnsBlueMap() {
        GameState state = stateWithCrystal(3, 0, 5); // kills == 0, spares > 0 → BLUE
        assertEquals("maps/lvl3_b.tmx", state.getCurrentMapPath());
    }

    // --- Piano 4: mappa varia con il cristallo ---

    @Test
    void floor4RedCrystalReturnsRedMap() {
        GameState state = stateWithCrystal(4, 5, 0);
        assertEquals("maps/lvl4_r.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor4GreenCrystalReturnsGreenMap() {
        GameState state = stateWithCrystal(4, 1, 1);
        assertEquals("maps/lvl4_v.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor4BlueCrystalReturnsBlueMap() {
        GameState state = stateWithCrystal(4, 0, 5);
        assertEquals("maps/lvl4_b.tmx", state.getCurrentMapPath());
    }

    // --- Piano 5: mappa varia con il cristallo ---

    @Test
    void floor5RedCrystalReturnsRedMap() {
        GameState state = stateWithCrystal(5, 5, 0);
        assertEquals("maps/lvl5_r.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor5GreenCrystalReturnsGreenMap() {
        GameState state = stateWithCrystal(5, 1, 1);
        assertEquals("maps/lvl5_v.tmx", state.getCurrentMapPath());
    }

    @Test
    void floor5BlueCrystalReturnsBlueMap() {
        GameState state = stateWithCrystal(5, 0, 5);
        assertEquals("maps/lvl5_b.tmx", state.getCurrentMapPath());
    }

    // -------------------------------------------------------------------------

    private GameState stateWithCrystal(int floor, int kills, int spares) {
        GameState state = GameState.createNew("Test");
        state.currentFloor = floor;
        state.killCount = kills;
        state.spareCount = spares;
        state.assignCrystal();
        return state;
    }
}
