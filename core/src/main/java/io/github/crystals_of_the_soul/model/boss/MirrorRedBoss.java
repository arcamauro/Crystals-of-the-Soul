package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;

public class MirrorRedBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* Guardami. Sono quello che sei diventato.",
        "* Così tanta violenza... ti ha cambiato.",
        "* Forse potresti... risparmiare qualcuno.",
        "* Abbassa la guardia. Parliamo.",
        "* Ecco la tua fine."
    };

    public MirrorRedBoss(float x, float y) {
        super(x, y, 999);
    }

    @Override
    public void onAttack() {
        takeDamage(1);
        dialogueIndex++;
    }

    @Override
    public boolean isDefeated() {
        return talkCount >= 7;
    }

    @Override
    public int getDamage() {
        return 25;
    }

    @Override
    public String getName() {
        return "Il Tuo Riflesso";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
