package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.controller.BattleManager;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.NormalEnemy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleManagerTest {

    private static class FakeListener
        implements BattleManager.Listener {

        boolean battleStarted = false;

        @Override
        public void onBattleStarted(Enemy enemy) {

            battleStarted = true;
        }

        @Override
        public void onEnemyHpChanged(String text) {}

        @Override
        public void onDialogueChanged(String text) {}

        @Override
        public void onPlayerHpChanged(int hp) {}

        @Override
        public void onEnemyKilled(Enemy enemy) {}

        @Override
        public void onEnemySpared(Enemy enemy) {}

        @Override
        public void onBossDefeated() {}

        @Override
        public void onPlayerDefeated() {}

        @Override
        public void onBattleExited() {}
    }
    @Test
    void startBattleSetsBattleState() {

        FakeListener listener =
            new FakeListener();

        BattleManager manager =
            new BattleManager(listener);

        Enemy enemy =
            new NormalEnemy(
                0,
                0
            );

        manager.startBattle(enemy);

        assertTrue(
            manager.isInBattle()
        );
    }
    @Test
    void startBattleSetsCurrentEnemy() {

        FakeListener listener =
            new FakeListener();

        BattleManager manager =
            new BattleManager(listener);

        Enemy enemy =
            new NormalEnemy(
                0,
                0
            );

        manager.startBattle(enemy);

        assertEquals(
            enemy,
            manager.getCurrentEnemy()
        );
    }
    @Test
    void startBattleNotifiesListener() {

        FakeListener listener =
            new FakeListener();

        BattleManager manager =
            new BattleManager(listener);

        Enemy enemy =
            new NormalEnemy(
                0,
                0
            );

        manager.startBattle(enemy);

        assertTrue(
            listener.battleStarted
        );
    }
}
