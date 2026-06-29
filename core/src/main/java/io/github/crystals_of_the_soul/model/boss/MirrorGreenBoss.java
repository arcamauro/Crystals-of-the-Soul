package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;

public class MirrorGreenBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* Mi conosci. Sono te.",
        "* Le tue scelte ti hanno portato qui.",
        "* Non c'è niente da dire. Solo da fare.",
        "* Combatti come sai fare."
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
        return hp <= 0;
    }

    @Override
    public int getDamage() {
        return 15;
    }

    @Override
    public String getName() {
        return "La Tua Ombra";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
