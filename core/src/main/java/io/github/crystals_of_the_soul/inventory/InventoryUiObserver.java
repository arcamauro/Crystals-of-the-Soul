package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.entity.Item;
import io.github.crystals_of_the_soul.player.Player;
import io.github.crystals_of_the_soul.ui.FloatingTextManager;

public class InventoryUiObserver implements InventoryObserver {

    private final FloatingTextManager floatingTextManager;
    private final Player player;
    public InventoryUiObserver(FloatingTextManager floatingTextManager,Player player) {

        System.out.println("InventoryUiObserver chiamato");
        floatingTextManager.show(
            "+20 HP",
            400,
            300
        );
        this.floatingTextManager = floatingTextManager;
        this.player = player;
    }
    @Override
    public void onItemUsed(Item item) {

        floatingTextManager.show(
            "+20 HP",
            player.getX(),
            player.getY() + 50
        );
    }
}
