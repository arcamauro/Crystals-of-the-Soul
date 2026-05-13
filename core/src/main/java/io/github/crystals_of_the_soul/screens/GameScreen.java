package io.github.crystals_of_the_soul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
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
import io.github.crystals_of_the_soul.save.SaveManager;
import io.github.crystals_of_the_soul.states.GameState;

public class GameScreen implements Screen {

    private final Main game;
    @SuppressWarnings("unused")
    private final AssetManager assets;
    private GameState state;

    private Stage stage;
    private Skin skin;
    private Table pauseTable;
    private boolean paused = false;
    private Label saveConfirmLabel;

    // New game
    public GameScreen(Main game, AssetManager assets) {
        this.game = game;
        this.assets = assets;
        this.state = GameState.createNew();
    }

    // Loaded game
    public GameScreen(Main game, AssetManager assets, GameState state) {
        this.game = game;
        this.assets = assets;
        this.state = state;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        buildPauseMenu();
        stage.addActor(pauseTable);
        pauseTable.setVisible(false);
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
                SaveManager.manualSave(state);
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

        if (paused) {
            Gdx.input.setInputProcessor(stage);
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            state.playTime += delta;
        }

        ScreenUtils.clear(Color.DARK_GRAY);

        // Temporary debug — remove before final submission
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            state.currentFloor++;
            if (state.currentFloor == 2) {
                state.killCount = 0;
                state.spareCount = 5;
                state.assignCrystal();
            }
            SaveManager.autoSave(state);
            Gdx.app.log("DEBUG", "Floor: " + state.currentFloor);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // OS lost focus — force pause menu open
        if (!paused) togglePause();
    }

    @Override
    public void resume() {
        // App regained focus — keep pause menu visible, let player manually resume
    }

    @Override
    public void hide() {
        // Leaving GameScreen — clean up input processor
        Gdx.input.setInputProcessor(null);
        paused = false;
        pauseTable.setVisible(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}