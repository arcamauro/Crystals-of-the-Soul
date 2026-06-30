package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.CrystalType;

public class GuardianBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* No one passes without proving their worth.",
        "* Show me you can both fight and speak.",
        "* A true warrior knows when to stop.",
        "* You are learning... keep going.",
        "* You are ready. Pass."
    };

    private final CrystalType crystal;
    private int attackCount = 0;

    public GuardianBoss(float x, float y, CrystalType crystal) {
        super(x, y, 250);
        this.crystal = crystal;
        if (crystal == CrystalType.RED) {
            this.spriteName = "BossB"; // RED crystal -> Blue boss
        } else if (crystal == CrystalType.GREEN) {
            this.spriteName = "BossG"; // GREEN crystal -> Green boss
        } else if (crystal == CrystalType.BLUE) {
            this.spriteName = "BossR"; // BLUE crystal -> Red boss
        }
    }

    @Override
    public void onAttack(int playerDamage) {
        attackCount++;
        takeDamage(playerDamage);
        dialogueIndex++;
    }

    @Override
    public boolean isDefeated() {
        if (hp <= 0) return true;
        switch (crystal) {
            case RED:   return attackCount >= 5;
            // GREEN crystal can resolve the fight either way: spare by talking or kill by attacking.
            case GREEN: return talkCount >= 5 || attackCount >= 5;
            case BLUE:  return talkCount >= 5;
            default:    return false;
        }
    }

    @Override
    public int getDamage() {
        return 15;
    }

    @Override
    public String getName() {
        return "The Guardian";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
