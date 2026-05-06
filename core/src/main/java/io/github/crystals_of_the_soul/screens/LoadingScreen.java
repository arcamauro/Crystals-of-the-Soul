package io.github.crystals_of_the_soul.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.states.GameState;

public class LoadingScreen implements Screen {

    private final Main game;
    private final AssetManager assets;
    private final GameState savedState; // null = new game
    private ShapeRenderer shapeRenderer;

    public LoadingScreen(Main game) {
        this(game, null);
    }

    public LoadingScreen(Main game, GameState savedState) {
        this.game = game;
        this.assets = new AssetManager();
        this.savedState = savedState;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        // assets.load("map/level1.tmx", TiledMap.class);
    }

    @Override
    public void render(float delta) {
        assets.update();
        ScreenUtils.clear(Color.BLACK);

        float progress = assets.getProgress();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(100, 50, 600 * progress, 20);
        shapeRenderer.end();

        if (assets.isFinished()) {
            if (savedState != null) {
                game.setScreen(new GameScreen(game, assets, savedState));
            } else {
                game.setScreen(new GameScreen(game, assets));
            }
        }
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void resize(int width, int height) {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}