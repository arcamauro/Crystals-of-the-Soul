package io.github.crystals_of_the_soul.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.model.GameState;

/**
 * Schermata di caricamento. Mostra una barra di progresso durante il caricamento delle risorse necessarie per il gioco.
 * Se viene fornito un GameState salvato, lo passa alla schermata di gioco una volta completato il caricamento.
 */
public class LoadingScreen implements Screen {

    private final Main game; // Reference al gioco principale per poter cambiare schermata
    private final AssetManager assets; // AssetManager per gestire il caricamento delle risorse
    private final GameState savedState; // GameState salvato da caricare, se presente
    private ShapeRenderer shapeRenderer; // ShapeRenderer per disegnare la barra di progresso

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

        assets.setLoader(TiledMap.class, new TmxMapLoader());

        assets.load("maps/lvl0.tmx", TiledMap.class);
        assets.load("maps/lvl0_1.tmx", TiledMap.class);
        assets.load("maps/lvl1_0.tmx", TiledMap.class);
        assets.load("maps/lvl1_1.tmx", TiledMap.class);
        assets.load("maps/lvl2.tmx", TiledMap.class);
        assets.load("maps/lvl3_b.tmx", TiledMap.class);
        assets.load("maps/lvl3_r.tmx", TiledMap.class);
        assets.load("maps/lvl3_v.tmx", TiledMap.class);
        assets.load("maps/lvl4_b.tmx", TiledMap.class);
        assets.load("maps/lvl4_r.tmx", TiledMap.class);
        assets.load("maps/lvl4_v.tmx", TiledMap.class);
        assets.load("maps/lvl5_b.tmx", TiledMap.class);
        assets.load("maps/lvl5_r.tmx", TiledMap.class);
        assets.load("maps/lvl5_v.tmx", TiledMap.class);
    }

    /**
     * Ciclo di rendering principale.
     * Aggiorna il progresso del caricamento e disegna la barra di progresso.
     * @param delta
     */
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

    /**
     * Libera le risorse utilizzate dalla schermata.
     */
    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
