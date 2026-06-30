package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;

public class MirrorBlueBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* Stop. Listen to me.",
        "* All this anger... where does it come from?",
        "* You can't fight me. It won't work.",
        "* Do you remember who you were before the dungeon?",
        "* Your words are reaching me...",
        "* Keep talking. I hear your voice.",
        "* ... You are reaching me. I am still here."
    };

    public MirrorBlueBoss(float x, float y) {
        super(x, y, 150);
        this.spriteName = "BossB";
    }

    @Override
    public void onAttack(int playerDamage) {
        takeDamage(hp);
        dialogueIndex++;
    }

    @Override
    public boolean isDefeated() {
        return hp <= 0;
    }

    @Override
    public boolean isTalkFinalPhase() {
        return dialogueIndex >= 4;
    }

    @Override
    public int getDamage() {
        return 10;
    }

    @Override
    public String getName() {
        return "Your Essence";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
