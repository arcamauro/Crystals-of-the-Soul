package io.github.crystals_of_the_soul.controller.interactions;

import com.badlogic.gdx.math.Vector2;
import io.github.crystals_of_the_soul.controller.BattleManager;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.Player;

public class EnemyInteraction implements InteractionStrategy {

    private final Player player;
    private final Enemy enemy;
    private final BattleManager battleManager;

    public EnemyInteraction(
        Player player,
        Enemy enemy,
        BattleManager battleManager
    ) {
        this.player = player;
        this.enemy = enemy;
        this.battleManager = battleManager;
    }

    @Override
    public boolean canInteract() {
        return Vector2.dst2(
            player.getX(),
            player.getY(),
            enemy.getX(),
            enemy.getY()
        ) < 900f;
    }

    @Override
    public void interact() {

        battleManager.startBattle(enemy);
    }
}
