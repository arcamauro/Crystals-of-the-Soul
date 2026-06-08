package io.github.crystals_of_the_soul.model;

public enum EndingType {
    HERO(
        "Redemption",
        "You have atoned for your sins and those of the other exiles. " +
        "The dungeon opens before you and your village welcomes you as a hero. " +
        "The freed souls follow you toward the light."
    ),
    CITIZEN(
        "Return",
        "You have overcome the dungeon with wisdom. " +
        "You return to your village, not as a hero, but as a common man. " +
        "No one will remember your name, but you know what you have done."
    ),
    WANDERER(
        "Exile",
        "You have left too many shadows in the dungeon. " +
        "The village gates remain closed to you. " +
        "You will wander through the dungeon's floors for eternity, neither alive nor dead."
    ),
    GUARDIAN(
        "The New Guardian",
        "You have become what you feared the most. " +
        "The dungeon has consumed you and now you are its guardian. " +
        "No one will leave as long as you are here."
    ),
    CHOICE(
        "The Crossroads",
        "Your path has been balanced, as many lives broken as spared. " +
        "The dungeon offers you a choice: return to the world that exiled you, " +
        "or remain as the guardian of these forgotten souls."
    );

    private final String title;
    private final String narrative;

    EndingType(String title, String narrative) {
        this.title = title;
        this.narrative = narrative;
    }

    public String getTitle() { return title; }
    public String getNarrative() { return narrative; }
}
