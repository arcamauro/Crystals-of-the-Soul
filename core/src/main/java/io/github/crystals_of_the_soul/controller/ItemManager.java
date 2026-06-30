package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.crystals_of_the_soul.controller.interactions.ItemInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;
import io.github.crystals_of_the_soul.view.GameHud;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
public class ItemManager {

    private final Array<Item> items;

    private final Player player;
    private final GameHud hud;

    public ItemManager(
        Player player,
        GameHud hud,
        float spawnX,
        float spawnY
    ) {

        this.player = player;
        this.hud = hud;

        items = new Array<>();
        spawnDebugPotion(spawnX, spawnY);
    }
    private void spawnDebugPotion(float x, float y) { items.add(new Item(x, y, "Pozione")); }

    public void clearFloorItems() {
        items.clear();
    }
    public void update() {

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

        if (hud != null) hud.setInteractVisible(nearItem);
    }
    public boolean isNear(Item item) {

        return Vector2.dst(
            player.getX(),
            player.getY(),
            item.getX(),
            item.getY()
        ) < 50;
    }
    public Array<Item> getItems() {

        return items;
    }
}
