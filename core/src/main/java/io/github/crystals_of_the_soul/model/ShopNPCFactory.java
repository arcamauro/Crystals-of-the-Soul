package io.github.crystals_of_the_soul.model;

public class ShopNPCFactory {
    public static ShopNPC create(float x, float y, String type) {
        if (type == null) {
            return new ShopNPC(x, y, "Potion Merchant", 15, "Potion", "Welcome! Would you like to buy a Potion for 15 Gold?", 0);
        }
        
        switch (type.toLowerCase()) {
            case "shop1":
            case "elixir":
                return new ShopNPC(x, y, "Elixir Sage", 30, "Super Potion", "Greetings, traveler. Would you like a Super Potion for 30 Gold?", 1);
            case "shop2":
            case "special":
                return new ShopNPC(x, y, "Dark Merchant", 50, "Strength Elixir", "Who goes there... Would you like to trade 50 Gold for a Strength Elixir?", 2);
            case "shop":
            case "potion":
            default:
                return new ShopNPC(x, y, "Potion Merchant", 15, "Potion", "Welcome! Would you like to buy a Potion for 15 Gold?", 0);
        }
    }
}
