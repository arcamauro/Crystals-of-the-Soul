package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.controller.interactions.ItemInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;
import org.junit.jupiter.api.Test;
import io.github.crystals_of_the_soul.model.Inventory;
import static org.junit.jupiter.api.Assertions.*;

class ItemInteractionTest {

    @Test
    void playerNearItemCanInteract() {

        Player player =
            new Player(0, 0);

        Item item =
            new Item(
                10,
                10,
                "Pozione"
            );

        ItemInteraction interaction =
            new ItemInteraction(
                player,
                item
            );

        assertTrue(
            interaction.canInteract()
        );
    }

    @Test
    void playerFarFromItemCannotInteract() {

        Player player =
            new Player(0, 0);

        Item item =
            new Item(
                500,
                500,
                "Pozione"
            );

        ItemInteraction interaction =
            new ItemInteraction(
                player,
                item
            );

        assertFalse(
            interaction.canInteract()
        );
    }
}
