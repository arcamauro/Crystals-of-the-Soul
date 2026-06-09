package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;
import org.junit.jupiter.api.Test;
import io.github.crystals_of_the_soul.model.Inventory;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void usePotionHealsPlayer() {

        Player player = new Player(0, 0);

        player.takeDamage(50);

        Inventory inventory = player.getInventory();

        inventory.addItem(
            new Item(
                0,
                0,
                "Pozione"
            )
        );

        boolean used = inventory.usePotion(player);

        assertTrue(used);
        assertEquals(70, player.getHp());
    }
    @Test
    void usePotionWithoutPotionReturnsFalse() {

        Player player = new Player(0, 0);

        Inventory inventory = player.getInventory();

        boolean used = inventory.usePotion(player);

        assertFalse(used);
    }
    @Test
    void potionCannotHealBeyondMaximumHp() {

        Player player = new Player(0, 0);

        Inventory inventory = player.getInventory();

        inventory.addItem(
            new Item(
                0,
                0,
                "Pozione"
            )
        );

        inventory.usePotion(player);

        assertEquals(
            100,
            player.getHp()
        );
    }

}
