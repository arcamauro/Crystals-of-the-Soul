package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.controller.interactions.UseItemInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;
import org.junit.jupiter.api.Test;
import io.github.crystals_of_the_soul.model.Inventory;
import static org.junit.jupiter.api.Assertions.*;
import io.github.crystals_of_the_soul.model.InventoryObserver;

class InventoryObserverTest {

    private static class FakeObserver implements InventoryObserver {
        boolean notified = false;

        @Override
        public void onItemUsed(Item item) {
            notified = true;
        }
    }

    @Test
    void observerIsNotifiedWhenPotionIsUsed() {
        Player player = new Player(0, 0);
        Inventory inventory = player.getInventory();
        inventory.addItem(new Item(0, 0, "Pozione"));

        FakeObserver observer = new FakeObserver();
        inventory.setObserver(observer);

        Item item = inventory.findFirstPotion();
        new UseItemInteraction(player, item).interact();

        assertTrue(observer.notified);
    }

    @Test
    void observerIsNotNotifiedWithoutPotion() {
        Player player = new Player(0, 0);
        Inventory inventory = player.getInventory();

        FakeObserver observer = new FakeObserver();
        inventory.setObserver(observer);

        Item item = inventory.findFirstPotion();
        assertNull(item);
        assertFalse(observer.notified);
    }
}
