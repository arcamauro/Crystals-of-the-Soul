package io.github.crystals_of_the_soul.spawn;

import io.github.crystals_of_the_soul.model.PlayerState;

public class PlayerSpawnResolver {

    public static float[] resolve(PlayerState state, float spawnX, float spawnY) {
        state.x = spawnX;
        state.y = spawnY;
        return new float[]{spawnX, spawnY};
    }
}
