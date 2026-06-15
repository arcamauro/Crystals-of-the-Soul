package io.github.crystals_of_the_soul.model;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.github.crystals_of_the_soul.controller.BattleManager;
import io.github.crystals_of_the_soul.controller.CollisionManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class Player2IntegrationTest {

    @Test
    void fullBattleFlowNotifiesPlayer2() {
        Player2State s = Player2State.create(Player2Class.ASSASSIN);
        s.x = 0f; s.y = 0f;
        Player2 p2 = Player2Factory.create(s);

        NormalEnemy enemy = new NormalEnemy(0,0,10); // low hp so one attack kills

        AtomicBoolean started = new AtomicBoolean(false);
        AtomicBoolean exited = new AtomicBoolean(false);
        AtomicBoolean killed = new AtomicBoolean(false);

        BattleManager.Listener listener = new BattleManager.Listener() {
            @Override
            public void onBattleStarted(Enemy e) {
                started.set(true);
                // simulate GameScreen behavior: notify Player2
                p2.notifyBattleStarted();
            }

            @Override
            public void onEnemyHpChanged(String text) {}

            @Override
            public void onDialogueChanged(String text) {}

            @Override
            public void onPlayerHpChanged(int hp) {}

            @Override
            public void onEnemyKilled(Enemy enemy) { killed.set(true); }

            @Override
            public void onEnemySpared(Enemy enemy) {}

            @Override
            public void onBossDefeated() { exited.set(true); p2.notifyBattleEnded(); }

            @Override
            public void onPlayerDefeated() { exited.set(true); p2.notifyBattleEnded(); }

            @Override
            public void onBattleExited() { exited.set(true); p2.notifyBattleEnded(); }
        };

        BattleManager bm = new BattleManager(listener);

        // Prepare path target
        p2.recordPosition(80f, 0f);
        float before = p2.getX();

        bm.startBattle(enemy);
        // After start, Player2 should have been notified and therefore not move
        p2.followPath(0.1f, new CollisionManager());
        assertEquals(before, p2.getX(), 0.0001, "Player2 should not move after battle start notification");

        // Ensure Gdx.app is set to a minimal proxy so log calls don't NPE in headless tests
        Application previousApp = Gdx.app;
        Application proxyApp = (Application) Proxy.newProxyInstance(
            Application.class.getClassLoader(),
            new Class[]{Application.class},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // no-op for all Application methods
                    Class<?> rt = method.getReturnType();
                    if (rt.equals(boolean.class)) return false;
                    if (rt.equals(int.class)) return 0;
                    if (rt.equals(long.class)) return 0L;
                    if (rt.equals(float.class)) return 0f;
                    return null;
                }
            }
        );
        Gdx.app = proxyApp;

        // Attack to kill the enemy and finish the battle
        bm.onAttack();
        // restore
        Gdx.app = previousApp;

        assertTrue(started.get(), "BattleManager should have called onBattleStarted");
        assertTrue(exited.get(), "BattleManager should have exited the battle");
        assertTrue(killed.get(), "BattleManager should have reported enemy killed");

        // After battle end notification, Player2 should move again
        float after = p2.getX();
        p2.followPath(0.1f, new CollisionManager());
        assertTrue(p2.getX() > after, "Player2 should move after battle ended notification");
    }
}
