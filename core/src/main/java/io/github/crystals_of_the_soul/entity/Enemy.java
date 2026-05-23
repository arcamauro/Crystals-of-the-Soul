package io.github.crystals_of_the_soul.entity;

public class Enemy {

    private float x;
    private float y;
    private int hp = 50;
    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void takeDamage(int damage) {

        hp -= damage;

        if (hp < 0)
            hp = 0;
    }
    public int getHp() {
        return hp;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
