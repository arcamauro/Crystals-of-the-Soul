package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.CrystalType;

public class GreenFloor4Factory extends BossFactory {

    @Override
    public Boss createBoss(float x, float y) {
        return new GuardianBoss(x, y, CrystalType.GREEN);
    }
}
