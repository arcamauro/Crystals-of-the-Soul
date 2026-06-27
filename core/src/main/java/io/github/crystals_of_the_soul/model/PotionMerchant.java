package io.github.crystals_of_the_soul.model;

public class PotionMerchant extends ShopNPC {
    public PotionMerchant(float x, float y) {
        super(
            x,
            y,
            "Mercante di Pozioni",
            15,
            "Pozione",
            "Benvenuto! Vuoi comprare una Pozione per 15 Oro?",
            0
        );
    }
}
