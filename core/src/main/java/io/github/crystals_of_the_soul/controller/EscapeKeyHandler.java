package io.github.crystals_of_the_soul.controller;

public class EscapeKeyHandler {

    public enum Action { TOGGLE_PAUSE, CLOSE_SHOP, NOTHING }

    public static Action resolve(boolean inShop, boolean crystalDialogueShowing, boolean inBattle) {
        if (inShop) return Action.CLOSE_SHOP;
        if (crystalDialogueShowing) return Action.NOTHING;
        if (inBattle) return Action.NOTHING;
        return Action.TOGGLE_PAUSE;
    }
}
