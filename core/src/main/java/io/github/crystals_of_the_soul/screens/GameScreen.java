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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.entity.Enemy;
import io.github.crystals_of_the_soul.input.InputHandler;
import io.github.crystals_of_the_soul.player.Player;
import io.github.crystals_of_the_soul.save.SaveManager;
import io.github.crystals_of_the_soul.states.GameState;

public class GameScreen implements Screen {

    private final Main game;
    private final AssetManager assets;
    private GameState state;

    // Map
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    // Player
    private Player player;
    private InputHandler inputHandler;
    private ShapeRenderer shapeRenderer;

    // Enemies
    private Array<Enemy> enemies;
    private Enemy currentEnemy;

    // Battle state
    private boolean inBattle = false;
    private boolean playerTurn = true;
    private boolean enemyHasAttacked = false;

    // UI
    private Stage stage;
    private Skin skin;
    private Table pauseTable;
    private Table battleTable;
    private Table hudTable;
    private Label enemyHpLabel;
    private Label playerHpLabel;
    private Label interactLabel;
    private Label saveConfirmLabel;
    private boolean paused = false;

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

        map = assets.get(state.getCurrentMapPath(), TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        player = new Player(
            state.getPlayer1().x != 0 ? state.getPlayer1().x : 100,
            state.getPlayer1().y != 0 ? state.getPlayer1().y : 100
        );
        inputHandler = new InputHandler();
        shapeRenderer = new ShapeRenderer();

        enemies = new Array<>();
        enemies.add(new Enemy(300, 200));
        enemies.add(new Enemy(500, 300));
        enemies.add(new Enemy(200, 400));

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        buildPauseMenu();
        buildBattleUI();
        buildHud();

        stage.addActor(pauseTable);
        stage.addActor(battleTable);
        stage.addActor(hudTable);

        pauseTable.setVisible(false);
        battleTable.setVisible(false);
        interactLabel.setVisible(false);
    }

    private void buildHud() {
        hudTable = new Table();
        hudTable.setFillParent(true);
        hudTable.top().left().pad(10);

        playerHpLabel = new Label("HP: " + player.getHp(), skin);
        interactLabel = new Label("Premi E per interagire", skin);

        hudTable.add(playerHpLabel).row();
        hudTable.add(interactLabel);
    }

