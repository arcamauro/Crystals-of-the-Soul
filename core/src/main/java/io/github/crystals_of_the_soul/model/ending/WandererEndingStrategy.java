package io.github.crystals_of_the_soul.model.ending;

public class WandererEndingStrategy implements EndingStrategy {

    @Override
    public String getTitle() { return "Exile"; }

    @Override
    public String getNarrative() {
        return "You have left too many shadows in the dungeon. " +
               "The village gates remain closed to you. " +
               "You will wander through the dungeon's floors for eternity, neither alive nor dead.";
    }

    @Override
    public boolean isInteractive() { return false; }
}
