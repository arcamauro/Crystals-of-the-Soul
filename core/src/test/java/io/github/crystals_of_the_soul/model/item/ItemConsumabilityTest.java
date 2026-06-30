package io.github.crystals_of_the_soul.model.item;

import io.github.crystals_of_the_soul.model.item.ItemEffectFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemConsumabilityTest {

    @Test
    void potionIsConsumable() {
        assertTrue(ItemEffectFactory.isConsumable("Potion"));
    }

    @Test
    void superPotionIsConsumable() {
        assertTrue(ItemEffectFactory.isConsumable("Super Potion"));
    }

    @Test
    void elixirIsConsumable() {
        assertTrue(ItemEffectFactory.isConsumable("Elixir"));
    }

    @Test
    void swordIsNotConsumable() {
        assertFalse(ItemEffectFactory.isConsumable("Sword"));
    }

    @Test
    void armorIsNotConsumable() {
        assertFalse(ItemEffectFactory.isConsumable("Armor"));
    }

    @Test
    void nullIsNotConsumable() {
        assertFalse(ItemEffectFactory.isConsumable(null));
    }

    @Test
    void caseInsensitiveAndLocalizedItemsAreConsumable() {
        assertTrue(ItemEffectFactory.isConsumable("potion"));
        assertTrue(ItemEffectFactory.isConsumable("pozione"));
        assertTrue(ItemEffectFactory.isConsumable("elisir"));
        assertTrue(ItemEffectFactory.isConsumable("elixir"));
        assertTrue(ItemEffectFactory.isConsumable("super potion"));
        assertTrue(ItemEffectFactory.isConsumable("POTION"));
        assertTrue(ItemEffectFactory.isConsumable("POZIONE"));
    }
}
