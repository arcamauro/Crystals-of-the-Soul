package io.github.crystals_of_the_soul.model;

/**
 * Nemico standard del dungeon.
 * Può essere sconfitto tramite attacco o risparmiato tramite dialogo.
 */
public class NormalEnemy extends Enemy {

    private static final int TALKS_TO_SPARE = 3;
    private int talkCount = 0;

    public NormalEnemy(float x, float y) {
        super(x, y, 50);
    }

    public NormalEnemy(float x, float y, int maxHp) {
        super(x, y, maxHp);
    }

    @Override
    public void onAttack(int playerDamage) {
        takeDamage(playerDamage);
    }

    @Override
    public void onTalk() {
        talkCount++;
    }

    @Override
    public String getDialogue() {
        if (talkCount == 0) return "* Il nemico ti fissa minaccioso.";
        if (talkCount == 1) return "* Il nemico sembra esitante.";
        if (talkCount == 2) return "* Il nemico abbassa la guardia...";
        return "* Il nemico smette di combattere.";
    }

    @Override
    public boolean isDefeated() {
        return hp <= 0 || talkCount >= TALKS_TO_SPARE;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public String getName() {
        return "Nemico";
    }
}
