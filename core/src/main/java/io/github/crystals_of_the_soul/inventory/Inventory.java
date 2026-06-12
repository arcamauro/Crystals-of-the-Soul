package io.github.crystals_of_the_soul.inventory;

import com.badlogic.gdx.utils.Array;

import io.github.crystals_of_the_soul.model.entity.Item;
import io.github.crystals_of_the_soul.model.Player;
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


            if (item.getName().contains("Pozione")) {

                if(observer != null) {
                    observer.onItemUsed(item);
                }
                player.heal(20);

                items.removeValue(item, true);

                System.out.println("Pozione usata!");

                return true;
            }
        }

        System.out.println("Nessuna pozione!");

        return false;
    }
}
