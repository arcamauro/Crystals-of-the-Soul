package io.github.crystals_of_the_soul.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import io.github.crystals_of_the_soul.controller.CollisionManager;

class Player2BattleBehaviorTest {

    @Test
    void assassinDoesNotMoveDuringBattle() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(100f, 0f);
        p2.notifyBattleStarted();
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertEquals(before, p2.getX(), 0.0001, "Should not move during battle");
    }

    @Test
    void protectorDoesNotMoveDuringBattle() {
        Player2State s = Player2State.create(Player2Class.PROTECTOR);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(60f, 0f);
        p2.notifyBattleStarted();
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertEquals(before, p2.getX(), 0.0001, "Protector should not move during battle");
    }

    @Test
    void archerDoesNotMoveDuringBattle() {
        Player2State s = Player2State.create(Player2Class.ARCHER);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(80f, 0f);
        p2.notifyBattleStarted();
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertEquals(before, p2.getX(), 0.0001, "Archer should not move during battle");
    }
}
