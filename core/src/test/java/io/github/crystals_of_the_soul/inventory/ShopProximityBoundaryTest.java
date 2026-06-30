package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.controller.interactions.ShopInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.ShopNPC;
import io.github.crystals_of_the_soul.model.ShopNPCFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShopProximityBoundaryTest {

    @Test
    void testProximityBoundaryInside() {
        Player player = new Player(100f, 100f);
        // Distance 29f (29^2 = 841 < 900) -> should be inside
        ShopNPC shop = ShopNPCFactory.create(129f, 100f, "potion");
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> {});
        assertTrue(interaction.canInteract(), "Distance 29 should be within interaction range");
    }

    @Test
    void testProximityBoundaryExactly60() {
        Player player = new Player(100f, 100f);
        // Distance 30f (30^2 = 900) -> should not be strictly less than 900
        ShopNPC shop = ShopNPCFactory.create(130f, 100f, "potion");
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> {});
        assertFalse(interaction.canInteract(), "Distance 30 should not be within interaction range (strict less-than)");
    }

    @Test
    void testProximityBoundaryOutside() {
        Player player = new Player(100f, 100f);
        // Distance 31f (31^2 = 961 > 900) -> should be outside
        ShopNPC shop = ShopNPCFactory.create(131f, 100f, "potion");
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> {});
        assertFalse(interaction.canInteract(), "Distance 31 should be outside interaction range");
    }
}
