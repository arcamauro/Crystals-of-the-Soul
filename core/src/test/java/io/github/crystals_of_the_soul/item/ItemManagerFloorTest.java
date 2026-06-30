package io.github.crystals_of_the_soul.item;

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
        ItemManager manager = new ItemManager(player, null, 100f, 200f);

        Array<Item> items = manager.getItems();

        assertFalse(items.isEmpty(), "Should have an initial item");
        assertEquals(100f, items.get(0).getX(), "Potion X should match spawn X");
        assertEquals(200f, items.get(0).getY(), "Potion Y should match spawn Y");
    }

    @Test
    void potionDoesNotSpawnAtHardcodedCoords() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, null, 100f, 200f);

        Array<Item> items = manager.getItems();

        assertFalse(items.isEmpty(), "Should have an initial item");
        assertNotEquals(50f, items.get(0).getX(), "Potion should not be at hardcoded x=50");
        assertNotEquals(50f, items.get(0).getY(), "Potion should not be at hardcoded y=50");
    }

    @Test
    void clearFloorItemsRemovesAllItems() {
        Player player = new Player(0, 0);
        ItemManager manager = new ItemManager(player, null, 100f, 200f);

        manager.clearFloorItems();

        assertTrue(manager.getItems().isEmpty(), "Items should be empty after floor change");
    }
}
