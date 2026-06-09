package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.controller.BattleManager;
import io.github.crystals_of_the_soul.controller.interactions.EnemyInteraction;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.NormalEnemy;
import io.github.crystals_of_the_soul.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyInteractionBehaviourTest {

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
    void interactStartsBattle() {

        Player player =
            new Player(0, 0);

        Enemy enemy =
            new NormalEnemy(
                10,
                10,
                100
            );

        FakeListener listener =
            new FakeListener();

        BattleManager battleManager =
            new BattleManager(listener);

        EnemyInteraction interaction =
            new EnemyInteraction(
                player,
                enemy,
                battleManager
            );

        interaction.interact();

        assertTrue(
            battleManager.isInBattle()
        );

        assertEquals(
            enemy,
            battleManager.getCurrentEnemy()
        );

        assertTrue(
            listener.battleStarted
        );
    }
}
