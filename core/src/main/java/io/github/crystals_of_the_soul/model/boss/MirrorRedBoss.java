package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;

public class MirrorRedBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* Look at me. I am what you have become.",
        "* So much violence... it has changed you.",
        "* Perhaps you could... spare someone.",
        "* Lower your guard. Let us talk.",
        "* This is your end."
    };

    public MirrorRedBoss(float x, float y) {
        super(x, y, 999);
        this.spriteName = "BossR";
    }

    @Override
    public void onAttack(int playerDamage) {
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
        return "Your Reflection";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
