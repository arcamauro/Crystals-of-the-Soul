package io.github.crystals_of_the_soul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.battle.BattleManager;
import io.github.crystals_of_the_soul.collision.CollisionManager;
import io.github.crystals_of_the_soul.entity.Boss;
import io.github.crystals_of_the_soul.entity.Enemy;
import io.github.crystals_of_the_soul.entity.NormalEnemy;
import io.github.crystals_of_the_soul.input.InputHandler;
import io.github.crystals_of_the_soul.player.Player;
import io.github.crystals_of_the_soul.save.SaveManager;
import io.github.crystals_of_the_soul.states.GameState;

public class GameScreen implements Screen, BattleManager.Listener, GameHud.Callbacks {

    private final Main game;
    private final AssetManager assets;
    private GameState state;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Player player;
    private InputHandler inputHandler;
    private Array<Enemy> enemies;

    private CollisionManager collisionManager;
    private BattleManager battleManager;
    private GameHud hud;

    public GameScreen(Main game, AssetManager assets) {
        this.game = game;
        this.assets = assets;
        this.state = GameState.createNew();
    }

    public GameScreen(Main game, AssetManager assets, GameState state) {
        this.game = game;
        this.assets = assets;
        this.state = state;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler();

        collisionManager = new CollisionManager();
        map = assets.get(state.getCurrentMapPath(), TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionManager.load(map);

        Vector2 spawn = collisionManager.getPlayerSpawn(map);
        player = new Player(
            state.getPlayer1().x != 0 ? state.getPlayer1().x : spawn.x,
            state.getPlayer1().y != 0 ? state.getPlayer1().y : spawn.y
        );

        enemies = new Array<>();
        for (Vector2 s : collisionManager.getEnemySpawns(map)) {
            enemies.add(new NormalEnemy(s.x, s.y));
        }

        battleManager = new BattleManager(this);
        hud = new GameHud(player.getHp(), this);
    }

    // -------------------------------------------------------------------------
    // Screen lifecycle
    // -------------------------------------------------------------------------

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        if (!hud.isPaused()) {
            if (battleManager.isInBattle()) {
                renderBattle();
            } else {
                renderWorld(delta);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        hud.act(delta);
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {
        if (!hud.isPaused()) togglePause();
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        hud.resetPause();
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        shapeRenderer.dispose();
        hud.dispose();
    }

    // -------------------------------------------------------------------------
    // Rendering
    // -------------------------------------------------------------------------

    private void renderWorld(float delta) {
        state.playTime += delta;

        float dx = inputHandler.getDx();
        float dy = inputHandler.getDy();
        Vector2 dir = new Vector2(dx, dy);
        if (dir.len() > 0) dir.nor();

        float newX = player.getX() + dir.x * 200 * delta;
        float newY = player.getY() + dir.y * 200 * delta;
        if (!collisionManager.wouldCollide(newX, player.getY(), 32, 32)) player.update(dir.x, 0, delta);
        if (!collisionManager.wouldCollide(player.getX(), newY, 32, 32)) player.update(0, dir.y, delta);

        state.getPlayer1().x = player.getX();
        state.getPlayer1().y = player.getY();

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(player.getX(), player.getY(), 32, 32);
        shapeRenderer.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            shapeRenderer.rect(enemy.getX(), enemy.getY(), 32, 32);
        }
        shapeRenderer.end();

        hud.updatePlayerHp(player.getHp());

        boolean nearEnemy = false;
        for (Enemy enemy : enemies) {
            if (Vector2.dst(player.getX(), player.getY(), enemy.getX(), enemy.getY()) < 60) {
                nearEnemy = true;
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    battleManager.startBattle(enemy);
                    Gdx.input.setInputProcessor(hud.getStage());
                }
                break;
            }
        }
        hud.setInteractVisible(nearEnemy);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            advanceFloor();
        }
    }

    private void renderBattle() {
        battleManager.tickEnemyTurn(player);
    }

