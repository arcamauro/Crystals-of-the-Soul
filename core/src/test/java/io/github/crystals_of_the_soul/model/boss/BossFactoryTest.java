package io.github.crystals_of_the_soul.model.boss;

import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.CrystalType;
import io.github.crystals_of_the_soul.model.boss.BossFactory;
import io.github.crystals_of_the_soul.model.boss.GuardianBoss;
import io.github.crystals_of_the_soul.model.boss.MirrorBlueBoss;
import io.github.crystals_of_the_soul.model.boss.MirrorGreenBoss;
import io.github.crystals_of_the_soul.model.boss.MirrorRedBoss;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test del Factory pattern dei boss (BossFactory + factory concrete per piano/cristallo).
 * Verifica che ad ogni combinazione piano + cristallo corrisponda il boss corretto.
 */
class BossFactoryTest {

    // --- Piano 4: sempre GuardianBoss, indipendentemente dal cristallo ---

    @Test
    void floor4RedProducesGuardian() {
        Boss boss = BossFactory.forFloor(CrystalType.RED, 4).createBoss(0, 0);
        assertInstanceOf(GuardianBoss.class, boss);
    }

    @Test
    void floor4GreenProducesGuardian() {
        Boss boss = BossFactory.forFloor(CrystalType.GREEN, 4).createBoss(0, 0);
        assertInstanceOf(GuardianBoss.class, boss);
    }

    @Test
    void floor4BlueProducesGuardian() {
        Boss boss = BossFactory.forFloor(CrystalType.BLUE, 4).createBoss(0, 0);
        assertInstanceOf(GuardianBoss.class, boss);
    }

    // --- Piano 5: un boss "specchio" diverso per ogni cristallo ---

    @Test
    void floor5RedProducesMirrorBlue() {
        Boss boss = BossFactory.forFloor(CrystalType.RED, 5).createBoss(0, 0);
        assertInstanceOf(MirrorBlueBoss.class, boss);
    }

    @Test
    void floor5GreenProducesMirrorGreen() {
        Boss boss = BossFactory.forFloor(CrystalType.GREEN, 5).createBoss(0, 0);
        assertInstanceOf(MirrorGreenBoss.class, boss);
    }

    @Test
    void floor5BlueProducesMirrorRed() {
        Boss boss = BossFactory.forFloor(CrystalType.BLUE, 5).createBoss(0, 0);
        assertInstanceOf(MirrorRedBoss.class, boss);
    }

    // --- La factory posiziona il boss alle coordinate richieste ---

    @Test
    void createBossUsesGivenPosition() {
        Boss boss = BossFactory.forFloor(CrystalType.GREEN, 5).createBoss(42f, 17f);
        assertEquals(42f, boss.getX());
        assertEquals(17f, boss.getY());
    }

    // --- Piani senza boss: errore esplicito ---

    @Test
    void unsupportedFloorThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> BossFactory.forFloor(CrystalType.RED, 3));
    }
}
