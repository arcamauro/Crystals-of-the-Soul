package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.controller.interactions.ShopInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.ShopNPC;
import io.github.crystals_of_the_soul.model.ShopNPCFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShopInteractionTest {

    private boolean opened = false;

    @Test
    void testShopInteractionProximity() {
        Player player = new Player(100, 100);
        ShopNPC shop = ShopNPCFactory.create(120, 100, "potion"); // 20 units away (within 60 range)
        
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> opened = true);
        
        assertTrue(interaction.canInteract());
        interaction.interact();
        assertTrue(opened);
    }

    @Test
    void testShopInteractionOutOfRange() {
        Player player = new Player(100, 100);
        ShopNPC shop = ShopNPCFactory.create(200, 100, "potion"); // 100 units away (out of 60 range)
        
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> opened = true);
        
        assertFalse(interaction.canInteract());
    }
}
