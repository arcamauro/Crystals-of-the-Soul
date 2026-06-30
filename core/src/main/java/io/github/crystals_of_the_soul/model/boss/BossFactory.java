package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.CrystalType;

public abstract class BossFactory {

    public abstract Boss createBoss(float x, float y);

    public static BossFactory forFloor(CrystalType crystal, int floor) {
        switch (floor) {
            case 4:
                switch (crystal) {
                    case RED:   return new RedFloor4Factory();
                    case GREEN: return new GreenFloor4Factory();
                    case BLUE:  return new BlueFloor4Factory();
                }
            case 5:
                switch (crystal) {
                    case RED:   return new RedFloor5Factory();
                    case GREEN: return new GreenFloor5Factory();
                    case BLUE:  return new BlueFloor5Factory();
                }
            default:
                throw new IllegalArgumentException("No boss for floor " + floor + " / crystal " + crystal);
        }
    }
}
