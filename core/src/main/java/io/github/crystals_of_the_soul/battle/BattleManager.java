package io.github.crystals_of_the_soul.battle;
import com.badlogic.gdx.Gdx;
import io.github.crystals_of_the_soul.entity.Boss;
import io.github.crystals_of_the_soul.entity.Enemy;
import io.github.crystals_of_the_soul.player.Player;

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
        if (!playerTurn || !inBattle) return;
        currentEnemy.onAttack();
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
            listener.onDialogueChanged(currentEnemy.getDialogue());
            endPlayerTurn();
        }
    }

    public void onTalk() {
        if (!playerTurn || !inBattle) return;
        currentEnemy.onTalk();
        Gdx.app.log("Battle", "Talk count for " + currentEnemy.getName());

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
            listener.onDialogueChanged(currentEnemy.getDialogue());
            endPlayerTurn();
        }
    }

    public void tickEnemyTurn(Player player) {
        if (playerTurn || enemyHasAttacked) return;
        player.takeDamage(currentEnemy.getDamage());
        int hp = player.getHp();
        listener.onPlayerHpChanged(hp);
        Gdx.app.log("Battle", "Player HP: " + hp);
        enemyHasAttacked = true;
        playerTurn = true;

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
