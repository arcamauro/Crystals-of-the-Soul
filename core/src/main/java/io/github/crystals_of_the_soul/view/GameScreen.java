package io.github.crystals_of_the_soul.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.controller.*;
import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.GameState;
import io.github.crystals_of_the_soul.model.NormalEnemy;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.SaveManager;
import io.github.crystals_of_the_soul.model.boss.BossFactory;
import io.github.crystals_of_the_soul.model.boss.MirrorBlueBoss;
import io.github.crystals_of_the_soul.model.entity.Item;

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

    private float portalCooldown = 0f;

    private InventoryManager inventoryManager;
    private ItemManager itemManager;
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
        spawnEnemies();

        battleManager = new BattleManager(this);
        hud = new GameHud(player.getHp(), this);
        initializeInventoryManager();
        itemManager = new ItemManager(
            player,
            hud
        );
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
    private void renderItems() {

        for (Item item : itemManager.getItems()) {

            if (itemManager.isNear(item)) {

                shapeRenderer.setColor(Color.YELLOW);

            } else {

                shapeRenderer.setColor(Color.GREEN);
            }

            shapeRenderer.rect(
                item.getX(),
                item.getY(),
                16,
                16
            );
        }
    }

    private void renderWorld(float delta) {

        state.playTime += delta;

        float dx = inputHandler.getDx();
        float dy = inputHandler.getDy();
        Vector2 dir = new Vector2(dx, dy);
        if (dir.len() > 0) dir.nor();

        float newX = player.getX() + dir.x * 200 * delta;
        float newY = player.getY() + dir.y * 200 * delta;
        if (!collisionManager.wouldCollide(newX, player.getY(), 16, 16)) player.update(dir.x, 0, delta);
        if (!collisionManager.wouldCollide(player.getX(), newY, 16, 16)) player.update(0, dir.y, delta);

        state.getPlayer1().x = player.getX();
        state.getPlayer1().y = player.getY();

        if (portalCooldown > 0) {
            portalCooldown = Math.max(0, portalCooldown - delta);
        } else {
            checkPortals();
        }

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(player.getX(), player.getY(), 16, 16);
        shapeRenderer.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            shapeRenderer.rect(enemy.getX(), enemy.getY(), 32, 32);
        }
        renderItems();
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
        itemManager.update();
        inventoryManager.update();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            advanceFloor();
        }
    }

    private void renderBattle() {
        battleManager.tickEnemyTurn(player);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        shapeRenderer.setProjectionMatrix(hud.getStage().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(w * 0.6f, h * 0.45f, 120, 120);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(w * 0.1f, h * 0.38f, 96, 96);

        shapeRenderer.end();
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
        //player = new Player(floorSpawn.x, floorSpawn.y);
        player.setPosition(floorSpawn.x, floorSpawn.y);
        state.getPlayer1().x = floorSpawn.x;
        state.getPlayer1().y = floorSpawn.y;

        spawnEnemies();

        SaveManager.getInstance().autoSave(state);
        Gdx.app.log("DEBUG", "Floor: " + state.currentFloor);
    }

    // -------------------------------------------------------------------------
    // BattleManager.Listener
    // -------------------------------------------------------------------------

    @Override
    public void onBattleStarted(Enemy enemy) {
        hud.updateEnemyHp(enemy.getName() + " HP: " + enemy.getHp());
        hud.updateDialogue("");

        boolean hasCrystal = state.hasCrystal();
        boolean canAttack = !hasCrystal || state.getPlayer1().canAttack;
        boolean canTalk;

        // RED crystal vs MIRROR_BLUE: talking is allowed initially to set up the deception
        boolean isRedVsMirrorBlue = enemy instanceof MirrorBlueBoss;

        if (isRedVsMirrorBlue) {
            canAttack = false;  // locked until dialogue series completes
            canTalk = true;
        } else {
            canTalk = !hasCrystal || state.getPlayer1().canTalk;
        }

        hud.setAttackEnabled(canAttack);
        hud.setTalkEnabled(canTalk);
        hud.showBattle();
    }

    @Override
    public void onEnemyHpChanged(String text) {
        hud.updateEnemyHp(text);
    }

    @Override
    public void onDialogueChanged(String text) {
        hud.updateDialogue(text);
        checkTalkFinalPhase();
    }

    private void checkTalkFinalPhase() {
        Enemy enemy = battleManager.getCurrentEnemy();
        if (enemy instanceof Boss && ((Boss) enemy).isTalkFinalPhase()) {
            hud.setTalkEnabled(false);
            hud.setAttackEnabled(true);
        }
    }

    @Override
    public void onPlayerHpChanged(int hp) {
        hud.updatePlayerHp(hp);
    }

    @Override
    public void onEnemyKilled(Enemy enemy) {
        if (state.currentFloor >= 1) {
            state.killCount++;
            Gdx.app.log("Battle", "Enemy killed. Total kills: " + state.killCount);
        }
        enemies.removeValue(enemy, true);
    }

    @Override
    public void onEnemySpared(Enemy enemy) {
        if (state.currentFloor >= 1) {
            state.spareCount++;
            Gdx.app.log("Battle", "Enemy spared. Total spares: " + state.spareCount);
        }
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

    private void initializeInventoryManager() {


        inventoryManager = new InventoryManager(
            player,
            hud,
            inputHandler
        );
    }
    private void checkPortals() {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), 16, 16);
        for (CollisionManager.Portal portal : collisionManager.getPortals()) {
            if (playerRect.overlaps(portal.rect) && canCrossPortal(portal)) {
                crossPortal(portal);
                return;
            }
        }
    }

    private boolean canCrossPortal(CollisionManager.Portal portal) {
        if (portal.condition == null) return true;
        if ("boss_sconfitto".equals(portal.condition)) return enemies.isEmpty();
        return true;
    }

    private void crossPortal(CollisionManager.Portal portal) {
        portalCooldown = 1.0f;

        if ("portaF".equals(portal.portalType)) {
            if (!state.hasCrystal()) state.assignCrystal();
            game.setScreen(new EndingScreen(game, state));
            return;
        }

        String destKey = portal.destination;
        if ("lvl3".equals(destKey) || "lvl4".equals(destKey) || "lvl5".equals(destKey)) {
            destKey = destKey + "_" + crystalSuffix();
        }
        String mapPath = "maps/" + destKey + ".tmx";
        int newFloor = floorFromMapKey(destKey);

        if (newFloor > state.currentFloor) {
            state.currentFloor = newFloor;
            if (newFloor == 2 && !state.hasCrystal()) state.assignCrystal();
        }

        map = assets.get(mapPath, TiledMap.class);
        mapRenderer.dispose();
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionManager.load(map);

        Vector2 spawnPos = collisionManager.getPlayerSpawn(map);
        player.setPosition(spawnPos.x, spawnPos.y);
        state.getPlayer1().x = spawnPos.x;
        state.getPlayer1().y = spawnPos.y;

        spawnEnemies();
        SaveManager.getInstance().autoSave(state);
        Gdx.app.log("Portal", "Crossed to " + mapPath + " (floor " + state.currentFloor + ")");
    }

    private int floorFromMapKey(String mapKey) {
        if (mapKey.startsWith("lvl5")) return 5;
        if (mapKey.startsWith("lvl4")) return 4;
        if (mapKey.startsWith("lvl3")) return 3;
        if (mapKey.startsWith("lvl2")) return 2;
        if (mapKey.startsWith("lvl1")) return 1;
        return 0;
    }

    private String crystalSuffix() {
        if (!state.hasCrystal()) return "v";
        switch (state.getCrystal()) {
            case RED:  return "r";
            case BLUE: return "b";
            default:   return "v";
        }
    }

    private void spawnEnemies() {
        enemies.clear();
        boolean isBossFloor = (state.currentFloor == 4 || state.currentFloor == 5) && state.hasCrystal();
        boolean isMiniBossFloor = state.currentFloor == 2 || state.currentFloor == 3;

        if (isBossFloor) {
            Vector2 bossSpawn = collisionManager.getBossSpawn(map);
            enemies.add(BossFactory.forFloor(state.getCrystal(), state.currentFloor).createBoss(bossSpawn.x, bossSpawn.y));
        } else if (isMiniBossFloor) {
            Vector2 bossSpawn = collisionManager.getBossSpawn(map);
            enemies.add(new NormalEnemy(bossSpawn.x, bossSpawn.y, 150));
        } else {
            for (Vector2 s : collisionManager.getEnemySpawns(map)) {
                enemies.add(new NormalEnemy(s.x, s.y));
            }
        }
    }

    private void togglePause() {
        hud.togglePause();
        Gdx.input.setInputProcessor(hud.isPaused() ? hud.getStage() : null);
    }
}
