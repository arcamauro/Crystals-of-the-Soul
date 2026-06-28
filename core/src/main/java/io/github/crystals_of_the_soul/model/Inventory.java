package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.utils.Array;

import io.github.crystals_of_the_soul.model.entity.Item;

public class Inventory {

    private Array<Item> items;

    private InventoryObserver observer;

    public void setObserver(InventoryObserver observer) {
        this.observer = observer;
    }

    public Inventory() {
        items = new Array<>();
    }

    public void addItem(Item item) {
        items.add(item);
        System.out.println(item.getName() + " aggiunto all'inventario");
    }

    public void printInventory() {
        System.out.println("=== INVENTARIO ===");
        for (Item item : items) {
            System.out.println("- " + item.getName());
        }
    }

    public Array<Item> getItems() {
        return items;
    }

    public void removeItem(Item item) {
        items.removeValue(item, true);
        if (observer != null) {
            observer.onItemUsed(item);
        }
    }

    public boolean usePotion(Player player) {
        for (Item item : items) {
            String name = item.getName();
            if (name.contains("Pozione") || name.contains("Elisir") || name.contains("Super")) {
                io.github.crystals_of_the_soul.controller.interactions.UseItemInteraction interaction =
                    new io.github.crystals_of_the_soul.controller.interactions.UseItemInteraction(player, item);
                if (interaction.canInteract()) {
                    interaction.interact();
                    System.out.println(name + " usata!");
                    return true;
                }
            }
        }
        System.out.println("Nessun oggetto curativo!");
        return false;
    }
}
