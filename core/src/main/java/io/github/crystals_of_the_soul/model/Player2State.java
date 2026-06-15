package io.github.crystals_of_the_soul.model;

public class Player2State extends PlayerState {

    public Player2Class playerClass;

    // Combat stats
    public int damage;
    public int defense;
    public float speedMultiplier;

    // Protector only
    public int shieldCapacity;
    public int currentShield;

    // AI target position (following Player 1)
    public float targetX;
    public float targetY;

    public static Player2State create(Player2Class playerClass) {
        Player2State p2 = new Player2State();
        p2.playerClass = playerClass;
        p2.health = 100;
        p2.maxHealth = 100;
        p2.speedMultiplier = 1.5f;
        p2.canAttack = true;
        p2.canSpare = true;
        p2.currentShield = 0;

        switch (playerClass) {
            case ASSASSIN:
                p2.damage = 30;
                p2.defense = 10;
                p2.shieldCapacity = 0;
                break;
            case ARCHER:
                p2.damage = 25;
                p2.defense = 5;
                p2.shieldCapacity = 0;
                break;
            case PROTECTOR:
                p2.damage = 10;
                p2.defense = 30;
                p2.shieldCapacity = 50;
                p2.currentShield = 50;
                break;
        }

        return p2;
    }
}
