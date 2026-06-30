package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.ShopNPC;
import io.github.crystals_of_the_soul.model.ShopNPCFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShopNPCTest {

    @Test
    void testPotionMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(10, 20, "potion");
        assertNotNull(shop);
        assertEquals(10f, shop.getX());
        assertEquals(20f, shop.getY());
        assertEquals("Potion Merchant", shop.getName());
        assertEquals(15, shop.getPotionPrice());
        assertEquals("Potion", shop.getItemName());
        assertEquals(0, shop.getImageIndex());
    }

    @Test
    void testElixirMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(30, 40, "elixir");
        assertNotNull(shop);
        assertEquals("Elixir Sage", shop.getName());
        assertEquals(30, shop.getPotionPrice());
        assertEquals("Super Potion", shop.getItemName());
        assertEquals(1, shop.getImageIndex());
    }

    @Test
    void testSpecialMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(50, 60, "special");
        assertNotNull(shop);
        assertEquals("Dark Merchant", shop.getName());
        assertEquals(50, shop.getPotionPrice());
        assertEquals("Strength Elixir", shop.getItemName());
        assertEquals(2, shop.getImageIndex());
    }

    @Test
    void testDefaultMerchantCreation() {
        ShopNPC shop = ShopNPCFactory.create(100, 100, "unknown_type");
        assertNotNull(shop);
        assertEquals("Potion Merchant", shop.getName());
        
        ShopNPC shopNull = ShopNPCFactory.create(100, 100, null);
        assertNotNull(shopNull);
        assertEquals("Potion Merchant", shopNull.getName());
    }

    @Test
    void testCaseInsensitiveMerchantCreation() {
        ShopNPC potionShop = ShopNPCFactory.create(0, 0, "PoTiOn");
        assertEquals("Potion Merchant", potionShop.getName());

        ShopNPC elixirShop = ShopNPCFactory.create(0, 0, "ELIXIR");
        assertEquals("Elixir Sage", elixirShop.getName());

        ShopNPC specialShop = ShopNPCFactory.create(0, 0, "Special");
        assertEquals("Dark Merchant", specialShop.getName());
    }

    @Test
    void testMerchantDialogues() {
        ShopNPC potionShop = ShopNPCFactory.create(0, 0, "potion");
        assertEquals("Welcome! Would you like to buy a Potion for 15 Gold?", potionShop.getDialogue());

        ShopNPC elixirShop = ShopNPCFactory.create(0, 0, "elixir");
        assertEquals("Greetings, traveler. Would you like a Super Potion for 30 Gold?", elixirShop.getDialogue());

        ShopNPC specialShop = ShopNPCFactory.create(0, 0, "special");
        assertEquals("Who goes there... Would you like to trade 50 Gold for a Strength Elixir?", specialShop.getDialogue());
    }

    @Test
    void testBoundaryCoordinates() {
        ShopNPC shopNeg = ShopNPCFactory.create(-1000.5f, -2000.75f, "potion");
        assertEquals(-1000.5f, shopNeg.getX());
        assertEquals(-2000.75f, shopNeg.getY());

        ShopNPC shopMax = ShopNPCFactory.create(Float.MAX_VALUE, Float.MIN_VALUE, "potion");
        assertEquals(Float.MAX_VALUE, shopMax.getX());
        assertEquals(Float.MIN_VALUE, shopMax.getY());
    }
}
