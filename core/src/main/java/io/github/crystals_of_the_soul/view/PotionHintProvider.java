package io.github.crystals_of_the_soul.view;

import io.github.crystals_of_the_soul.model.Inventory;
import io.github.crystals_of_the_soul.model.entity.Item;

public class PotionHintProvider {

    public static String getHintText(Inventory inventory) {
        Item potion = inventory.findFirstPotion();
        if (potion == null) return null;
        return "[H] " + potion.getName();
    }
}
