package io.github.crystals_of_the_soul.controller;

import io.github.crystals_of_the_soul.controller.EscapeKeyHandler;
import org.junit.jupiter.api.Test;

import static io.github.crystals_of_the_soul.controller.EscapeKeyHandler.Action.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EscapeKeyHandlerTest {

    @Test
    void closesShopWhenShopIsOpen() {
        assertEquals(CLOSE_SHOP, EscapeKeyHandler.resolve(true, false, false));
    }

    @Test
    void doesNothingWhenCrystalDialogueIsShowing() {
        assertEquals(NOTHING, EscapeKeyHandler.resolve(false, true, false));
    }

    @Test
    void doesNothingWhenInBattle() {
        assertEquals(NOTHING, EscapeKeyHandler.resolve(false, false, true));
    }

    @Test
    void togglesPauseWhenNothingElseIsOpen() {
        assertEquals(TOGGLE_PAUSE, EscapeKeyHandler.resolve(false, false, false));
    }

    @Test
    void shopTakesPriorityOverCrystalDialogue() {
        assertEquals(CLOSE_SHOP, EscapeKeyHandler.resolve(true, true, false));
    }

    @Test
    void shopTakesPriorityOverBattle() {
        assertEquals(CLOSE_SHOP, EscapeKeyHandler.resolve(true, false, true));
    }
}
