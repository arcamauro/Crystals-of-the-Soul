package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.math.Vector2;

import io.github.crystals_of_the_soul.controller.CollisionManager;

public class ProtectorBehavior implements Player2Behavior {

    private boolean inBattle = false;

    @Override
    public void onBattleStarted(Player2 p2) {
        inBattle = true;
        p2.getState().currentShield = p2.getState().shieldCapacity;
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
        logMessage.append("\n* Il Protettore attacca infliggendo ").append(companionDamage).append(" danni!");
    }

    @Override
    public void onTalk(Player2 p2, Enemy enemy, StringBuilder logMessage) {
        if (p2.getHp() <= 0) return;
        logMessage.append("\n* Il Protettore alza lo scudo per proteggerti mentre parli.");
    }

    @Override
    public int onShieldDamage(Player2 p2, int damage, StringBuilder logMessage) {
        if (p2.getHp() <= 0) return damage;

        int shield = p2.getState().currentShield;
        if (shield > 0) {
            if (shield >= damage) {
                p2.getState().currentShield -= damage;
                logMessage.append("\n* Lo scudo del Protettore assorbe completamente il colpo! (Scudo: ").append(p2.getState().currentShield).append(")");
                return 0;
            } else {
                p2.getState().currentShield = 0;
                logMessage.append("\n* Lo scudo del Protettore assorbe ").append(shield).append(" danni e si rompe!");
                damage -= shield;
            }
        }

        p2.takeDamage(damage);
        logMessage.append("\n* Il Protettore fa scudo con il suo corpo e subisce danni! (HP compagno: ").append(p2.getHp()).append(")");
        return 0;
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
