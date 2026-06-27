package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.math.Vector2;

import io.github.crystals_of_the_soul.controller.CollisionManager;

public class Player2 {
    private final Player2State state;
    private final Vector2[] pathHistory;
    private int pathHistorySize = 0;
    private int pathHistoryIndex = 0; // Circular write index
    public static final int PATH_DELAY = 20;  // Frame di ritardo per il caterpillar
    public static final float DEFAULT_SPEED = 180f;   // Velocità fissa base

    private Player2Behavior behavior;

    public Player2(Player2State state) {
        this(state, Player2Factory.createBehavior(state.playerClass));
    }

    public Player2(Player2State state, Player2Behavior behavior) {
        this.state = state;
        this.pathHistory = new Vector2[PATH_DELAY];
        for (int i = 0; i < PATH_DELAY; i++) {
            pathHistory[i] = new Vector2();
        }
        this.behavior = behavior;
    }

    public Player2Behavior getBehavior() {
        return behavior;
    }

    public void recordPosition(float x, float y) {
        if (pathHistorySize < PATH_DELAY) {
            pathHistory[pathHistorySize].set(x, y);
            pathHistorySize++;
        } else {
            pathHistory[pathHistoryIndex].set(x, y);
            pathHistoryIndex = (pathHistoryIndex + 1) % PATH_DELAY;
        }
    }

    public void followPath(float delta, CollisionManager collisionManager) {
        if (behavior != null) {
            behavior.update(this, delta, collisionManager);
        }
    }

    public void notifyBattleStarted() {
        if (behavior != null) behavior.onBattleStarted(this);
    }

    public void notifyBattleEnded() {
        if (behavior != null) behavior.onBattleEnded(this);
    }

    // Helpers used by behaviors
    public Vector2 peekTarget() {
        if (pathHistorySize == 0) return null;
        if (pathHistorySize < PATH_DELAY) {
            return pathHistory[0];
        } else {
            return pathHistory[pathHistoryIndex];
        }
    }

    public void tryMove(float newX, float newY, CollisionManager collisionManager) {
        // Controlla X prima di applicare
        if (!collisionManager.wouldCollide(newX + Player.HITBOX_OFFSET, state.y + Player.HITBOX_OFFSET, Player.HITBOX_SIZE, Player.HITBOX_SIZE)) {
            state.x = newX;
        }

        // Controlla Y prima di applicare (usa la nuova X se è stata accettata)
        if (!collisionManager.wouldCollide(state.x + Player.HITBOX_OFFSET, newY + Player.HITBOX_OFFSET, Player.HITBOX_SIZE, Player.HITBOX_SIZE)) {
            state.y = newY;
        }
    }

    public void update(float dx, float dy, float delta) {
        state.x += dx * DEFAULT_SPEED * delta;
        state.y += dy * DEFAULT_SPEED * delta;
    }

    public void takeDamage(int damage) {
        // Applica difesa al danno ricevuto
        int actualDamage = Math.max(0, damage - state.defense);
        state.health -= actualDamage;

        if (state.health < 0)
            state.health = 0;
    }

    public int getHp() {
        return state.health;
    }

    public float getX() {
        return state.x;
    }

    public float getY() {
        return state.y;
    }

    public Player2State getState() {
        return state;
    }
}
