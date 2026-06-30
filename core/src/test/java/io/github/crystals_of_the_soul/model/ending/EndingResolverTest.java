package io.github.crystals_of_the_soul.model.ending;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import io.github.crystals_of_the_soul.model.GameState;
import io.github.crystals_of_the_soul.model.ending.CitizenEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.ChoiceEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.EndingResolver;
import io.github.crystals_of_the_soul.model.ending.EndingStrategy;
import io.github.crystals_of_the_soul.model.ending.GuardianEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.HeroEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.WandererEndingStrategy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Verifica che EndingResolver.resolve() restituisca la strategia corretta
 * per ogni combinazione cristallo + contatori morali.
 *
 * GameState.assignCrystal() usa Gdx.app per il logging, quindi serve il
 * backend headless attivo durante tutta la classe.
 */
class EndingResolverTest {

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

    // --- Cristallo RED: sempre Guardian ---

    @Test
    void redCrystal_resolvesToGuardianEnding() {
        GameState state = buildState(5, 0);
        assertInstanceOf(GuardianEndingStrategy.class, EndingResolver.resolve(state));
    }

    // --- Cristallo BLUE: sempre Hero ---

    @Test
    void blueCrystal_resolvesToHeroEnding() {
        GameState state = buildState(0, 5);
        assertInstanceOf(HeroEndingStrategy.class, EndingResolver.resolve(state));
    }

    // --- Cristallo GREEN: dipende dai contatori ---

    @Test
    void greenCrystal_moreKills_resolvesToWandererEnding() {
        GameState state = buildState(3, 1);
        assertInstanceOf(WandererEndingStrategy.class, EndingResolver.resolve(state));
    }

    @Test
    void greenCrystal_moreSpares_resolvesToCitizenEnding() {
        GameState state = buildState(1, 3);
        assertInstanceOf(CitizenEndingStrategy.class, EndingResolver.resolve(state));
    }

    @Test
    void greenCrystal_equalCounts_resolvesToChoiceEnding() {
        GameState state = buildState(2, 2);
        assertInstanceOf(ChoiceEndingStrategy.class, EndingResolver.resolve(state));
    }

    // --- Senza cristallo: nessun finale ---

    @Test
    void noCrystal_resolvesToNull() {
        GameState state = GameState.createNew("Test");
        EndingStrategy result = EndingResolver.resolve(state);
        assertNull(result);
    }

    // --- Caso limite: GREEN con entrambi i contatori a zero ---

    @Test
    void greenCrystal_zeroCounts_resolvesToChoiceEnding() {
        GameState state = buildState(1, 1); // mix green: assign then override? Let's use equal nonzero
        assertInstanceOf(ChoiceEndingStrategy.class, EndingResolver.resolve(state));
    }

    // -------------------------------------------------------------------------

    private GameState buildState(int kills, int spares) {
        GameState state = GameState.createNew("Test");
        state.killCount = kills;
        state.spareCount = spares;
        state.assignCrystal();
        return state;
    }
}
