package io.github.crystals_of_the_soul.controller;

import io.github.crystals_of_the_soul.inventory.InventoryUiObserver;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.view.GameHud;

public class InventoryManager {

    private final Player player;
    private final InputHandler inputHandler;

    public InventoryManager(
        Player player,
        GameHud hud,
        InputHandler inputHandler
    ) {

        this.player = player;
        this.inputHandler = inputHandler;

        player.getInventory().setObserver(
            new InventoryUiObserver(hud)
        );
    }

    public void update() {

        if (inputHandler.isUsePotionPressed()) {

            usePotion();
        }
    }

    public void usePotion() {

        player.getInventory().usePotion(player);
    }
}
