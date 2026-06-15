package io.github.crystals_of_the_soul.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class Player2InitTest {

    @Test
    void assassinStateInitializesCorrectly() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        assertEquals(Player2Class.ASSASSIN, s.playerClass);
        assertEquals(100, s.health);
        assertEquals(30, s.damage);
        assertEquals(10, s.defense);
        assertEquals(0, s.shieldCapacity);
    }

    @Test
    void archerStateInitializesCorrectly() {
        Player2State s = Player2State.create(Player2Class.ARCHER);
        assertEquals(Player2Class.ARCHER, s.playerClass);
        assertEquals(100, s.health);
        assertEquals(25, s.damage);
        assertEquals(5, s.defense);
        assertEquals(0, s.shieldCapacity);
    }

    @Test
    void protectorStateInitializesCorrectly() {
        Player2State s = Player2State.create(Player2Class.PROTECTOR);
        assertEquals(Player2Class.PROTECTOR, s.playerClass);
        assertEquals(100, s.health);
        assertEquals(10, s.damage);
        assertEquals(30, s.defense);
        assertEquals(50, s.shieldCapacity);
        assertEquals(50, s.currentShield);
    }
}
