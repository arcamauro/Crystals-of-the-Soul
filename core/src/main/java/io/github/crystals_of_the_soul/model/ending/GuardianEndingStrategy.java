package io.github.crystals_of_the_soul.model.ending;

public class GuardianEndingStrategy implements EndingStrategy {

    @Override
    public String getTitle() { return "The New Guardian"; }

    @Override
    public String getNarrative() {
        return "You have become what you feared the most. " +
               "The dungeon has consumed you and now you are its guardian. " +
               "No one will leave as long as you are here.";
    }

    @Override
    public boolean isInteractive() { return false; }
}
