package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.crystals_of_the_soul.controller.interactions.ItemInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;

public class ItemManager {

    private final Array<Item> items;

    private final Player player;

    public ItemManager(
        Player player,
        float spawnX,
        float spawnY
    ) {

        this.player = player;

        items = new Array<>();
        spawnDebugPotion(spawnX, spawnY);
    }
    private void spawnDebugPotion(float x, float y) { items.add(new Item(x, y, "Potion")); }

    public void clearFloorItems() {
        items.clear();
    }
    public boolean update() {

        boolean nearItem = false;

        Item collectedItem = null;

        for (Item item : items) {

            ItemInteraction interaction =
                new ItemInteraction(
                    player,
                    item
                );

            if (interaction.canInteract()) {

                nearItem = true;

                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {

                    interaction.interact();

                    collectedItem = item;
                }
            }
        }

        if (collectedItem != null) {

            items.removeValue(collectedItem, true);
        }

        return nearItem;
    }
    public boolean isNear(Item item) {
        return Vector2.dst(player.getX(), player.getY(), item.getX(), item.getY()) < 50;
    }

    public Item getNearItem() {
        for (Item item : items) {
            if (isNear(item)) return item;
        }
        return null;
    }
    public Array<Item> getItems() {

        return items;
    }
}
