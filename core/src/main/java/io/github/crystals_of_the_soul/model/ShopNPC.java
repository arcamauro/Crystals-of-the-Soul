package io.github.crystals_of_the_soul.model;

public abstract class ShopNPC {
    protected final float x;
    protected final float y;
    protected final String name;
    protected final int potionPrice;
    protected final String itemName;
    protected final String dialogue;
    protected final int imageIndex;

    public ShopNPC(float x, float y, String name, int potionPrice, String itemName, String dialogue, int imageIndex) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.potionPrice = potionPrice;
        this.itemName = itemName;
        this.dialogue = dialogue;
        this.imageIndex = imageIndex;
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

    public String getItemName() {
        return itemName;
    }

    public String getDialogue() {
        return dialogue;
    }

    public int getImageIndex() {
        return imageIndex;
    }
}
