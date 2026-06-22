package io.github.crystals_of_the_soul.model;

import io.github.crystals_of_the_soul.controller.CollisionManager;

public interface Player2Behavior {
    void update(Player2 p2, float delta, CollisionManager collisionManager);
    default void onPlayerMoved(Player2 p2, float px, float py) {}
    default void onBattleStarted(Player2 p2) {}
    default void onBattleEnded(Player2 p2) {}

    // Strategy Pattern combat methods
    default void onAttack(Player2 p2, Enemy enemy, StringBuilder logMessage) {}
    default void onTalk(Player2 p2, Enemy enemy, StringBuilder logMessage) {}
    default int onShieldDamage(Player2 p2, int damage, StringBuilder logMessage) {
        return damage;
    }
}
