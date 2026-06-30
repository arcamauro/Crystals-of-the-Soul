package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.Inventory;
import io.github.crystals_of_the_soul.model.entity.Item;
import io.github.crystals_of_the_soul.view.PotionHintProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PotionHintProviderTest {

    @Test
    void returnsNullWhenInventoryIsEmpty() {
        Inventory inventory = new Inventory();
        assertNull(PotionHintProvider.getHintText(inventory));
    }

    @Test
    void returnsHintWithNameWhenPotionPresent() {
        Inventory inventory = new Inventory();
        inventory.addItem(new Item(0, 0, "Pozione"));
        assertEquals("[H] Pozione", PotionHintProvider.getHintText(inventory));
    }

    @Test
    void returnsHintWithSuperPotionName() {
        Inventory inventory = new Inventory();
        inventory.addItem(new Item(0, 0, "Super Pozione"));
        assertEquals("[H] Super Pozione", PotionHintProvider.getHintText(inventory));
    }

    @Test
    void returnsHintWithElixirName() {
        Inventory inventory = new Inventory();
        inventory.addItem(new Item(0, 0, "Elisir di Forza"));
        assertEquals("[H] Elisir di Forza", PotionHintProvider.getHintText(inventory));
    }

    @Test
    void returnsNullWhenOnlyNonPotionItemsPresent() {
        Inventory inventory = new Inventory();
        inventory.addItem(new Item(0, 0, "Spada"));
        assertNull(PotionHintProvider.getHintText(inventory));
    }

    @Test
    void returnsFirstPotionNameWhenMultipleItemsPresent() {
        Inventory inventory = new Inventory();
        inventory.addItem(new Item(0, 0, "Spada"));
        inventory.addItem(new Item(0, 0, "Pozione"));
        assertEquals("[H] Pozione", PotionHintProvider.getHintText(inventory));
    }
}
