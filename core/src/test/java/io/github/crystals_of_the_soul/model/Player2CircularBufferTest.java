package io.github.crystals_of_the_soul.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Vector2;

class Player2CircularBufferTest {

    @Test
    void testInitialStateReturnsNull() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        Player2 p2 = Player2Factory.create(s);
        
        assertNull(p2.peekTarget(), "Initially, target trail should be null");
    }

    @Test
    void testUnderCapacityReturnsFirstElement() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        Player2 p2 = Player2Factory.create(s);
        
        p2.recordPosition(10f, 20f);
        p2.recordPosition(15f, 25f);
        p2.recordPosition(20f, 30f);
        
        Vector2 target = p2.peekTarget();
        assertNotNull(target);
        assertEquals(10f, target.x, 0.0001);
        assertEquals(20f, target.y, 0.0001);
    }

    @Test
    void testAtCapacityReturnsFirstElement() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        Player2 p2 = Player2Factory.create(s);
        
        // Record exactly PATH_DELAY (20) positions
        for (int i = 0; i < Player2.PATH_DELAY; i++) {
            p2.recordPosition(i * 10f, i * 20f);
        }
        
        Vector2 target = p2.peekTarget();
        assertNotNull(target);
        assertEquals(0f, target.x, 0.0001);
        assertEquals(0f, target.y, 0.0001);
    }

    @Test
    void testOverCapacityOverwritesOldestAndAdvancesPointer() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        Player2 p2 = Player2Factory.create(s);
        
        // Record 21 positions (one over PATH_DELAY)
        for (int i = 0; i <= Player2.PATH_DELAY; i++) {
            p2.recordPosition(i * 10f, i * 20f);
        }
        
        // The first position (0, 0) should be overwritten by (200, 400)
        // The new oldest position should be the second one recorded (10, 20)
        Vector2 target = p2.peekTarget();
        assertNotNull(target);
        assertEquals(10f, target.x, 0.0001);
        assertEquals(20f, target.y, 0.0001);
    }

    @Test
    void testPreallocatedReferenceReuse() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        Player2 p2 = Player2Factory.create(s);
        
        // Record exactly 20 times to fill the buffer
        for (int i = 0; i < Player2.PATH_DELAY; i++) {
            p2.recordPosition(i * 10f, i * 10f);
        }
        
        // Target is oldest (index 0)
        Vector2 firstTargetRef = p2.peekTarget();
        
        // Record 20 more times. We write to 0, then 1, ..., up to 19.
        // After recording 20 more times, the write pointer wrapped around fully,
        // and the current oldest (at index 0) is still the same pre-allocated Vector2 object ref,
        // but now updated with new coordinates.
        for (int i = 0; i < Player2.PATH_DELAY; i++) {
            p2.recordPosition(1000f, 1000f);
        }
        
        Vector2 secondTargetRef = p2.peekTarget();
        
        // Identity check: the references must be exactly identical (reusing the same object instances!)
        assertSame(firstTargetRef, secondTargetRef, "Vector2 objects must be reused to avoid allocations");
    }
}
