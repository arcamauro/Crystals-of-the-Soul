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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.input.InputHandler;
import io.github.crystals_of_the_soul.player.Player;
import io.github.crystals_of_the_soul.save.SaveManager;
import io.github.crystals_of_the_soul.states.GameState;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

    // UI
    private Stage stage;
    private Skin skin;
    private Table pauseTable;
    private boolean paused = false;
    private Label saveConfirmLabel;

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
        // Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Map
        map = assets.get(state.getCurrentMapPath(), TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Player — spawn at center of screen for now
        player = new Player(
            state.getPlayer1().x != 0 ? state.getPlayer1().x : 100,
            state.getPlayer1().y != 0 ? state.getPlayer1().y : 100
        );
        inputHandler = new InputHandler();
        shapeRenderer = new ShapeRenderer();

        // UI
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        buildPauseMenu();
        stage.addActor(pauseTable);
        pauseTable.setVisible(false);
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            state.playTime += delta;

            // Update player
            float dx = inputHandler.getDx();
            float dy = inputHandler.getDy();
            player.update(dx, dy, delta);

            // Sync player position back to GameState
            state.getPlayer1().x = player.getX();
            state.getPlayer1().y = player.getY();

            // Camera follows player
            camera.position.set(player.getX(), player.getY(), 0);
            camera.update();
        }

        ScreenUtils.clear(Color.BLACK);

        // Render map
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Render player as square placeholder
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(player.getX(), player.getY(), 32, 32);
        shapeRenderer.end();

        // ESC to pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        // Temporary debug
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            state.currentFloor++;
            if (state.currentFloor == 2) {
                state.killCount = 5;
                state.assignCrystal();
            }
            SaveManager.getInstance().autoSave(state);
            Gdx.app.log("DEBUG", "Floor: " + state.currentFloor);

            // Load new map for the new floor
            map = assets.get(state.getCurrentMapPath(), TiledMap.class);
            mapRenderer.getMap().dispose();
            mapRenderer = new OrthogonalTiledMapRenderer(map);
        }

        stage.act(delta);
        stage.draw();
    }

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
