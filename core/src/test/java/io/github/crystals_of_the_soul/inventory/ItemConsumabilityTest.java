package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.item.ItemEffectFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemConsumabilityTest {

    @Test
    void potionIsConsumable() {
        assertTrue(ItemEffectFactory.isConsumable("Pozione"));
    }

    @Test
    void superPotionIsConsumable() {
        assertTrue(ItemEffectFactory.isConsumable("Super Pozione"));
    }

    @Test
    void elixirIsConsumable() {
        assertTrue(ItemEffectFactory.isConsumable("Elisir di Forza"));
    }

    @Test
    void swordIsNotConsumable() {
        assertFalse(ItemEffectFactory.isConsumable("Spada"));
    }

    @Test
    void armorIsNotConsumable() {
        assertFalse(ItemEffectFactory.isConsumable("Armatura"));
    }

    @Test
    void nullIsNotConsumable() {
        assertFalse(ItemEffectFactory.isConsumable(null));
    }
}
