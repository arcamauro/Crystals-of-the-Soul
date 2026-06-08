package io.github.crystals_of_the_soul.inventory;

import io.github.crystals_of_the_soul.model.entity.Item;

public interface InventoryObserver {
    void onItemUsed(Item item);
}
