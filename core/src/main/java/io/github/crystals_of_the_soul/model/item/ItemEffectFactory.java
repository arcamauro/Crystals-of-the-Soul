package io.github.crystals_of_the_soul.model.item;

public class ItemEffectFactory {

    public static boolean isConsumable(String name) {
        if (name == null) return false;
        return name.contains("Pozione") || name.contains("Potion")
            || name.contains("Elisir") || name.contains("Elixir")
            || name.contains("Super");
    }

    public static ItemEffect getEffect(String name) {
        if (name == null) return null;
        if (name.contains("Spada")) {
            return new SwordEffect();
        }
        if (name.contains("Armatura")) {
            return new ArmorEffect();
        }
        if (name.contains("Elisir") || name.contains("Elixir")) {
            return new ElixirEffect();
        }
        if (name.contains("Super")) {
            return new SuperPotionEffect();
        }
        if (name.contains("Pozione") || name.contains("Potion")) {
            return new PotionEffect();
        }
        return null;
    }
}