    // -------------------------------------------------------------------------
    // Floor transition
    // -------------------------------------------------------------------------

    private void advanceFloor() {
        state.currentFloor++;

        if (state.currentFloor > 5) {
            state.assignCrystal();
            game.setScreen(new EndingScreen(game, state));
            return;
        }

        if (state.currentFloor == 2) {
            state.assignCrystal();
        }

        map = assets.get(state.getCurrentMapPath(), TiledMap.class);
        mapRenderer.getMap().dispose();
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionManager.load(map);

        Vector2 floorSpawn = collisionManager.getPlayerSpawn(map);
        player = new Player(floorSpawn.x, floorSpawn.y);
        state.getPlayer1().x = floorSpawn.x;
        state.getPlayer1().y = floorSpawn.y;

        enemies.clear();
        if ((state.currentFloor == 3 || state.currentFloor == 4 || state.currentFloor == 5) && state.hasCrystal()) {
            Array<Vector2> bossSpawns = collisionManager.getEnemySpawns(map);
            Vector2 bossSpawn = bossSpawns.size > 0 ? bossSpawns.first() : new Vector2(300, 300);
            enemies.add(Boss.createForFloor(state.getCrystal(), state.currentFloor, bossSpawn.x, bossSpawn.y));
        } else {
            for (Vector2 s : collisionManager.getEnemySpawns(map)) {
                enemies.add(new NormalEnemy(s.x, s.y));
            }
        }

        SaveManager.getInstance().autoSave(state);
        Gdx.app.log("DEBUG", "Floor: " + state.currentFloor);
    }

    // -------------------------------------------------------------------------
    // BattleManager.Listener
    // -------------------------------------------------------------------------

    @Override
    public void onBattleStarted(Enemy enemy) {
        hud.updateEnemyHp(enemy.getName() + " HP: " + enemy.getHp());
        hud.updateDialogue(enemy.getDialogue());
        hud.showBattle();
    }

    @Override
    public void onEnemyHpChanged(String text) {
        hud.updateEnemyHp(text);
    }

    @Override
    public void onDialogueChanged(String text) {
        hud.updateDialogue(text);
    }

    @Override
    public void onPlayerHpChanged(int hp) {
        hud.updatePlayerHp(hp);
    }

    @Override
    public void onEnemyKilled(Enemy enemy) {
        state.killCount++;
        Gdx.app.log("Battle", "Enemy killed. Total kills: " + state.killCount);
        enemies.removeValue(enemy, true);
    }

    @Override
    public void onEnemySpared(Enemy enemy) {
        state.spareCount++;
        Gdx.app.log("Battle", "Enemy spared. Total spares: " + state.spareCount);
        enemies.removeValue(enemy, true);
    }

    @Override
    public void onBossDefeated() {
        state.assignCrystal();
        game.setScreen(new EndingScreen(game, state));
    }

    @Override
    public void onPlayerDefeated() {
        Gdx.app.log("Battle", "Player defeated — returning to main menu");
        hud.hideBattle();
        Gdx.input.setInputProcessor(null);
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void onBattleExited() {
        hud.hideBattle();
        Gdx.input.setInputProcessor(null);
    }

    // -------------------------------------------------------------------------
    // GameHud.Callbacks
    // -------------------------------------------------------------------------

    @Override
    public void onAttack() {
        battleManager.onAttack();
    }

    @Override
    public void onTalk() {
        battleManager.onTalk();
    }

    @Override
    public void onResume() {
        togglePause();
    }

    @Override
    public void onSave() {
        SaveManager.getInstance().manualSave(state);
        hud.showSaveConfirm();
    }

    @Override
    public void onMainMenu() {
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void onExit() {
        Gdx.app.exit();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void togglePause() {
        hud.togglePause();
        Gdx.input.setInputProcessor(hud.isPaused() ? hud.getStage() : null);
    }
}
