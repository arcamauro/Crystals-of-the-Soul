package io.github.crystals_of_the_soul.entity;

public class Item {


    private float x;
    private float y;

    private String name;

    public Item(float x, float y, String name) {

        this.x = x;
        this.y = y;
        this.name = name;
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
}
