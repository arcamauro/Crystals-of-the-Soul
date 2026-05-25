package io.github.crystals_of_the_soul.entity;

import io.github.crystals_of_the_soul.states.CrystalType;

public class Boss extends Enemy {

    public enum BossType {
        ASSASSIN,
        ARCHER,
        PROTECTOR,
        GUARDIAN,
        MIRROR_RED,
        MIRROR_GREEN,
        MIRROR_BLUE
    }

    private final BossType type;
    private int talkCount = 0;
    private int attackCount = 0;
    private int dialogueIndex = 0;

    private Boss(float x, float y, BossType type, int maxHp) {
        super(x, y, maxHp);
        this.type = type;
    }

    public static Boss createForFloor(CrystalType crystal, int floor, float x, float y) {
        switch (floor) {
            case 3:
                switch (crystal) {
                    case RED:   return new Boss(x, y, BossType.ASSASSIN, 150);
                    case GREEN: return new Boss(x, y, BossType.ARCHER, 120);
                    case BLUE:  return new Boss(x, y, BossType.PROTECTOR, 200);
                }
            case 4:
                return new Boss(x, y, BossType.GUARDIAN, 250);
            case 5:
                switch (crystal) {
                    case RED:   return new Boss(x, y, BossType.MIRROR_RED, 999);
                    case GREEN: return new Boss(x, y, BossType.MIRROR_GREEN, 180);
                    case BLUE:  return new Boss(x, y, BossType.MIRROR_BLUE, 999);
                }
            default:
                return new Boss(x, y, BossType.ARCHER, 120);
        }
    }

    @Override
    public void onAttack() {
        attackCount++;
        switch (type) {
            case ASSASSIN:
                takeDamage(15);
                break;
            case ARCHER:
                takeDamage(10);
                break;
            case PROTECTOR:
                takeDamage(3);
                break;
            case GUARDIAN:
                takeDamage(8);
                break;
            case MIRROR_RED:
                takeDamage(1);
                break;
            case MIRROR_GREEN:
                takeDamage(10);
                break;
            case MIRROR_BLUE:
                break;
        }
        dialogueIndex++;
    }

    @Override
    public void onTalk() {
        talkCount++;
        dialogueIndex++;
    }

    @Override
    public boolean isDefeated() {
        switch (type) {
            case ASSASSIN:    return hp <= 0;
            case ARCHER:      return hp <= 0;
            case PROTECTOR:   return talkCount >= 5;
            case GUARDIAN:    return talkCount >= 3 && attackCount >= 3;
            case MIRROR_RED:  return talkCount >= 2 && attackCount >= 1 && talkCount < attackCount + 3;
            case MIRROR_GREEN: return hp <= 0;
            case MIRROR_BLUE: return talkCount >= 7;
            default:          return hp <= 0;
        }
    }

    @Override
    public int getDamage() {
        switch (type) {
            case ASSASSIN:     return 20;
            case ARCHER:       return 12;
            case PROTECTOR:    return 8;
            case GUARDIAN:     return 15;
            case MIRROR_RED:   return 25;
            case MIRROR_GREEN: return 15;
            case MIRROR_BLUE:  return 10;
            default:           return 10;
        }
    }

    @Override
    public String getName() {
        switch (type) {
            case ASSASSIN:     return "Assassino";
            case ARCHER:       return "Arciere";
            case PROTECTOR:    return "Protettore";
            case GUARDIAN:     return "Il Guardiano";
            case MIRROR_RED:   return "Il Tuo Riflesso";
            case MIRROR_GREEN: return "La Tua Ombra";
            case MIRROR_BLUE:  return "La Tua Essenza";
            default:           return "Boss";
        }
    }

    @Override
    public String getDialogue() {
        switch (type) {
            case ASSASSIN:     return getAssassinDialogue();
            case ARCHER:       return getArcherDialogue();
            case PROTECTOR:    return getProtectorDialogue();
            case GUARDIAN:     return getGuardianDialogue();
            case MIRROR_RED:   return getMirrorRedDialogue();
            case MIRROR_GREEN: return getMirrorGreenDialogue();
            case MIRROR_BLUE:  return getMirrorBlueDialogue();
            default:           return "* ...";
        }
    }

    private String getAssassinDialogue() {
        String[] lines = {
            "* Finalmente qualcuno degno di combattere.",
            "* Non trattenerti. Mostrami tutto quello che hai.",
            "* Il sangue è l'unico linguaggio che capisco.",
            "* Sei più forte di quanto pensassi.",
            "* Continua... questo è quello che voglio."
        };
        return lines[Math.min(dialogueIndex, lines.length - 1)];
    }

    private String getArcherDialogue() {
        String[] lines = {
            "* Sei entrato nel mio territorio.",
            "* La mia mira non sbaglia mai.",
            "* Interessante... sai muoverti.",
            "* Forse sei degno di passare."
        };
        return lines[Math.min(dialogueIndex, lines.length - 1)];
    }

    private String getProtectorDialogue() {
        String[] lines = {
            "* Perché vuoi combattere?",
            "* Non c'è bisogno di violenza qui.",
            "* Ho protetto queste anime per secoli.",
            "* Parliamo invece di combattere.",
            "* Forse... hai ragione. Puoi passare."
        };
        return lines[Math.min(dialogueIndex, lines.length - 1)];
    }

    private String getGuardianDialogue() {
        String[] lines = {
            "* Nessuno passa senza dimostrare il proprio valore.",
            "* Mostrami che sai sia combattere che parlare.",
            "* Il vero guerriero sa quando fermarsi.",
            "* Stai imparando... continua.",
            "* Siete pronti. Passate."
        };
        return lines[Math.min(dialogueIndex, lines.length - 1)];
    }

    private String getMirrorRedDialogue() {
        String[] lines = {
            "* Guardami. Sono quello che sei diventato.",
            "* Così tanta violenza... ti ha cambiato.",
            "* Forse potresti... risparmiare qualcuno.",
            "* Abbassa la guardia. Parliamo.",
            "* Ecco la tua fine."
        };
        return lines[Math.min(dialogueIndex, lines.length - 1)];
    }

    private String getMirrorGreenDialogue() {
        String[] lines = {
            "* Mi conosci. Sono te.",
            "* Le tue scelte ti hanno portato qui.",
            "* Non c'è niente da dire. Solo da fare.",
            "* Combatti come sai fare."
        };
        return lines[Math.min(dialogueIndex, lines.length - 1)];
    }

    private String getMirrorBlueDialogue() {
        String[] lines = {
            "* Fermati. Ascoltami.",
            "* Tutta questa rabbia... da dove viene?",
            "* Non puoi combattermi. Non funzionerà.",
            "* Ricordi chi eri prima del dungeon?",
            "* Le tue parole mi raggiungono...",
            "* Continua a parlare. Sento la tua voce.",
            "* ... Mi stai raggiungendo. Sono ancora qui."
        };
        return lines[Math.min(dialogueIndex, lines.length - 1)];
    }

    public BossType getType() { return type; }
}
