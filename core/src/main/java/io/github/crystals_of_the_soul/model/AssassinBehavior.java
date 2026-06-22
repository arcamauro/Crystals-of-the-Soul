package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.math.Vector2;

import io.github.crystals_of_the_soul.controller.CollisionManager;

public class AssassinBehavior implements Player2Behavior {

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
    public void onAttack(Player2 p2, Enemy enemy, StringBuilder logMessage) {
        if (p2.getHp() <= 0) return;
        int companionDamage = p2.getState().damage;
        enemy.takeDamage(companionDamage);
        logMessage.append("\n* L'Assassino colpisce alle spalle infliggendo ").append(companionDamage).append(" danni!");
    }

    @Override
    public void update(Player2 p2, float delta, CollisionManager collisionManager) {
        Vector2 target = p2.peekTarget();
        if (target == null) return;

        float dx = target.x - p2.getState().x;
        float dy = target.y - p2.getState().y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (inBattle) return;

        if (distance > 25f) {
            float speed = Player2.DEFAULT_SPEED * p2.getState().speedMultiplier;
            float moveX = (dx / distance) * speed;
            float moveY = (dy / distance) * speed;

            float newX = p2.getState().x + moveX * delta;
            float newY = p2.getState().y + moveY * delta;
            p2.tryMove(newX, newY, collisionManager);
        }
    }
}
