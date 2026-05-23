package io.github.crystals_of_the_soul.player;

public class Player {
    private float x, y;
    private float speed = 200f;
    private int hp = 100;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float dx, float dy, float delta) {
        x += dx * speed * delta;
        y += dy * speed * delta;
    }
    public void takeDamage(int damage) {

        hp -= damage;

        if (hp < 0)
            hp = 0;
    }
    public int getHp() {
        return hp;
    }
    public float getX() { return x; }
    public float getY() { return y; }
}
