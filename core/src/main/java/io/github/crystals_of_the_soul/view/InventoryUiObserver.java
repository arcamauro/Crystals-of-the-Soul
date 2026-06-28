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
        String name = item.getName();
        if (name.contains("Super")) {
            hud.showNotification("+50 HP");
        } else if (name.contains("Elisir") || name.contains("Elixir")) {
            hud.showNotification("+100 HP");
        } else if (name.contains("Spada")) {
            hud.showNotification("+5 ATK");
        } else if (name.contains("Armatura")) {
            hud.showNotification("+5 DEF");
        } else {
            hud.showNotification("+20 HP");
        }
    }
}
