package io.github.crystals_of_the_soul.controller.interactions;

import com.badlogic.gdx.math.Vector2;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.ShopNPC;

public class ShopInteraction implements InteractionStrategy {

    public interface Listener {
        void onShopOpened(ShopNPC shop);
    }

    private final Player player;
    private final ShopNPC shop;
    private final Listener listener;

    public ShopInteraction(Player player, ShopNPC shop, Listener listener) {
        this.player = player;
        this.shop = shop;
        this.listener = listener;
    }

    @Override
    public boolean canInteract() {
        return Vector2.dst2(
            player.getX(),
            player.getY(),
            shop.getX(),
            shop.getY()
        ) < 900f;
    }

    @Override
    public void interact() {
        listener.onShopOpened(shop);
    }
}
