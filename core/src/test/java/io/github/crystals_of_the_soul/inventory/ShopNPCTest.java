package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.ShopNPC;
import io.github.crystals_of_the_soul.model.ShopNPCFactory;
import io.github.crystals_of_the_soul.model.PotionMerchant;
import io.github.crystals_of_the_soul.model.ElixirMerchant;
import io.github.crystals_of_the_soul.model.SpecialMerchant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShopNPCTest {

    @Test
    void testPotionMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(10, 20, "potion");
        assertTrue(shop instanceof PotionMerchant);
        assertEquals(10f, shop.getX());
        assertEquals(20f, shop.getY());
        assertEquals("Mercante di Pozioni", shop.getName());
        assertEquals(15, shop.getPotionPrice());
        assertEquals("Pozione", shop.getItemName());
        assertEquals(0, shop.getImageIndex());
    }

    @Test
    void testElixirMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(30, 40, "elixir");
        assertTrue(shop instanceof ElixirMerchant);
        assertEquals("Saggio degli Elisir", shop.getName());
        assertEquals(30, shop.getPotionPrice());
        assertEquals("Super Pozione", shop.getItemName());
        assertEquals(1, shop.getImageIndex());
    }

    @Test
    void testSpecialMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(50, 60, "special");
        assertTrue(shop instanceof SpecialMerchant);
        assertEquals("Mercante Oscuro", shop.getName());
        assertEquals(50, shop.getPotionPrice());
        assertEquals("Elisir di Forza", shop.getItemName());
        assertEquals(2, shop.getImageIndex());
    }

    @Test
    void testDefaultMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(100, 100, "unknown_type");
        assertTrue(shop instanceof PotionMerchant);
        
        ShopNPC shopNull = ShopNPCFactory.create(100, 100, null);
        assertTrue(shopNull instanceof PotionMerchant);
    }
}
