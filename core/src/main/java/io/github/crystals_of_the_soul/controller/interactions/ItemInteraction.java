package io.github.crystals_of_the_soul.controller.interactions;

import com.badlogic.gdx.math.Vector2;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;

public class ItemInteraction implements InteractionStrategy {

    private final Player player;
    private final Item item;

    public ItemInteraction(
        Player player,
        Item item
    ) {
        this.player = player;
        this.item = item;
    }

    @Override
    public boolean canInteract() {

        return Vector2.dst(
            player.getX(),
            player.getY(),
            item.getX(),
            item.getY()
        ) < 50;
    }

    @Override
    public void interact() {

        player.getInventory().addItem(item);
    }

    public Item getItem() {

        return item;
    }
}
