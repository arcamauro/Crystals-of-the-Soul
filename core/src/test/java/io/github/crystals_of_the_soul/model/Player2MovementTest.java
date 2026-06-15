package io.github.crystals_of_the_soul.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.github.crystals_of_the_soul.controller.CollisionManager;

class Player2MovementTest {

    @Test
    void assassinMovesWhenNotInBattle() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(100f, 0f);
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertTrue(p2.getX() > before, "Assassin should move when not in battle");
    }

    @Test
    void archerMovesWhenNotInBattle() {
        Player2State s = Player2State.create(Player2Class.ARCHER);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(80f, 0f);
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertTrue(p2.getX() > before, "Archer should move when not in battle");
    }

    @Test
    void protectorMovesWhenNotInBattle() {
        Player2State s = Player2State.create(Player2Class.PROTECTOR);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(60f, 0f);
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertTrue(p2.getX() > before, "Protector should move when not in battle");
    }
}
