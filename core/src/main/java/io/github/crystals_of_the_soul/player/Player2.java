package io.github.crystals_of_the_soul.player;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;

import io.github.crystals_of_the_soul.collision.CollisionManager;
import io.github.crystals_of_the_soul.states.Player2State;

public class Player2 {
    private Player2State state;
    private Queue<Vector2> pathHistory;
    private static final int PATH_DELAY = 20;  // Frame di ritardo per il caterpillar
    private static final float SPEED = 180f;   // Velocità fissa

    public Player2(Player2State state) {
        this.state = state;
        this.pathHistory = new LinkedList<>();
    }

    public void recordPosition(float x, float y) {
        pathHistory.add(new Vector2(x, y));
        if (pathHistory.size() > PATH_DELAY) {
            pathHistory.poll();
        }
    }

    public void followPath(float delta, CollisionManager collisionManager) {
        if (pathHistory.isEmpty()) return;

        Vector2 targetPos = pathHistory.peek();
        float dx = targetPos.x - state.x;
        float dy = targetPos.y - state.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 25) {  // Si ferma quando è a ~25px di distanza
            float moveX = (dx / distance) * SPEED;
            float moveY = (dy / distance) * SPEED;
            
            float deltaMove = delta;
            float newX = state.x + moveX * deltaMove;
            float newY = state.y + moveY * deltaMove;
            
            // Controlla X prima di applicare
            if (!collisionManager.wouldCollide(newX + Player.HITBOX_OFFSET, state.y + Player.HITBOX_OFFSET, Player.HITBOX_SIZE, Player.HITBOX_SIZE)) {
                state.x = newX;
            }

            // Controlla Y prima di applicare (usa la nuova X se è stata accettata)
            newY = state.y + moveY * deltaMove;
            if (!collisionManager.wouldCollide(state.x + Player.HITBOX_OFFSET, newY + Player.HITBOX_OFFSET, Player.HITBOX_SIZE, Player.HITBOX_SIZE)) {
                state.y = newY;
            }
        }
    }

    public void update(float dx, float dy, float delta) {
        state.x += dx * SPEED * delta;
        state.y += dy * SPEED * delta;
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
