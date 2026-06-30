package io.github.crystals_of_the_soul.controller.interactions;

import io.github.crystals_of_the_soul.controller.interactions.UseItemInteraction;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.entity.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UseItemInteractionTest {

    @Test
    void potionCannotBeUsedAtFullHp() {
        Player player = new Player(0, 0);
        Item potion = new Item(0, 0, "Potion");
        player.getInventory().addItem(potion);

        assertFalse(new UseItemInteraction(player, potion).canInteract());
    }

    @Test
    void potionCanBeUsedWhenDamaged() {
        Player player = new Player(0, 0);
        player.takeDamage(10);
        Item potion = new Item(0, 0, "Potion");
        player.getInventory().addItem(potion);

        assertTrue(new UseItemInteraction(player, potion).canInteract());
    }

    @Test
    void superPotionCannotBeUsedAtFullHp() {
        Player player = new Player(0, 0);
        Item potion = new Item(0, 0, "Super Potion");
        player.getInventory().addItem(potion);

        assertFalse(new UseItemInteraction(player, potion).canInteract());
    }

    @Test
    void elixirCannotBeUsedAtFullHp() {
        Player player = new Player(0, 0);
        Item potion = new Item(0, 0, "Elixir");
        player.getInventory().addItem(potion);

        assertFalse(new UseItemInteraction(player, potion).canInteract());
    }

    @Test
    void swordCanAlwaysBeUsedRegardlessOfHp() {
        Player player = new Player(0, 0);
        Item sword = new Item(0, 0, "Sword");
        player.getInventory().addItem(sword);

        assertTrue(new UseItemInteraction(player, sword).canInteract());
    }

    @Test
    void armorCanAlwaysBeUsedRegardlessOfHp() {
        Player player = new Player(0, 0);
        Item armor = new Item(0, 0, "Armor");
        player.getInventory().addItem(armor);

        assertTrue(new UseItemInteraction(player, armor).canInteract());
    }
}
