package io.github.crystals_of_the_soul.model.ending;

public class HeroEndingStrategy implements EndingStrategy {

    @Override
    public String getTitle() { return "Redemption"; }

    @Override
    public String getNarrative() {
        return "You have atoned for your sins and those of the other exiles. " +
               "The dungeon opens before you and your village welcomes you as a hero. " +
               "The freed souls follow you toward the light.";
    }

    @Override
    public boolean isInteractive() { return false; }
}
