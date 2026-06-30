package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.utils.Array;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.NormalEnemy;
import io.github.crystals_of_the_soul.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloorTransitionServiceTest {

    @Test
    void canAdvanceWhenNoEnemiesRemain() {
        Array<Enemy> enemies = new Array<>();
        assertTrue(FloorTransitionService.canAdvance(enemies));
    }

    @Test
    void cannotAdvanceWhenOneEnemyRemains() {
        Array<Enemy> enemies = new Array<>();
        enemies.add(new NormalEnemy(0, 0));
        assertFalse(FloorTransitionService.canAdvance(enemies));
    }

    @Test
    void cannotAdvanceWhenMultipleEnemiesRemain() {
        Array<Enemy> enemies = new Array<>();
        enemies.add(new NormalEnemy(0, 0));
        enemies.add(new NormalEnemy(10, 10));
        assertFalse(FloorTransitionService.canAdvance(enemies));
    }

    @Test
    void healAmountIsApplied() {
        Player player = new Player(0, 0);
        player.takeDamage(50);
        int hpBefore = player.getHp();

        FloorTransitionService.applyHeal(player);

        assertEquals(hpBefore + FloorTransitionService.HEAL_AMOUNT, player.getHp());
    }

    @Test
    void healDoesNotExceedMaxHp() {
        Player player = new Player(0, 0);
        player.takeDamage(5);

        FloorTransitionService.applyHeal(player);

        assertEquals(Player.MAX_HP, player.getHp());
    }

    @Test
    void healAtFullHpKeepsMaxHp() {
        Player player = new Player(0, 0);

        FloorTransitionService.applyHeal(player);

        assertEquals(Player.MAX_HP, player.getHp());
    }
}
