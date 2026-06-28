
package io.github.crystals_of_the_soul.model;

public class Player {
    private float x, y;
    private float speed = 200f;
    private int hp = 100;
    private Inventory inventory;
    private PlayerState state;

    // Hitbox configuration (pixels)
    public static final float HITBOX_SIZE = 10f; // change this to shrink/grow hitbox
    public static final float HITBOX_OFFSET = (16f - HITBOX_SIZE) / 2f; // center inside 16x16 sprite

    public Player(float x, float y) {
        this(new PlayerState(), x, y);
    }

    public Player(PlayerState state, float x, float y) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.hp = state.health;
        this.inventory = new Inventory();
    }

    public void update(float dx, float dy, float delta) {
        x += dx * speed * delta;
        y += dy * speed * delta;
    }

    public int takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - getDefense());
        hp -= actualDamage;

        if (hp < 0)
            hp = 0;

        if (state != null) {
            state.health = hp;
        }
        return actualDamage;
    }

    public int getHp() {
        return hp;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getAttack() {
        return state != null ? state.attack : 10;
    }

    public int getDefense() {
        return state != null ? state.defense : 0;
    }

    public void setAttack(int attack) {
        if (state != null) {
            state.attack = attack;
        }
    }

    public void setDefense(int defense) {
        if (state != null) {
            state.defense = defense;
        }
    }

    public void heal(int amount) {
        hp += amount;

        if (hp > 100)
            hp = 100;

        if (state != null) {
            state.health = hp;
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}