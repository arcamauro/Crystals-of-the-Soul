package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;

public class MirrorGreenBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* You know me. I am you.",
        "* Your choices have brought you here.",
        "* There is nothing to say. Only to act.",
        "* Fight as you know how."
    };

    public MirrorGreenBoss(float x, float y) {
        super(x, y, 180);
        this.spriteName = "BossG";
    }

    @Override
    public void onAttack(int playerDamage) {
        takeDamage(playerDamage);
        dialogueIndex++;
    }

    @Override
    public boolean isDefeated() {
        // GREEN crystal can resolve the fight either way: spare by talking or kill by depleting HP.
        return hp <= 0 || talkCount >= 4;
    }

    @Override
    public int getDamage() {
        return 15;
    }

    @Override
    public String getName() {
        return "Your Shadow";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
