package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.CrystalType;

public class GuardianBoss extends Boss {

    private static final String[] DIALOGUE = {
        "* Nessuno passa senza dimostrare il proprio valore.",
        "* Mostrami che sai sia combattere che parlare.",
        "* Il vero guerriero sa quando fermarsi.",
        "* Stai imparando... continua.",
        "* Siete pronti. Passate."
    };

    private final CrystalType crystal;
    private int attackCount = 0;

    public GuardianBoss(float x, float y, CrystalType crystal) {
        super(x, y, 250);
        this.crystal = crystal;
    }

    @Override
    public void onAttack() {
        attackCount++;
        takeDamage(8);
        dialogueIndex++;
    }

    @Override
    public boolean isDefeated() {
        switch (crystal) {
            case RED:   return attackCount >= 5;
            case GREEN: return talkCount >= 3 && attackCount >= 3;
            case BLUE:  return talkCount >= 5;
            default:    return hp <= 0;
        }
    }

    @Override
    public int getDamage() {
        return 15;
    }

    @Override
    public String getName() {
        return "Il Guardiano";
    }

    @Override
    public String getDialogue() {
        return DIALOGUE[Math.min(dialogueIndex, DIALOGUE.length - 1)];
    }
}
