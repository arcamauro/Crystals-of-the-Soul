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
 * Test sulla gestione del nome del giocatore in {@link GameState#createNew(String)}.
 *
 * GameState.createNew usa Gdx.app per il logging, quindi serve un backend libGDX
 * attivo: qui viene avviato il backend headless (senza finestra/grafica) una sola
 * volta per tutta la classe.
 *
 * Regole attese sul nome:
 *  - un nome valido viene conservato;
 *  - gli spazi iniziali/finali vengono rimossi (trim);
 *  - nome null, vuoto o composto solo da spazi ricade sul default "Player".
 */
class PlayerNameTest {

    private static final String DEFAULT_NAME = "Player";

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

    // --- Nome valido ---

    @Test
    void validNameIsKept() {
        GameState state = GameState.createNew("Arcangelo");

        assertEquals("Arcangelo", state.playerName);
    }

    @Test
    void nameWithInnerSpacesIsKept() {
        GameState state = GameState.createNew("Giovanni Romano");

        assertEquals("Giovanni Romano", state.playerName);
    }

    // --- Trim degli spazi ---

    @Test
    void leadingAndTrailingSpacesAreTrimmed() {
        GameState state = GameState.createNew("   Claudio   ");

        assertEquals("Claudio", state.playerName);
    }

    // --- Ricadute sul default "Player" ---

    @Test
    void nullNameFallsBackToDefault() {
        GameState state = GameState.createNew((String) null);

        assertEquals(DEFAULT_NAME, state.playerName);
    }

    @Test
    void emptyNameFallsBackToDefault() {
        GameState state = GameState.createNew("");

        assertEquals(DEFAULT_NAME, state.playerName);
    }

    @Test
    void blankNameFallsBackToDefault() {
        GameState state = GameState.createNew("     ");

        assertEquals(DEFAULT_NAME, state.playerName);
    }

    @Test
    void noArgFactoryUsesTheDefaultName() {
        GameState state = GameState.createNew();

        assertEquals(DEFAULT_NAME, state.playerName);
    }
}
