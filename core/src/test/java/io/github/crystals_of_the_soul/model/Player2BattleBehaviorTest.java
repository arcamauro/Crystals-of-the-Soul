package io.github.crystals_of_the_soul.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import io.github.crystals_of_the_soul.controller.BattleManager;
import io.github.crystals_of_the_soul.controller.CollisionManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class Player2BattleBehaviorTest {

    private Application previousApp;

    @BeforeEach
    void setUp() {
        previousApp = Gdx.app;
        Gdx.app = (Application) Proxy.newProxyInstance(
            Application.class.getClassLoader(),
            new Class[]{Application.class},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Class<?> rt = method.getReturnType();
                    if (rt.equals(boolean.class)) return false;
                    if (rt.equals(int.class)) return 0;
                    if (rt.equals(long.class)) return 0L;
                    if (rt.equals(float.class)) return 0f;
                    return null;
                }
            }
        );
    }

    @AfterEach
    void tearDown() {
        Gdx.app = previousApp;
    }

    @Test
    void assassinDoesNotMoveDuringBattle() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(100f, 0f);
        p2.notifyBattleStarted();
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertEquals(before, p2.getX(), 0.0001, "Should not move during battle");
    }

    @Test
    void protectorDoesNotMoveDuringBattle() {
        Player2State s = Player2State.create(Player2Class.PROTECTOR);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(60f, 0f);
        p2.notifyBattleStarted();
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertEquals(before, p2.getX(), 0.0001, "Protector should not move during battle");
    }

    @Test
    void archerDoesNotMoveDuringBattle() {
        Player2State s = Player2State.create(Player2Class.ARCHER);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        p2.recordPosition(80f, 0f);
        p2.notifyBattleStarted();
        float before = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertEquals(before, p2.getX(), 0.0001, "Archer should not move during battle");
    }

    @Test
    void assassinDealsExtraDamageInBattle() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        Player2 p2 = Player2Factory.create(s);
        NormalEnemy enemy = new NormalEnemy(0, 0, 100);

        BattleManager bm = new BattleManager(new DummyListener());
        bm.startBattle(enemy);
        bm.onAttack(p2);

        // Enemy HP: starts 100. Player 1 attack deals 10. Assassin follow-up deals 30.
        // Remaining = 100 - 10 - 30 = 60.
        assertEquals(60, enemy.getHp(), "Assassin should deal 30 additional damage on attack");
    }

    @Test
    void archerDealsExtraDamageAndHelpsTalkInBattle() {
        Player2State s = Player2State.create(Player2Class.ARCHER);
        Player2 p2 = Player2Factory.create(s);
        NormalEnemy enemy = new NormalEnemy(0, 0, 100);

        BattleManager bm = new BattleManager(new DummyListener());
        bm.startBattle(enemy);
        
        // Attack test
        bm.onAttack(p2);
        // Enemy HP: starts 100. Player 1 deals 10. Archer deals 25.
        // Remaining = 100 - 10 - 25 = 65.
        assertEquals(65, enemy.getHp(), "Archer should deal 25 additional damage on attack");

        // Talk test
        NormalEnemy enemy2 = new NormalEnemy(0, 0, 50);
        bm.startBattle(enemy2);
        bm.onTalk(p2);
        // talkCount is incremented by 1 (Player 1) + 1 (Archer) = 2.
        assertEquals("* Il nemico abbassa la guardia...", enemy2.getDialogue(), "Archer should double the talk progress");
    }

    @Test
    void protectorShieldsDamageInBattle() {
        Player2State s = Player2State.create(Player2Class.PROTECTOR);
        Player2 p2 = Player2Factory.create(s);
        NormalEnemy enemy = new NormalEnemy(0, 0, 50) {
            @Override
            public int getDamage() {
                return 15;
            }
        };

        Player player = new Player(0, 0);
        BattleManager bm = new BattleManager(new DummyListener());
        bm.startBattle(enemy);
        
        // On battle start, shield is regenerated to 50
        p2.notifyBattleStarted();
        assertEquals(50, p2.getState().currentShield, "Protector shield should regenerate to 50 at battle start");

        // Enemy attacks
        bm.onAttack(p2); // Ends player turn
        bm.tickEnemyTurn(player, p2);

        // 15 damage absorbed by shield. Shield left: 35. Player HP remains 100.
        assertEquals(100, player.getHp(), "Player 1 should take no damage when shielded");
        assertEquals(35, p2.getState().currentShield, "Protector shield should be reduced to 35");
        assertEquals(100, p2.getHp(), "Protector health should remain 100");

        // Test shield depletion with a heavy attack
        NormalEnemy heavyEnemy = new NormalEnemy(0, 0, 50) {
            @Override
            public int getDamage() {
                return 60; // 60 damage
            }
        };
        bm.startBattle(heavyEnemy);
        bm.onAttack(p2); // Ends player turn
        bm.tickEnemyTurn(player, p2);

        // 60 damage. Shield is 35. Remainder is 25.
        // Protector defense is 30. Actual damage: max(0, 25 - 30) = 0.
        // Protector HP stays 100, shield goes to 0. Player HP remains 100.
        assertEquals(100, player.getHp(), "Player 1 should take no damage when shielded");
        assertEquals(0, p2.getState().currentShield, "Protector shield should be depleted to 0");
        assertEquals(100, p2.getHp(), "Protector health should remain 100 due to high defense");

        // Test taking damage exceeding defense
        NormalEnemy megaEnemy = new NormalEnemy(0, 0, 50) {
            @Override
            public int getDamage() {
                return 80; // 80 damage. Shield is 0, so all 80 damage goes to Protector.
            }
        };
        bm.startBattle(megaEnemy);
        bm.onAttack(p2); // Ends player turn
        bm.tickEnemyTurn(player, p2);

        // megaEnemy deals 80. Shield is 0. Defense is 30.
        // Actual damage: max(0, 80 - 30) = 50. HP: 100 - 50 = 50.
        assertEquals(100, player.getHp(), "Player 1 should take no damage when shielded");
        assertEquals(50, p2.getHp(), "Protector health should be reduced to 50");
    }

    private static class DummyListener implements BattleManager.Listener {
        @Override public void onBattleStarted(Enemy enemy) {}
        @Override public void onEnemyHpChanged(String text) {}
        @Override public void onDialogueChanged(String text) {}
        @Override public void onPlayerHpChanged(int hp) {}
        @Override public void onEnemyKilled(Enemy enemy) {}
        @Override public void onEnemySpared(Enemy enemy) {}
        @Override public void onBossDefeated() {}
        @Override public void onPlayerDefeated() {}
        @Override public void onBattleExited() {}
    }
}
