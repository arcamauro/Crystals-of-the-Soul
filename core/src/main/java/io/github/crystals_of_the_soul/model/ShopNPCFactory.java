package io.github.crystals_of_the_soul.model;

public class ShopNPCFactory {
    public static ShopNPC create(float x, float y, String type) {
        if (type == null) {
            return new PotionMerchant(x, y);
        }
        
        switch (type.toLowerCase()) {
            case "shop1":
            case "elixir":
                return new ElixirMerchant(x, y);
            case "shop2":
            case "special":
                return new SpecialMerchant(x, y);
            case "shop":
            case "potion":
            default:
                return new PotionMerchant(x, y);
        }
    }
}