    private void buildBattleUI() {
        battleTable = new Table();
        battleTable.setFillParent(true);
        battleTable.center();

        enemyHpLabel = new Label("Nemico HP: 50", skin);

        TextButton attackBtn = new TextButton("Attacca", skin);
        attackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerTurn && inBattle) {
                    onAttack();
                }
            }
        });

        TextButton talkBtn = new TextButton("Parla", skin);
        talkBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerTurn && inBattle) {
                    onTalk();
                }
            }
        });

        battleTable.add(enemyHpLabel).padBottom(40).row();
        battleTable.add(attackBtn).width(200).height(50).padBottom(20).row();
        battleTable.add(talkBtn).width(200).height(50);
    }

    /**
     * Gestisce l'attacco del giocatore al nemico.
     * Se il nemico viene sconfitto, incrementa il contatore delle uccisioni.
     */
    private void onAttack() {
        currentEnemy.takeDamage(10);
        enemyHpLabel.setText("Nemico HP: " + currentEnemy.getHp());
        Gdx.app.log("Battle", "Enemy HP: " + currentEnemy.getHp());

        if (currentEnemy.getHp() <= 0) {
            state.killCount++;
            Gdx.app.log("Battle", "Enemy killed. Total kills: " + state.killCount);
            enemies.removeValue(currentEnemy, true);
            exitBattle();
        } else {
            endPlayerTurn();
        }
    }

    /**
     * funzione che gestisce il risparmio tramite dialogo
     * Se il nemico viene risparmiato, incrementa il contatore dei risparmi
    */
    private void onTalk() {
        state.spareCount++;
        Gdx.app.log("Battle", "Enemy spared. Total spares: " + state.spareCount);
        enemies.removeValue(currentEnemy, true);
        exitBattle();
    }

    private void endPlayerTurn() {
        playerTurn = false;
        enemyHasAttacked = false;
    }

    private void exitBattle() {
        inBattle = false;
        currentEnemy = null;
        playerTurn = true;
        enemyHasAttacked = false;
        battleTable.setVisible(false);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        if (!paused) {
            if (inBattle) {
                renderBattle();
            } else {
                renderWorld(delta);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        stage.act(delta);
        stage.draw();
    }

    private void renderWorld(float delta) {
        state.playTime += delta;

        float dx = inputHandler.getDx();
        float dy = inputHandler.getDy();
        Vector2 dir = new Vector2(dx, dy);
        if (dir.len() > 0) dir.nor();
        player.update(dir.x, dir.y, delta);

        state.getPlayer1().x = player.getX();
        state.getPlayer1().y = player.getY();

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Player
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(player.getX(), player.getY(), 32, 32);

        // Nemici
        shapeRenderer.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            shapeRenderer.rect(enemy.getX(), enemy.getY(), 32, 32);
        }

        shapeRenderer.end();

        // HUD update della vuta
        playerHpLabel.setText("HP: " + player.getHp());

        // Controllo della prossimità ai nemici
        boolean nearEnemy = false;
        for (Enemy enemy : enemies) {
            float distance = Vector2.dst(
                player.getX(), player.getY(),
                enemy.getX(), enemy.getY()
            );
            if (distance < 60) {
                nearEnemy = true;
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    currentEnemy = enemy;
                    inBattle = true;
                    playerTurn = true;
                    enemyHasAttacked = false;
                    enemyHpLabel.setText("Nemico HP: " + currentEnemy.getHp());
                    battleTable.setVisible(true);
                    Gdx.input.setInputProcessor(stage);
                }
                break;
            }
        }
        interactLabel.setVisible(nearEnemy);


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            state.currentFloor++;
            if (state.currentFloor == 2) {
                state.killCount = 5;
                state.assignCrystal();
            }
            SaveManager.getInstance().autoSave(state);
            Gdx.app.log("DEBUG", "Floor: " + state.currentFloor);
            map = assets.get(state.getCurrentMapPath(), TiledMap.class);
            mapRenderer.getMap().dispose();
            mapRenderer = new OrthogonalTiledMapRenderer(map);
        }
    }

    private void renderBattle() {
        // Turno nemico
        if (!playerTurn && !enemyHasAttacked) {
            player.takeDamage(5);
            playerHpLabel.setText("HP: " + player.getHp());
            Gdx.app.log("Battle", "Player HP: " + player.getHp());
            enemyHasAttacked = true;
            playerTurn = true;
        }
    }

    // funzione che crea il menù di pausa
    private void buildPauseMenu() {
        pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();

        Label title = new Label("Paused", skin);
        title.setFontScale(2f);

        TextButton resumeBtn = new TextButton("Resume", skin);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        TextButton saveBtn = new TextButton("Save Game", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SaveManager.getInstance().manualSave(state);
                saveConfirmLabel.setVisible(true);
            }
        });

        TextButton menuBtn = new TextButton("Main Menu", skin);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        TextButton exitBtn = new TextButton("Exit Game", skin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        saveConfirmLabel = new Label("Game Saved!", skin);
        saveConfirmLabel.setVisible(false);

        pauseTable.add(title).padBottom(40).row();
        pauseTable.add(resumeBtn).width(200).height(50).padBottom(15).row();
        pauseTable.add(saveBtn).width(200).height(50).padBottom(15).row();
        pauseTable.add(menuBtn).width(200).height(50).padBottom(15).row();
        pauseTable.add(exitBtn).width(200).height(50).padBottom(20).row();
        pauseTable.add(saveConfirmLabel);
    }

    private void togglePause() {
        paused = !paused;
        pauseTable.setVisible(paused);
        saveConfirmLabel.setVisible(false);
        Gdx.input.setInputProcessor(paused ? stage : null);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        if (!paused) togglePause();
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        paused = false;
        pauseTable.setVisible(false);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }
}
