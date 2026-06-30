package io.github.crystals_of_the_soul.model.item;

public class ItemEffectFactory {

    public static boolean isConsumable(String name) {
        if (name == null) return false;
        return name.contains("Potion") || name.contains("Elixir") || name.contains("Super");
    }

    public static ItemEffect getEffect(String name) {
        if (name == null) return null;
        if (name.contains("Sword")) {
            return new SwordEffect();
        }
        if (name.contains("Armor")) {
            return new ArmorEffect();
        }
        if (name.contains("Elixir")) {
            return new ElixirEffect();
        }
        if (name.contains("Super")) {
            return new SuperPotionEffect();
        }
        if (name.contains("Potion")) {
            return new PotionEffect();
        }
        return null;
    }
}
