package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.controller.interactions.ShopInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.ShopNPC;
import io.github.crystals_of_the_soul.model.PotionMerchant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShopProximityBoundaryTest {

    @Test
    void testProximityBoundaryInside() {
        Player player = new Player(100f, 100f);
        // Distance 59f (59^2 = 3481 < 3600) -> should be inside
        ShopNPC shop = new PotionMerchant(159f, 100f);
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> {});
        assertTrue(interaction.canInteract(), "Distance 59 should be within interaction range");
    }

    @Test
    void testProximityBoundaryExactly60() {
        Player player = new Player(100f, 100f);
        // Distance 60f (60^2 = 3600) -> should not be strictly less than 3600
        ShopNPC shop = new PotionMerchant(160f, 100f);
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> {});
        assertFalse(interaction.canInteract(), "Distance 60 should not be within interaction range (strict less-than)");
    }

    @Test
    void testProximityBoundaryOutside() {
        Player player = new Player(100f, 100f);
        // Distance 61f (61^2 = 3721 > 3600) -> should be outside
        ShopNPC shop = new PotionMerchant(161f, 100f);
        ShopInteraction interaction = new ShopInteraction(player, shop, s -> {});
        assertFalse(interaction.canInteract(), "Distance 61 should be outside interaction range");
    }
}
