package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.utils.Array;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.Player;

public final class FloorTransitionService {

    public static final int HEAL_AMOUNT = 20;

    private FloorTransitionService() {}

    public static boolean canAdvance(Array<Enemy> enemies) {
        return enemies.isEmpty();
    }

    public static void applyHeal(Player player) {
        player.heal(HEAL_AMOUNT);
    }
}
