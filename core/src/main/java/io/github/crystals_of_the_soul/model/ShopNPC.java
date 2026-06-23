package io.github.crystals_of_the_soul.model;

public class ShopNPC {
    private final float x;
    private final float y;
    private final String name;
    private final int potionPrice = 15;
    private final int imageIndex;

    public ShopNPC(float x, float y, int imageIndex) {
        this.x = x;
        this.y = y;
        this.imageIndex = imageIndex;
        this.name = "Mercante";
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getPotionPrice() {
        return potionPrice;
    }

    public int getImageIndex() {
        return imageIndex;
    }
}
