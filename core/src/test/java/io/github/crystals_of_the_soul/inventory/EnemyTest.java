package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.NormalEnemy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnemyTest {
    @Test
    void onAttackReducesEnemyHp() {

        NormalEnemy enemy =
            new NormalEnemy(
                0,
                0
            );

        enemy.onAttack();

        assertEquals(
            40,
            enemy.getHp()
        );
    }
    @Test
    void enemyIsDefeatedAtZeroHp() {

        NormalEnemy enemy =
            new NormalEnemy(
                0,
                0
            );

        for (int i = 0; i < 5; i++) {

            enemy.onAttack();
        }

        assertTrue(
            enemy.isDefeated()
        );
    }
    @Test
    void enemyCanBeSparedAfterThreeTalks() {

        NormalEnemy enemy =
            new NormalEnemy(
                0,
                0
            );

        enemy.onTalk();
        enemy.onTalk();
        enemy.onTalk();

        assertTrue(
            enemy.isDefeated()
        );
    }
}
