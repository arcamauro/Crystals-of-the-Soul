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
        this.spriteName = "Skeleton";
    }

    public NormalEnemy(float x, float y, int maxHp) {
        super(x, y, maxHp);
        this.spriteName = "Skeleton";
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
        if (talkCount == 0) return "* The enemy glares at you menacingly.";
        if (talkCount == 1) return "* The enemy seems hesitant.";
        if (talkCount == 2) return "* The enemy lowers their guard...";
        return "* The enemy stops fighting.";
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
        return "Enemy";
    }
}
