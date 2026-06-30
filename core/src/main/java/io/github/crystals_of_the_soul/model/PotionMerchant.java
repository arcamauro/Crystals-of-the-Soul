package io.github.crystals_of_the_soul.model;

public class PotionMerchant extends ShopNPC {
    public PotionMerchant(float x, float y) {
        super(
            x,
            y,
            "Potion Merchant",
            15,
            "Potion",
            "Welcome! Would you like to buy a Potion for 15 Gold?",
            0
        );
    }
}
