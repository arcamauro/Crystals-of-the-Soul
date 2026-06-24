package io.github.crystals_of_the_soul.model.ending;

public class ChoiceEndingStrategy implements EndingStrategy {

    @Override
    public String getTitle() { return "The Crossroads"; }

    @Override
    public String getNarrative() {
        return "Your path has been balanced, as many lives broken as spared. " +
               "The dungeon offers you a choice: return to the world that exiled you, " +
               "or remain as the guardian of these forgotten souls.";
    }

    @Override
    public boolean isInteractive() { return true; }
}
