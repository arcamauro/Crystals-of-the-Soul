package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.math.Vector2;

import io.github.crystals_of_the_soul.controller.CollisionManager;

public class ProtectorBehavior implements Player2Behavior {

    private boolean inBattle = false;

    @Override
    public void onBattleStarted(Player2 p2) {
        inBattle = true;
    }

    @Override
    public void onBattleEnded(Player2 p2) {
        inBattle = false;
    }

    @Override
    public void update(Player2 p2, float delta, CollisionManager collisionManager) {
        Vector2 target = p2.peekTarget();
        if (target == null) return;

        float dx = target.x - p2.getState().x;
        float dy = target.y - p2.getState().y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (inBattle) return;

        if (distance > 22f) { // protector stays slightly closer
            float speed = Player2.DEFAULT_SPEED * 0.8f * p2.getState().speedMultiplier;
            float moveX = (dx / distance) * speed;
            float moveY = (dy / distance) * speed;

            float newX = p2.getState().x + moveX * delta;
            float newY = p2.getState().y + moveY * delta;
            p2.tryMove(newX, newY, collisionManager);
        }
    }
}
