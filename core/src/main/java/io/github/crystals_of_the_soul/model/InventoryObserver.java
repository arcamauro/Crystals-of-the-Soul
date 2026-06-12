package io.github.crystals_of_the_soul.model;

import io.github.crystals_of_the_soul.model.entity.Item;

public interface InventoryObserver {
    void onItemUsed(Item item);
}
