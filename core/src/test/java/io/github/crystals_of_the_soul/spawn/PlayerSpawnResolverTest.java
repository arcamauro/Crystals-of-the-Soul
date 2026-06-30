package io.github.crystals_of_the_soul.spawn;

import io.github.crystals_of_the_soul.model.PlayerState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerSpawnResolverTest {

    @Test
    void alwaysUsesSpawnEvenWhenStateHasSavedPosition() {
        PlayerState state = new PlayerState();
        state.x = 39.5f;
        state.y = 197.25f; // outside a 320x176px map

        float[] result = PlayerSpawnResolver.resolve(state, 48f, 57.5f);

        assertEquals(48f, result[0], "Should use spawn X, not saved X");
        assertEquals(57.5f, result[1], "Should use spawn Y, not saved Y");
    }

    @Test
    void usesSpawnWhenStatePositionIsZero() {
        PlayerState state = new PlayerState();
        // state.x = 0, state.y = 0 by default

        float[] result = PlayerSpawnResolver.resolve(state, 48f, 57.5f);

        assertEquals(48f, result[0]);
        assertEquals(57.5f, result[1]);
    }

    @Test
    void usesSpawnEvenWhenStateHasInBoundsNonZeroPosition() {
        PlayerState state = new PlayerState();
        state.x = 100f;
        state.y = 80f;

        float[] result = PlayerSpawnResolver.resolve(state, 48f, 57.5f);

        assertEquals(48f, result[0], "Should always use spawn, not any saved position");
        assertEquals(57.5f, result[1], "Should always use spawn, not any saved position");
    }

    @Test
    void updatesSavedStateToSpawnPosition() {
        PlayerState state = new PlayerState();
        state.x = 39.5f;
        state.y = 197.25f;

        PlayerSpawnResolver.resolve(state, 48f, 57.5f);

        assertEquals(48f, state.x, "State x should be updated to spawn x");
        assertEquals(57.5f, state.y, "State y should be updated to spawn y");
    }
}
