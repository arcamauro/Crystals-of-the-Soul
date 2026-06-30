package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;

public class RedFloor5Factory extends BossFactory {

    @Override
    public Boss createBoss(float x, float y) {
        return new MirrorBlueBoss(x, y);
    }
}
