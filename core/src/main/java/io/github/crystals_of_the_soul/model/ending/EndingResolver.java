package io.github.crystals_of_the_soul.model.ending;

import io.github.crystals_of_the_soul.model.GameState;

public class EndingResolver {

    private EndingResolver() {}

    public static EndingStrategy resolve(GameState state) {
        if (!state.hasCrystal()) return null;

        switch (state.getCrystal()) {
            case RED:   return new GuardianEndingStrategy();
            case BLUE:  return new HeroEndingStrategy();
            case GREEN:
                if (state.killCount > state.spareCount) return new WandererEndingStrategy();
                if (state.spareCount > state.killCount) return new CitizenEndingStrategy();
                return new ChoiceEndingStrategy();
            default:    return null;
        }
    }
}
