package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;

public class MirrorBlueBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* Fermati. Ascoltami.",
        "* Tutta questa rabbia... da dove viene?",
        "* Non puoi combattermi. Non funzionerà.",
        "* Ricordi chi eri prima del dungeon?",
        "* Le tue parole mi raggiungono...",
        "* Continua a parlare. Sento la tua voce.",
        "* ... Mi stai raggiungendo. Sono ancora qui."
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
        return "La Tua Essenza";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
