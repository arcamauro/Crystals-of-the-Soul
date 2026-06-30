package io.github.crystals_of_the_soul.controller.interactions;

import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;
import io.github.crystals_of_the_soul.model.item.ItemEffect;
import io.github.crystals_of_the_soul.model.item.ItemEffectFactory;

public class UseItemInteraction implements InteractionStrategy {
    private final Player player;
    private final Item item;

    public UseItemInteraction(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public boolean canInteract() {
        if (!player.getInventory().getItems().contains(item, true)) return false;
        if (ItemEffectFactory.isConsumable(item.getName()) && player.isAtFullHp()) return false;
        return true;
    }

    @Override
    public void interact() {
        ItemEffect effect = ItemEffectFactory.getEffect(item.getName());
        if (effect != null) {
            effect.apply(player);
        }
        player.getInventory().removeItem(item);
    }
}
