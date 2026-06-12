package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    void takeDamageReducesHp() {

        Player player = new Player(0, 0);

        player.takeDamage(30);

        assertEquals(
            70,
            player.getHp()
        );
    }
    @Test
    void takeDamageCannotGoBelowZero() {

        Player player = new Player(0, 0);

        player.takeDamage(200);

        assertEquals(
            0,
            player.getHp()
        );
    }
    @Test
    void healIncreasesHp() {

        Player player = new Player(0, 0);

        player.takeDamage(50);

        player.heal(20);

        assertEquals(
            70,
            player.getHp()
        );
    }
}
