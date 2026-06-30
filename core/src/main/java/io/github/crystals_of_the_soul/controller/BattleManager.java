package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.Gdx;
import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.Player2;

public class BattleManager {

    public interface Listener {
        void onBattleStarted(Enemy enemy);
        void onEnemyHpChanged(String text);
        void onDialogueChanged(String text);
        void onPlayerHpChanged(int hp);
        void onEnemyKilled(Enemy enemy);
        void onEnemySpared(Enemy enemy);
        void onBossDefeated();
        void onPlayerDefeated();
        void onBattleExited();
    }

    private final Listener listener;

    private boolean inBattle = false;
    private boolean playerTurn = true;
    private boolean enemyHasAttacked = false;
    private Enemy currentEnemy;

    public BattleManager(Listener listener) {
        this.listener = listener;
    }

    public void startBattle(Enemy enemy) {
        currentEnemy = enemy;
        inBattle = true;
        playerTurn = true;
        enemyHasAttacked = false;
        listener.onBattleStarted(enemy);
    }

    public void onAttack() {
        onAttack(10, null);
    }

    public void onAttack(Player2 player2) {
        onAttack(10, player2);
    }

    public void onAttack(int playerDamage, Player2 player2) {
        if (!playerTurn || !inBattle) return;
        currentEnemy.onAttack(playerDamage);

        StringBuilder logMsg = new StringBuilder("* You attack the enemy!");
        if (player2 != null && player2.getBehavior() != null) {
            player2.getBehavior().onAttack(player2, currentEnemy, logMsg);
        }

        listener.onEnemyHpChanged(currentEnemy.getName() + " HP: " + currentEnemy.getHp());
        Gdx.app.log("Battle", currentEnemy.getName() + " HP: " + currentEnemy.getHp());

        if (currentEnemy.isDefeated()) {
            if (currentEnemy instanceof Boss) {
                inBattle = false;
                listener.onBossDefeated();
            } else {
                Gdx.app.log("Battle", "Enemy killed.");
                Enemy killed = currentEnemy;
                exitBattle();
                listener.onEnemyKilled(killed);
            }
        } else {
            logMsg.append("\n").append(currentEnemy.getDialogue());
            listener.onDialogueChanged(logMsg.toString());
            endPlayerTurn();
        }
    }

    public void onTalk() {
        onTalk(null);
    }

    public void onTalk(Player2 player2) {
        if (!playerTurn || !inBattle) return;
        currentEnemy.onTalk();
        Gdx.app.log("Battle", "Talk count for " + currentEnemy.getName());

        StringBuilder logMsg = new StringBuilder("* You talk to the enemy.");
        if (player2 != null && player2.getBehavior() != null) {
            player2.getBehavior().onTalk(player2, currentEnemy, logMsg);
        }

        if (currentEnemy.isDefeated()) {
            if (currentEnemy instanceof Boss) {
                inBattle = false;
                listener.onBossDefeated();
            } else {
                Gdx.app.log("Battle", "Enemy spared.");
                Enemy spared = currentEnemy;
                exitBattle();
                listener.onEnemySpared(spared);
            }
        } else {
            logMsg.append("\n").append(currentEnemy.getDialogue());
            listener.onDialogueChanged(logMsg.toString());
            endPlayerTurn();
        }
    }

    public void tickEnemyTurn(Player player) {
        tickEnemyTurn(player, null);
    }

    public void tickEnemyTurn(Player player, Player2 player2) {
        if (playerTurn || enemyHasAttacked) return;

        int initialDamage = currentEnemy.getDamage();
        StringBuilder logMsg = new StringBuilder("* " + currentEnemy.getName() + " attacks!");

        int remainingDamage = initialDamage;
        if (player2 != null && player2.getBehavior() != null) {
            remainingDamage = player2.getBehavior().onShieldDamage(player2, initialDamage, logMsg);
        }

        if (remainingDamage > 0) {
            int actualDamage = player.takeDamage(remainingDamage);
            logMsg.append("\n* You take ").append(actualDamage).append(" damage!");
        }

        int hp = player.getHp();
        listener.onPlayerHpChanged(hp);
        Gdx.app.log("Battle", "Player HP: " + hp);
        enemyHasAttacked = true;
        playerTurn = true;

        logMsg.append("\n").append(currentEnemy.getDialogue());
        listener.onDialogueChanged(logMsg.toString());

        if (hp <= 0) {
            Gdx.app.log("Battle", "Player defeated");
            inBattle = false;
            listener.onPlayerDefeated();
        }
    }

    public boolean isInBattle() { return inBattle; }
    public boolean isPlayerTurn() { return playerTurn; }
    public Enemy getCurrentEnemy() { return currentEnemy; }

    private void endPlayerTurn() {
        playerTurn = false;
        enemyHasAttacked = false;
    }

    private void exitBattle() {
        inBattle = false;
        currentEnemy = null;
        playerTurn = true;
        enemyHasAttacked = false;
        listener.onDialogueChanged("");
        listener.onBattleExited();
    }
}
