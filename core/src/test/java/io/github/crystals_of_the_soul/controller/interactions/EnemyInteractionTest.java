package io.github.crystals_of_the_soul.controller.interactions;

import io.github.crystals_of_the_soul.controller.BattleManager;
import io.github.crystals_of_the_soul.controller.interactions.EnemyInteraction;
import io.github.crystals_of_the_soul.model.NormalEnemy;
import io.github.crystals_of_the_soul.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyInteractionTest {

    @Test
    void playerNearEnemyCanInteract() {

        Player player =
            new Player(0, 0);

        NormalEnemy enemy =
            new NormalEnemy(
                10,
                10,
                100
            );

        BattleManager battleManager =
            new BattleManager(null);

        EnemyInteraction interaction =
            new EnemyInteraction(
                player,
                enemy,
                battleManager
            );

        assertTrue(
            interaction.canInteract()
        );
    }

    @Test
    void playerFarEnemyCannotInteract() {

        Player player =
            new Player(0, 0);

        NormalEnemy enemy =
            new NormalEnemy(
                500,
                500,
                100
            );

        BattleManager battleManager =
            new BattleManager(null);

        EnemyInteraction interaction =
            new EnemyInteraction(
                player,
                enemy,
                battleManager
            );

        assertFalse(
            interaction.canInteract()
        );
    }
}
