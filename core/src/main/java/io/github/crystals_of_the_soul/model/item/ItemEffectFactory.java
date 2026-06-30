package io.github.crystals_of_the_soul.model.item;

public class ItemEffectFactory {

    public static boolean isConsumable(String name) {
        if (name == null) return false;
        String lower = name.toLowerCase();
        return lower.contains("potion") || lower.contains("elixir") || lower.contains("super")
            || lower.contains("pozione") || lower.contains("elisir");
    }

    public static ItemEffect getEffect(String name) {
        if (name == null) return null;
        String lower = name.toLowerCase();
        if (lower.contains("sword") || lower.contains("spada")) {
            return new SwordEffect();
        }
        if (lower.contains("armor") || lower.contains("armatura")) {
            return new ArmorEffect();
        }
        if (lower.contains("elixir") || lower.contains("elisir")) {
            return new ElixirEffect();
        }
        if (lower.contains("super")) {
            return new SuperPotionEffect();
        }
        if (lower.contains("potion") || lower.contains("pozione")) {
            return new PotionEffect();
        }
        return null;
    }
}
