package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.utils.Array;
import io.github.crystals_of_the_soul.controller.ItemManager;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemManagerFloorTest {

    @Test
    void potionSpawnsAtGivenPosition() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, 100f, 200f);

        Array<Item> items = manager.getItems();

        assertFalse(items.isEmpty(), "Should have an initial item");
        assertEquals(100f, items.get(0).getX(), "Potion X should match spawn X");
        assertEquals(200f, items.get(0).getY(), "Potion Y should match spawn Y");
    }

    @Test
    void potionDoesNotSpawnAtHardcodedCoords() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, 100f, 200f);

        Array<Item> items = manager.getItems();

        assertFalse(items.isEmpty(), "Should have an initial item");
        assertNotEquals(50f, items.get(0).getX(), "Potion should not be at hardcoded x=50");
        assertNotEquals(50f, items.get(0).getY(), "Potion should not be at hardcoded y=50");
    }

    @Test
    void clearFloorItemsRemovesAllItems() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, 100f, 200f);

        manager.clearFloorItems();

        assertTrue(manager.getItems().isEmpty(), "Items should be empty after floor change");
    }

    @Test
    void getNearItemReturnsNullWhenPlayerIsFarFromAllItems() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, 300f, 300f);

        assertNull(manager.getNearItem(), "Should return null when no items are within range");
    }

    @Test
    void getNearItemReturnsItemWhenPlayerIsClose() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, 10f, 10f);

        Item result = manager.getNearItem();

        assertNotNull(result, "Should return an item when player is within range");
        assertEquals(10f, result.getX());
        assertEquals(10f, result.getY());
    }

    @Test
    void getNearItemReturnsNullAfterFloorCleared() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, 10f, 10f);

        manager.clearFloorItems();

        assertNull(manager.getNearItem(), "Should return null after all items are cleared");
    }

    @Test
    void getNearItemReturnsOnlyTheCloseOneWhenMultipleExist() {
        Player player = new Player(0, 0);
        // spawn the default item far away
        ItemManager manager = new ItemManager(player, 500f, 500f);
        // manually add a close item
        manager.getItems().add(new Item(5f, 5f, "Pozione"));

        Item result = manager.getNearItem();

        assertNotNull(result);
        assertEquals(5f, result.getX(), "Should return the nearby item, not the distant one");
    }
}
