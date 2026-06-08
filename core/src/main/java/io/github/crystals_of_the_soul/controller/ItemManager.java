package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
        GameHud hud
    ) {

        this.player = player;
        this.hud = hud;

        items = new Array<>();
        spawnDebugPotion();
    }
    private void spawnDebugPotion() { items.add( new Item( 50, 50, "Pozione" ) ); }
    public void update() {

        boolean nearItem = false;

        Item collectedItem = null;

        for (Item item : items) {

            float distance = Vector2.dst(
                player.getX(),
                player.getY(),
                item.getX(),
                item.getY()
            );

            if (distance < 50) {

                nearItem = true;

                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {

                    player.getInventory().addItem(item);

                    collectedItem = item;

                }
            }
        }

        if (collectedItem != null) {

            items.removeValue(collectedItem, true);
        }

        hud.setInteractVisible(nearItem);
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
