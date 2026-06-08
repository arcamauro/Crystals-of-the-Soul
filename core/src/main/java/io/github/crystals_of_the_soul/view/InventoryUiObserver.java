package io.github.crystals_of_the_soul.view;

import io.github.crystals_of_the_soul.model.InventoryObserver;
import io.github.crystals_of_the_soul.model.entity.Item;

public class InventoryUiObserver implements InventoryObserver {

    private final GameHud hud;

    public InventoryUiObserver(GameHud hud) {
        this.hud = hud;
    }

    @Override
    public void onItemUsed(Item item) {
        hud.showNotification("+20 HP");
    }
}
