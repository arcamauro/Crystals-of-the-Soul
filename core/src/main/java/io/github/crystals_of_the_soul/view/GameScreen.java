package io.github.crystals_of_the_soul.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.controller.BattleManager;
import io.github.crystals_of_the_soul.controller.CollisionManager;
import io.github.crystals_of_the_soul.controller.InputHandler;
import io.github.crystals_of_the_soul.controller.InventoryManager;
import io.github.crystals_of_the_soul.controller.ItemManager;
import io.github.crystals_of_the_soul.controller.interactions.EnemyInteraction;
import io.github.crystals_of_the_soul.controller.interactions.ShopInteraction;
import io.github.crystals_of_the_soul.model.Boss;
import io.github.crystals_of_the_soul.model.Enemy;
import io.github.crystals_of_the_soul.model.GameState;
import io.github.crystals_of_the_soul.model.NormalEnemy;
import io.github.crystals_of_the_soul.model.Player;
import io.github.crystals_of_the_soul.model.Player2;
import io.github.crystals_of_the_soul.model.SaveManager;
import io.github.crystals_of_the_soul.model.ShopNPC;
import io.github.crystals_of_the_soul.model.ShopNPCFactory;
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
    private com.badlogic.gdx.utils.Array<Texture> playerTextures;
    private AnimatedSprite playerSprite;
    private AnimatedSprite player2Sprite;
    private String player2SpriteKey;
    private float lastPlayer2X = 0f;
    private float lastPlayer2Y = 0f;

    // Sprite / animation for player
    private Animation<TextureRegion> walkDownAnim, walkLeftAnim, walkRightAnim, walkUpAnim;
    private TextureRegion stillRegion;
    private float animStateTime = 0f;
    private int lastDirection = 0; // 0 = down, 1 = left, 2 = right, 3 = up
    private Player player;
    private Player2 player2;
    private InputHandler inputHandler;
    private Array<Enemy> enemies;
    private Array<ShopNPC> shops;
    private Texture[] shopTextures;
    private java.util.Map<String, Texture> enemyTextures;
    private boolean inShop = false;
    private ShopNPC activeShop;

    private CollisionManager collisionManager;
    private BattleManager battleManager;
    private GameHud hud;

    private float portalCooldown = 0f;

    private float accumulator = 0f;
    private static final float TIME_STEP = 1f / 60f;

    private InventoryManager inventoryManager;
    private ItemManager itemManager;
    private final Vector2 tempDir = new Vector2();

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
        camera.zoom = 0.3f; // Zoom in (0.5f makes elements 2x larger, adjust as desired)
        shapeRenderer = new ShapeRenderer();
        inputHandler = new InputHandler();
        playerTextures = new com.badlogic.gdx.utils.Array<>();

        // Proviamo a caricare il player tramite il loader centralizzato
        playerSprite = AnimationManager.load("Player");

        // Se il loader non trova nulla, manteniamo il caricamento manuale (fallback)
        if (playerSprite == null) {
            // Carica i frame del player (path relativi alla cartella assets/)
            try {
                Texture down1 = new Texture(Gdx.files.internal("sprites/Player/sotto/sotto1.png"));
                Texture down2 = new Texture(Gdx.files.internal("sprites/Player/sotto/sotto.png"));
                Texture left1 = new Texture(Gdx.files.internal("sprites/Player/sinistra/sinistra1.png"));
                Texture left2 = new Texture(Gdx.files.internal("sprites/Player/sinistra/sinistra.png"));
                Texture right1 = new Texture(Gdx.files.internal("sprites/Player/destra/destra1.png"));
                Texture right2 = new Texture(Gdx.files.internal("sprites/Player/destra/destra.png"));
                Texture up1 = new Texture(Gdx.files.internal("sprites/Player/sopra/sopra1.png"));
                Texture up2 = new Texture(Gdx.files.internal("sprites/Player/sopra/sopra.png"));
                Texture still = new Texture(Gdx.files.internal("sprites/Player/STILL.png"));

                playerTextures.addAll(down1, down2, left1, left2, right1, right2, up1, up2, still);

                float frameDuration = 0.14f;
                walkDownAnim = new Animation<>(frameDuration, new TextureRegion(down1), new TextureRegion(down2));
                walkLeftAnim = new Animation<>(frameDuration, new TextureRegion(left1), new TextureRegion(left2));
                walkRightAnim = new Animation<>(frameDuration, new TextureRegion(right1), new TextureRegion(right2));
                walkUpAnim = new Animation<>(frameDuration, new TextureRegion(up1), new TextureRegion(up2));
                stillRegion = new TextureRegion(still);
            } catch (Exception e) {
                Gdx.app.log("Assets", "Errore caricamento sprite player: " + e.getMessage());
            }
        }
        collisionManager = new CollisionManager();
        // Ensure default collision mapping (no rotation/offset) to avoid breaking maps
        collisionManager.setRotateTileObjects90(false);
        collisionManager.setTileObjectYOffset(0);
        map = assets.get(state.getCurrentMapPath(), TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionManager.load(map);

        Vector2 spawn = collisionManager.getPlayerSpawn(map);
        player = new Player(
            state.getPlayer1(),
            state.getPlayer1().x != 0 ? state.getPlayer1().x : spawn.x,
            state.getPlayer1().y != 0 ? state.getPlayer1().y : spawn.y
        );

        // Istanzia Player2 se esiste (dal floor 2 in poi)
        if (state.getPlayer2() != null) {
            // Posiziona Player2 accanto a Player1
            player2 = null; // ensure fresh
            placePlayer2Adjacent();
        }

        enemies = new Array<>();
        shops = new Array<>();
        shopTextures = new Texture[3];
        try {
            shopTextures[0] = new Texture(Gdx.files.internal("sprites/Shop/shop.png"));
            shopTextures[1] = new Texture(Gdx.files.internal("sprites/Shop/shop1.png"));
            shopTextures[2] = new Texture(Gdx.files.internal("sprites/Shop/shop2.png"));
        } catch (Exception e) {
            Gdx.app.log("GameScreen", "Failed to load shop textures: " + e.getMessage());
        }

        enemyTextures = new java.util.HashMap<>();
        String[] enemySpriteNames = {"Orco", "Skeleton", "MiniBoss", "BossB", "BossG", "BossR"};
        for (String name : enemySpriteNames) {
            try {
                enemyTextures.put(name, new Texture(Gdx.files.internal("sprites/Enemy/" + name + ".png")));
            } catch (Exception e) {
                Gdx.app.log("GameScreen", "Failed to load enemy texture " + name + ": " + e.getMessage());
            }
        }

        spawnEnemies();

        battleManager = new BattleManager(this);
        hud = new GameHud(player.getHp(), this);
        hud.updateGold(state.gold);
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
        camera.zoom = 0.3f; // Maintain the zoom level when window is resized
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
        if (playerTextures != null) {
            for (Texture t : playerTextures) {
                if (t != null) t.dispose();
            }
        }
        if (shopTextures != null) {
            for (Texture t : shopTextures) {
                if (t != null) t.dispose();
            }
        }
        if (enemyTextures != null) {
            for (Texture t : enemyTextures.values()) {
                if (t != null) t.dispose();
            }
            enemyTextures.clear();
        }
        // Dispose del player caricato tramite AnimationManager (se presente)
        AnimationManager.dispose("Player");
        if (player2SpriteKey != null) AnimationManager.dispose(player2SpriteKey);
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
        // Accumulate elapsed frame time
        float frameTime = Math.min(delta, 0.25f); // Prevent spiral of death
        accumulator += frameTime;

        while (accumulator >= TIME_STEP) {
            state.playTime += TIME_STEP;

            float currentDx = inShop ? 0f : inputHandler.getDx();
            float currentDy = inShop ? 0f : inputHandler.getDy();
            tempDir.set(currentDx, currentDy);
            if (tempDir.len() > 0) tempDir.nor();

            // Calcola movimento proposto con TIME_STEP fisso
            float moveAmount = 200f * TIME_STEP;
            float newX = player.getX() + tempDir.x * moveAmount;

            // Verifica X
            if (!inShop && !collisionManager.wouldCollide(newX + Player.HITBOX_OFFSET, player.getY() + Player.HITBOX_OFFSET, Player.HITBOX_SIZE, Player.HITBOX_SIZE)) {
                player.update(tempDir.x, 0, TIME_STEP);
            }

            // Verifica Y (usa la nuova posizione X se è stata accettata)
            float newY = player.getY() + tempDir.y * moveAmount;
            if (!inShop && !collisionManager.wouldCollide(player.getX() + Player.HITBOX_OFFSET, newY + Player.HITBOX_OFFSET, Player.HITBOX_SIZE, Player.HITBOX_SIZE)) {
                player.update(0, tempDir.y, TIME_STEP);
            }

            state.getPlayer1().x = player.getX();
            state.getPlayer1().y = player.getY();

            // Sincronizza Player2 con movimento caterpillar
            if (player2 != null && !inShop) {
                player2.recordPosition(player.getX(), player.getY());
                player2.followPath(TIME_STEP, collisionManager);
            }

            if (!inShop) {
                if (portalCooldown > 0) {
                    portalCooldown = Math.max(0, portalCooldown - TIME_STEP);
                } else {
                    checkPortals();
                }
            }

            accumulator -= TIME_STEP;
        }

        // Camera updates and map rendering (per frame)
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        // Aggiorna lo sprite animato e lo stato animazione (a frame rate nativo per massima fluidità)
        float dx = inShop ? 0f : inputHandler.getDx();
        float dy = inShop ? 0f : inputHandler.getDy();
        if (playerSprite != null) playerSprite.update(delta, dx, dy);

        tempDir.set(dx, dy);
        if (tempDir.len() > 0) {
            animStateTime += delta;
            if (Math.abs(dx) > Math.abs(dy)) {
                lastDirection = dx > 0 ? 2 : 1;
            } else {
                lastDirection = dy > 0 ? 3 : 0;
            }
        } else {
            animStateTime = 0f;
        }

        // Scegli frame corrente (solo se non stiamo usando AnimatedSprite centralizzato)
        TextureRegion currentFrame = stillRegion;
        if (playerSprite == null && tempDir.len() > 0) {
            switch (lastDirection) {
                case 1:
                    if (walkLeftAnim != null) currentFrame = walkLeftAnim.getKeyFrame(animStateTime, true);
                    break;
                case 2:
                    if (walkRightAnim != null) currentFrame = walkRightAnim.getKeyFrame(animStateTime, true);
                    break;
                case 3:
                    if (walkUpAnim != null) currentFrame = walkUpAnim.getKeyFrame(animStateTime, true);
                    break;
                default:
                    if (walkDownAnim != null) currentFrame = walkDownAnim.getKeyFrame(animStateTime, true);
                    break;
            }
        }

        // Disegna il player con SpriteBatch condiviso
        SpriteBatch batch = game.batch;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (playerSprite != null) {
            playerSprite.draw(batch, player.getX(), player.getY(), 16f, 32f);
        } else {
            if (currentFrame != null) {
                // Disegna il personaggio alto 2 tile (16x32)
                batch.draw(currentFrame, player.getX(), player.getY(), 16f, 32f);
            }
        }
        // Disegna Player2 con AnimatedSprite se disponibile
        if (player2 != null && player2Sprite != null) {
            float p2dx = player2.getX() - lastPlayer2X;
            float p2dy = player2.getY() - lastPlayer2Y;
            player2Sprite.update(delta, p2dx, p2dy);
            player2Sprite.draw(batch, player2.getX(), player2.getY(), 16f, 32f);
            lastPlayer2X = player2.getX();
            lastPlayer2Y = player2.getY();
        }
        
        // Disegna i ShopNPC se la texture è disponibile
        for (ShopNPC shop : shops) {
            Texture tex = null;
            if (shopTextures != null && shop.getImageIndex() >= 0 && shop.getImageIndex() < shopTextures.length) {
                tex = shopTextures[shop.getImageIndex()];
            }
            if (tex != null) {
                batch.draw(tex, shop.getX(), shop.getY(), 32f, 32f);
            }
        }

        // Disegna gli Enemy se la texture è disponibile
        for (Enemy enemy : enemies) {
            Texture tex = null;
            if (enemyTextures != null && enemy.getSpriteName() != null) {
                tex = enemyTextures.get(enemy.getSpriteName());
            }
            if (tex != null) {
                batch.draw(tex, enemy.getX(), enemy.getY(), 32f, 32f);
            }
        }
        batch.end();

        // Usa ShapeRenderer per gli altri oggetti debug (Player2, nemici, items)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Renderizza Player2 se esiste
        if (player2 != null && player2Sprite == null) {
            switch (state.getPlayer2().playerClass) {
                case ASSASSIN: shapeRenderer.setColor(Color.RED); break;
                case PROTECTOR: shapeRenderer.setColor(Color.BLUE); break;
                case ARCHER: shapeRenderer.setColor(Color.GREEN); break;
                default: shapeRenderer.setColor(Color.CYAN); break;
            }
            shapeRenderer.rect(player2.getX(), player2.getY(), 16, 16);
        }

        // Se la texture dello shop non è stata caricata, usa ShapeRenderer verde
        boolean hasShopTexture = false;
        if (shopTextures != null) {
            for (Texture t : shopTextures) {
                if (t != null) hasShopTexture = true;
            }
        }
        if (!hasShopTexture) {
            shapeRenderer.setColor(Color.GREEN);
            for (ShopNPC shop : shops) {
                shapeRenderer.rect(shop.getX(), shop.getY(), 32, 32);
            }
        }

        shapeRenderer.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            boolean hasTexture = enemyTextures != null && enemy.getSpriteName() != null && enemyTextures.containsKey(enemy.getSpriteName());
            if (!hasTexture) {
                shapeRenderer.rect(enemy.getX(), enemy.getY(), 32, 32);
            }
        }
        renderItems();
        shapeRenderer.end();

        hud.updatePlayerHp(player.getHp());
        hud.updatePlayerStats(player.getAttack(), player.getDefense());
        if (player2 != null) {
            String p2Text = player2.getState().playerClass.name() + " HP: " + player2.getHp();
            if (player2.getState().playerClass == io.github.crystals_of_the_soul.model.Player2Class.PROTECTOR) {
                p2Text += " (Scudo: " + player2.getState().currentShield + ")";
            }
            hud.updatePlayer2Hp(p2Text);
        } else {
            hud.updatePlayer2Hp("");
        }

        boolean nearEnemy = false;
        boolean nearShop = false;

        // Verifica prossimità nemici
        for (Enemy enemy : enemies) {
            if (Vector2.dst2(player.getX(), player.getY(), enemy.getX(), enemy.getY()) < 900f) {
                nearEnemy = true;
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    EnemyInteraction interaction = new EnemyInteraction(player, enemy, battleManager);
                    interaction.interact();
                    Gdx.input.setInputProcessor(hud.getStage());
                }
                break;
            }
        }

        // Verifica prossimità shop (solo se non siamo già vicini a un nemico)
        if (!nearEnemy) {
            for (final ShopNPC shop : shops) {
                if (Vector2.dst2(player.getX(), player.getY(), shop.getX(), shop.getY()) < 900f) {
                    nearShop = true;
                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                        ShopInteraction interaction = new ShopInteraction(player, shop, new ShopInteraction.Listener() {
                            @Override
                            public void onShopOpened(ShopNPC s) {
                                openShop(s);
                            }
                        });
                        interaction.interact();
                    }
                    break;
                }
            }
        }

        hud.setInteractVisible(nearEnemy || nearShop);
        if (!inShop) {
            itemManager.update();
            inventoryManager.update();
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                advanceFloor();
            }
        }
    }

    private void renderBattle() {
        battleManager.tickEnemyTurn(player, player2);

        // Aggiorna HUD in tempo reale per la battaglia
        hud.updatePlayerHp(player.getHp());
        hud.updatePlayerStats(player.getAttack(), player.getDefense());
        if (player2 != null) {
            String p2Text = player2.getState().playerClass.name() + " HP: " + player2.getHp();
            if (player2.getState().playerClass == io.github.crystals_of_the_soul.model.Player2Class.PROTECTOR) {
                p2Text += " (Scudo: " + player2.getState().currentShield + ")";
            }
            hud.updatePlayer2Hp(p2Text);
        } else {
            hud.updatePlayer2Hp("");
        }

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        Enemy currentEnemy = battleManager.getCurrentEnemy();
        boolean hasEnemyTexture = false;
        Texture enemyTex = null;
        if (currentEnemy != null && enemyTextures != null && currentEnemy.getSpriteName() != null) {
            enemyTex = enemyTextures.get(currentEnemy.getSpriteName());
            if (enemyTex != null) {
                hasEnemyTexture = true;
            }
        }

        shapeRenderer.setProjectionMatrix(hud.getStage().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (!hasEnemyTexture) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(w * 0.6f, h * 0.45f, 120, 120);
        }

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(w * 0.1f, h * 0.38f, 96, 96);

        // Disegna Player 2 (colore di classe o grigio se KO)
        if (player2 != null) {
            if (player2.getHp() <= 0) {
                shapeRenderer.setColor(Color.GRAY);
            } else {
                switch (player2.getState().playerClass) {
                    case ASSASSIN:
                        shapeRenderer.setColor(Color.ORANGE);
                        break;
                    case ARCHER:
                        shapeRenderer.setColor(Color.GREEN);
                        break;
                    case PROTECTOR:
                        shapeRenderer.setColor(Color.BLUE);
                        break;
                    default:
                        shapeRenderer.setColor(Color.CYAN);
                        break;
                }
            }
            shapeRenderer.rect(w * 0.22f, h * 0.38f, 80, 80);
        }

        shapeRenderer.end();

        if (hasEnemyTexture) {
            SpriteBatch batch = game.batch;
            batch.setProjectionMatrix(hud.getStage().getCamera().combined);
            batch.begin();
            batch.draw(enemyTex, w * 0.6f, h * 0.45f, 120f, 120f);
            batch.end();
        }
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

        if (state.currentFloor == 3) {
            state.assignCrystal();
            // Crea Player2 quando il cristallo è assegnato (floor 3)
            if (state.getPlayer2() != null) {
                player2 = io.github.crystals_of_the_soul.model.Player2Factory.create(state.getPlayer2());
            }
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

        // Ricrea Player2 se esiste
        if (player2 != null && state.getPlayer2() != null) {
            // Posiziona Player2 accanto a Player1 nel nuovo floor
            player2 = null;
            placePlayer2Adjacent();
        }

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
        if (player2 != null) player2.notifyBattleStarted();
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
        state.gold += 10;
        hud.updateGold(state.gold);
        enemies.removeValue(enemy, true);
    }

    @Override
    public void onEnemySpared(Enemy enemy) {
        if (state.currentFloor >= 1) {
            state.spareCount++;
            Gdx.app.log("Battle", "Enemy spared. Total spares: " + state.spareCount);
        }
        state.gold += 10;
        hud.updateGold(state.gold);
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
        if (player2 != null) player2.notifyBattleEnded();
        hud.hideBattle();
        Gdx.input.setInputProcessor(null);
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void onBattleExited() {
        if (player2 != null) player2.notifyBattleEnded();
        hud.hideBattle();
        Gdx.input.setInputProcessor(null);
    }

    // -------------------------------------------------------------------------
    // GameHud.Callbacks
    // -------------------------------------------------------------------------

    @Override
    public void onAttack() {
        battleManager.onAttack(player.getAttack(), player2);
    }

    @Override
    public void onTalk() {
        battleManager.onTalk(player2);
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

    @Override
    public void onBuyItem(String itemName, int price) {
        System.out.println("onBuyItem called: " + itemName + ", price: " + price + ", current gold: " + state.gold);
        if (state.gold >= price) {
            state.gold -= price;
            hud.updateGold(state.gold);
            io.github.crystals_of_the_soul.model.entity.Item item = new io.github.crystals_of_the_soul.model.entity.Item(0, 0, itemName);
            player.getInventory().addItem(item);
            System.out.println("Item added. Inventory size: " + player.getInventory().getItems().size);
            
            // Execute the UseItemInteraction strategy to apply the item effect
            io.github.crystals_of_the_soul.controller.interactions.UseItemInteraction interaction =
                new io.github.crystals_of_the_soul.controller.interactions.UseItemInteraction(player, item);
            boolean canUse = interaction.canInteract();
            System.out.println("canInteract: " + canUse);
            if (canUse) {
                interaction.interact();
                System.out.println("Applied! Player Attack: " + player.getAttack() + ", Defense: " + player.getDefense());
            }
        } else {
            hud.showNotification("Oro insufficiente!");
        }
    }

    @Override
    public void onCloseShop() {
        inShop = false;
        activeShop = null;
        hud.hideShop();
        Gdx.input.setInputProcessor(null);
    }

    private int getAdjustedPrice(int basePrice) {
        if (!state.hasCrystal()) return basePrice;
        switch (state.getCrystal()) {
            case BLUE:
                return (int) Math.round(basePrice * 0.8);
            case RED:
                return (int) Math.round(basePrice * 1.2);
            case GREEN:
            default:
                return basePrice;
        }
    }

    private void openShop(ShopNPC shop) {
        activeShop = shop;
        inShop = true;
        hud.updateGold(state.gold);
        int potionPrice = getAdjustedPrice(shop.getPotionPrice());
        int swordPrice = getAdjustedPrice(40);
        int armorPrice = getAdjustedPrice(40);
        hud.showShop(shop.getName(), shop.getDialogue(), shop.getItemName(), potionPrice, swordPrice, armorPrice);
        Gdx.input.setInputProcessor(hud.getStage());
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

        // Posiziona Player2 accanto al player dopo il teletrasporto/entrata in stanza
        if (state.getPlayer2() != null) {
            player2 = null;
            placePlayer2Adjacent();
        }
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
        shops.clear();

        for (CollisionManager.ShopSpawnData s : collisionManager.getShopSpawns(map, state.getCurrentMapPath())) {
            shops.add(ShopNPCFactory.create(s.position.x, s.position.y, s.shopType));
        }

        // Carichiamo i boss/mini-boss dal layer Boss/boss
        for (CollisionManager.BossSpawnData b : collisionManager.getBossSpawns(map)) {
            if (b.isBoss) {
                if (state.hasCrystal()) {
                    enemies.add(BossFactory.forFloor(state.getCrystal(), state.currentFloor).createBoss(b.position.x, b.position.y));
                }
            } else if (b.isMiniBoss) {
                if (state.currentFloor == 4) {
                    if (state.hasCrystal()) {
                        Enemy boss = BossFactory.forFloor(state.getCrystal(), state.currentFloor).createBoss(b.position.x, b.position.y);
                        boss.setSpriteName("MiniBoss");
                        enemies.add(boss);
                    }
                } else {
                    NormalEnemy miniBoss = new NormalEnemy(b.position.x, b.position.y, 150);
                    miniBoss.setSpriteName("MiniBoss");
                    enemies.add(miniBoss);
                }
            }
        }

        // Carichiamo i nemici normali dal layer NPC
        for (CollisionManager.EnemySpawnData s : collisionManager.getEnemySpawnsWithData(map)) {
            NormalEnemy e = new NormalEnemy(s.position.x, s.position.y);
            String sprite = "Skeleton"; // default
            if ("orco".equalsIgnoreCase(s.aspetto) || "orco".equalsIgnoreCase(s.type)) {
                sprite = "Orco";
            }
            e.setSpriteName(sprite);
            enemies.add(e);
        }
    }

    /**
     * Posiziona Player2 accanto a Player1 cercando la prima posizione libera.
     */
    private void placePlayer2Adjacent() {
        if (state.getPlayer2() == null) return;

        float px = player.getX();
        float py = player.getY();
        float tile = 16f;
        float[][] offsets = new float[][]{
            {tile, 0}, {-tile, 0}, {0, tile}, {0, -tile}, {tile, tile}, {-tile, tile}, {tile, -tile}, {-tile, -tile}
        };

        for (float[] off : offsets) {
            float nx = px + off[0];
            float ny = py + off[1];
            // use same hitbox offset as collision checks
            if (!collisionManager.wouldCollide(nx + Player.HITBOX_OFFSET, ny + Player.HITBOX_OFFSET, Player.HITBOX_SIZE, Player.HITBOX_SIZE)) {
                state.getPlayer2().x = nx;
                state.getPlayer2().y = ny;
                player2 = io.github.crystals_of_the_soul.model.Player2Factory.create(state.getPlayer2());
                // Carica la sprite per Player2
                loadPlayer2Sprite();
                lastPlayer2X = player2.getX();
                lastPlayer2Y = player2.getY();
                return;
            }
        }

        // fallback: just place to the right
        state.getPlayer2().x = px + tile;
        state.getPlayer2().y = py;
        player2 = io.github.crystals_of_the_soul.model.Player2Factory.create(state.getPlayer2());
        loadPlayer2Sprite();
        lastPlayer2X = player2.getX();
        lastPlayer2Y = player2.getY();
    }

    private void loadPlayer2Sprite() {
        // dispose precedente
        if (player2SpriteKey != null) AnimationManager.dispose(player2SpriteKey);
        if (state.getPlayer2() == null) {
            player2Sprite = null;
            player2SpriteKey = null;
            return;
        }
        // mappatura semplice dalle classi a cartelle assets
        switch (state.getPlayer2().playerClass) {
            case ARCHER: player2SpriteKey = "Archer"; break;
            case PROTECTOR: player2SpriteKey = "Tank"; break;
            case ASSASSIN: player2SpriteKey = "Assassin"; break;
            default: player2SpriteKey = "Player"; break;
        }
        player2Sprite = AnimationManager.load(player2SpriteKey);
        if (player2Sprite == null) {
            Gdx.app.log("AnimationManager", "Player2 sprite not found for key: " + player2SpriteKey);
        }
    }

    private void togglePause() {
        hud.togglePause();
        Gdx.input.setInputProcessor(hud.isPaused() ? hud.getStage() : null);
    }
}
