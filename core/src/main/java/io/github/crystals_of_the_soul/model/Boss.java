package io.github.crystals_of_the_soul.model;

public abstract class Boss extends Enemy {

    protected int talkCount = 0;
    protected int dialogueIndex = 0;

    protected Boss(float x, float y, int maxHp) {
        super(x, y, maxHp);
    }

    @Override
    public void onTalk() {
        talkCount++;
        dialogueIndex++;
    }

    public boolean isTalkFinalPhase() {
        return false;
    }
}
