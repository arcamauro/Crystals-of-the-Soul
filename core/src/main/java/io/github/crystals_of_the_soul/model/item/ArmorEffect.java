package io.github.crystals_of_the_soul.model.item;

import io.github.crystals_of_the_soul.model.Player;

public class ArmorEffect implements ItemEffect {
    @Override
    public void apply(Player player) {
        player.setDefense(player.getDefense() + 5);
    }
}
