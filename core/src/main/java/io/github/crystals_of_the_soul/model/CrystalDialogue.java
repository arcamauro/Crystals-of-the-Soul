package io.github.crystals_of_the_soul.model;

public class CrystalDialogue {

    public static String getPreMessage(GameState state) {
        if (state.killCount > 0 && state.spareCount == 0) {
            return "Every enemy you faced has fallen by your hand. The weight of those battles follows you as you descend deeper into the crystal caves...\n\nSomething stirs in the darkness ahead.";
        } else if (state.killCount == 0 && state.spareCount > 0) {
            return "You have shown mercy to every soul you encountered. Their gratitude lingers like warmth as you venture deeper into the crystal caves...\n\nSomething gentle stirs in the light ahead.";
        } else {
            return "Your path has been one of both mercy and conflict. The duality of your choices — to spare and to strike — has left its mark on this world...\n\nSomething stirs in the cave ahead, drawn by the balance within you.";
        }
    }

    public static String getPostMessage(GameState state) {
        switch (state.getCrystal()) {
            case RED:
                return "The Crystal of Wrath solidifies before you — deep crimson, pulsing with the echo of every battle.\n\nFrom its glow steps a figure cloaked in shadow: an Assassin. They are your companion now.\n\nBut their presence has changed you. You can no longer bring yourself to talk or spare your enemies.";
            case BLUE:
                return "The Crystal of Mercy shimmers before you — clear blue, carrying the calm of every life you chose to spare.\n\nFrom its glow emerges a steadfast guardian: a Protector. They are your companion now.\n\nBut their presence has changed you. Bound by their vow of peace, you can no longer raise your hand to attack.";
            case GREEN:
                return "The Crystal of Balance glows before you — verdant green, alive with the harmony of your mixed choices.\n\nFrom its glow appears a nimble figure with bow in hand: an Archer. They are your companion now.\n\nAll paths remain open to you.";
            default:
                return "";
        }
    }
}
