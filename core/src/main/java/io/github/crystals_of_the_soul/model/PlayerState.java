package io.github.crystals_of_the_soul.model;

public class PlayerState {
    public float x, y;          // position
    public int health;
    public int maxHealth;
    public boolean canAttack;   // false for Player 1 with Blue crystal
    public boolean canTalk;     // false for Player 1 with Red crystal (except vs MIRROR_RED)
    public boolean canSpare;    // false for Player 1 with Red crystal
    public int attack;
    public int defense;

    public PlayerState() {
        health = 100;
        maxHealth = 100;
        canAttack = true;
        canTalk = true;
        canSpare = true;
        attack = 10;
        defense = 0;
    }
}
