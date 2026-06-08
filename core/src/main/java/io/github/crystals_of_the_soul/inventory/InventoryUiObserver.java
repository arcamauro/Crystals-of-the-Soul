package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.entity.Item;
import io.github.crystals_of_the_soul.view.GameHud;

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
