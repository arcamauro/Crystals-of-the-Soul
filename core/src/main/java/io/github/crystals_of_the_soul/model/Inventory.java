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

    public boolean usePotion(Player player) {
        for (Item item : items) {
            String name = item.getName();
            if (name.contains("Pozione") || name.contains("Elisir") || name.contains("Super")) {
                int healAmount = 20;
                if (name.contains("Super")) {
                    healAmount = 50;
                } else if (name.contains("Elisir")) {
                    healAmount = 100;
                }

                if (observer != null) {
                    observer.onItemUsed(item);
                }
                player.heal(healAmount);
                items.removeValue(item, true);
                System.out.println(name + " usata! Curato di " + healAmount + " HP.");
                return true;
            }
        }
        System.out.println("Nessun oggetto curativo!");
        return false;
    }
}
