package io.github.crystals_of_the_soul.model.ending;

public class CitizenEndingStrategy implements EndingStrategy {

    @Override
    public String getTitle() { return "Return"; }

    @Override
    public String getNarrative() {
        return "You have overcome the dungeon with wisdom. " +
               "You return to your village, not as a hero, but as a common man. " +
               "No one will remember your name, but you know what you have done.";
    }

    @Override
    public boolean isInteractive() { return false; }
}
