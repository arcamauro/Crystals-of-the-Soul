package io.github.crystals_of_the_soul.model.ending;

import io.github.crystals_of_the_soul.model.ending.CitizenEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.ChoiceEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.EndingStrategy;
import io.github.crystals_of_the_soul.model.ending.GuardianEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.HeroEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.WandererEndingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica che ogni strategia di finale restituisca titolo e narrativa non nulli
 * e che solo ChoiceEndingStrategy sia marcata come interattiva.
 */
class EndingStrategyTest {

    // --- HeroEndingStrategy ---

    @Test
    void heroEnding_titleIsNotNull() {
        assertNotNull(new HeroEndingStrategy().getTitle());
    }

    @Test
    void heroEnding_narrativeIsNotNull() {
        assertNotNull(new HeroEndingStrategy().getNarrative());
    }

    @Test
    void heroEnding_isNotInteractive() {
        assertFalse(new HeroEndingStrategy().isInteractive());
    }

    // --- CitizenEndingStrategy ---

    @Test
    void citizenEnding_titleIsNotNull() {
        assertNotNull(new CitizenEndingStrategy().getTitle());
    }

    @Test
    void citizenEnding_narrativeIsNotNull() {
        assertNotNull(new CitizenEndingStrategy().getNarrative());
    }

    @Test
    void citizenEnding_isNotInteractive() {
        assertFalse(new CitizenEndingStrategy().isInteractive());
    }

    // --- WandererEndingStrategy ---

    @Test
    void wandererEnding_titleIsNotNull() {
        assertNotNull(new WandererEndingStrategy().getTitle());
    }

    @Test
    void wandererEnding_narrativeIsNotNull() {
        assertNotNull(new WandererEndingStrategy().getNarrative());
    }

    @Test
    void wandererEnding_isNotInteractive() {
        assertFalse(new WandererEndingStrategy().isInteractive());
    }

    // --- GuardianEndingStrategy ---

    @Test
    void guardianEnding_titleIsNotNull() {
        assertNotNull(new GuardianEndingStrategy().getTitle());
    }

    @Test
    void guardianEnding_narrativeIsNotNull() {
        assertNotNull(new GuardianEndingStrategy().getNarrative());
    }

    @Test
    void guardianEnding_isNotInteractive() {
        assertFalse(new GuardianEndingStrategy().isInteractive());
    }

    // --- ChoiceEndingStrategy ---

    @Test
    void choiceEnding_titleIsNotNull() {
        assertNotNull(new ChoiceEndingStrategy().getTitle());
    }

    @Test
    void choiceEnding_narrativeIsNotNull() {
        assertNotNull(new ChoiceEndingStrategy().getNarrative());
    }

    @Test
    void choiceEnding_isInteractive() {
        assertTrue(new ChoiceEndingStrategy().isInteractive());
    }

    // --- Polymorphism contract: every concrete strategy satisfies the interface ---

    @Test
    void allStrategiesAreEndingStrategy() {
        EndingStrategy[] all = {
            new HeroEndingStrategy(),
            new CitizenEndingStrategy(),
            new WandererEndingStrategy(),
            new GuardianEndingStrategy(),
            new ChoiceEndingStrategy()
        };
        for (EndingStrategy s : all) {
            assertNotNull(s.getTitle());
            assertNotNull(s.getNarrative());
        }
    }
}
