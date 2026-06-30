package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.CrystalType;
import io.github.crystals_of_the_soul.model.boss.GuardianBoss;
import io.github.crystals_of_the_soul.model.boss.MirrorBlueBoss;
import io.github.crystals_of_the_soul.model.boss.MirrorGreenBoss;
import io.github.crystals_of_the_soul.model.boss.MirrorRedBoss;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test del comportamento dei boss: ogni tipo ha una condizione di sconfitta diversa,
 * non sempre basata sugli HP. Qui si verifica che le condizioni scattino al momento giusto.
 */
class BossBehaviorTest {

    // --- GuardianBoss: la condizione dipende dal cristallo ---

    @Test
    void guardianRedIsDefeatedAfterFiveAttacks() {
        GuardianBoss boss = new GuardianBoss(0, 0, CrystalType.RED);

        for (int i = 0; i < 4; i++) {
            boss.onAttack();
            assertFalse(boss.isDefeated(), "Non ancora sconfitto dopo " + (i + 1) + " attacchi");
        }
        boss.onAttack();
        assertTrue(boss.isDefeated(), "Sconfitto al quinto attacco");
    }

    @Test
    void guardianGreenCanBeSparedByTalking() {
        GuardianBoss boss = new GuardianBoss(0, 0, CrystalType.GREEN);

        // Il cristallo verde può risparmiare parlando: 5 dialoghi.
        for (int i = 0; i < 4; i++) {
            boss.onTalk();
            assertFalse(boss.isDefeated());
        }
        boss.onTalk();
        assertTrue(boss.isDefeated(), "Risparmiato al quinto dialogo");
    }

    @Test
    void guardianGreenCanBeKilledByAttacking() {
        GuardianBoss boss = new GuardianBoss(0, 0, CrystalType.GREEN);

        // ...oppure ucciderlo combattendo: 5 attacchi.
        for (int i = 0; i < 4; i++) {
            boss.onAttack();
            assertFalse(boss.isDefeated());
        }
        boss.onAttack();
        assertTrue(boss.isDefeated(), "Ucciso al quinto attacco");
    }

    @Test
    void guardianBlueIsDefeatedAfterFiveTalks() {
        GuardianBoss boss = new GuardianBoss(0, 0, CrystalType.BLUE);

        for (int i = 0; i < 4; i++) {
            boss.onTalk();
            assertFalse(boss.isDefeated());
        }
        boss.onTalk();
        assertTrue(boss.isDefeated());
    }

    @Test
    void guardianBlueIgnoresAttacks() {
        GuardianBoss boss = new GuardianBoss(0, 0, CrystalType.BLUE);

        for (int i = 0; i < 20; i++) {
            boss.onAttack();
        }
        assertFalse(boss.isDefeated(), "Il Guardiano blu si sconfigge solo parlando");
    }

    // --- MirrorRedBoss: solo il dialogo lo sconfigge (7 dialoghi) ---

    @Test
    void mirrorRedIsDefeatedAfterSevenTalks() {
        MirrorRedBoss boss = new MirrorRedBoss(0, 0);

        for (int i = 0; i < 6; i++) {
            boss.onTalk();
            assertFalse(boss.isDefeated());
        }
        boss.onTalk();
        assertTrue(boss.isDefeated());
    }

    @Test
    void mirrorRedCannotBeDefeatedByAttacks() {
        MirrorRedBoss boss = new MirrorRedBoss(0, 0);

        for (int i = 0; i < 50; i++) {
            boss.onAttack();
        }
        assertFalse(boss.isDefeated());
        assertTrue(boss.getHp() > 0, "Gli attacchi infliggono solo 1 di danno su 999 HP");
    }

    // --- MirrorBlueBoss: va sconfitto con un singolo attacco che svuota gli HP ---

    @Test
    void mirrorBlueIsDefeatedBySingleAttack() {
        MirrorBlueBoss boss = new MirrorBlueBoss(0, 0);

        assertFalse(boss.isDefeated());
        boss.onAttack();

        assertTrue(boss.isDefeated());
        assertTrue(boss.getHp() <= 0);
    }

    @Test
    void mirrorBlueEntersTalkFinalPhaseAfterFourTalks() {
        MirrorBlueBoss boss = new MirrorBlueBoss(0, 0);

        boss.onTalk();
        boss.onTalk();
        boss.onTalk();
        assertFalse(boss.isTalkFinalPhase());

        boss.onTalk();
        assertTrue(boss.isTalkFinalPhase());
    }

    // --- MirrorGreenBoss: classica deplezione degli HP (180 HP, 10 a colpo) ---

    @Test
    void mirrorGreenIsDefeatedWhenHpDepleted() {
        MirrorGreenBoss boss = new MirrorGreenBoss(0, 0);

        for (int i = 0; i < 17; i++) {
            boss.onAttack();
            assertFalse(boss.isDefeated(), "Ancora vivo dopo " + (i + 1) + " attacchi");
        }
        boss.onAttack(); // 18° attacco: 180 HP esauriti
        assertTrue(boss.isDefeated());
    }

    @Test
    void mirrorGreenCanBeSparedByTalking() {
        MirrorGreenBoss boss = new MirrorGreenBoss(0, 0);

        for (int i = 0; i < 3; i++) {
            boss.onTalk();
            assertFalse(boss.isDefeated());
        }
        boss.onTalk(); // 4° dialogo: risparmiato
        assertTrue(boss.isDefeated());
    }

    // --- Comportamento di base condiviso da Boss ---

    @Test
    void dialogueAdvancesAndClampsToLastLine() {
        GuardianBoss boss = new GuardianBoss(0, 0, CrystalType.BLUE);

        String first = boss.getDialogue();
        assertNotNull(first);

        // Molti più dialoghi delle battute disponibili: non deve andare fuori indice.
        for (int i = 0; i < 50; i++) {
            boss.onTalk();
        }
        String last = boss.getDialogue();
        assertNotNull(last);
        // L'ultima battuta resta stabile anche continuando a parlare.
        boss.onTalk();
        assertTrue(last.equals(boss.getDialogue()));
    }

    @Test
    void defaultTalkFinalPhaseIsFalse() {
        GuardianBoss boss = new GuardianBoss(0, 0, CrystalType.RED);
        assertFalse(boss.isTalkFinalPhase());
    }
}
