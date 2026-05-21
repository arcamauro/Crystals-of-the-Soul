package io.github.crystals_of_the_soul.screens;

import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.Gdx;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.save.SaveManager;

/**
 * Schermata del menu principale. Permette al giocatore di iniziare una nuova partita, continuare una partita salvata o uscire dal gioco.
 */
public class MainMenuScreen implements Screen {

    private final Main game; // Reference al gioco principale per poter cambiare schermata
    private Stage stage; // Stage per gestire la UI
    private Skin skin; // Skin per lo stile dei componenti UI

    /**
     * Costruttore. Riceve una reference al gioco principale per poter cambiare schermata.
     * @param game
     */
    public MainMenuScreen(Main game) {
        this.game = game;
    }

    /**
     * Inizializza la schermata del menu principale, creando i pulsanti e il layout.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("Crystals of the Soul", skin);
        title.setFontScale(2f);

        TextButton newGameBtn = new TextButton("New Game", skin);
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadingScreen(game));
            }
        });

        TextButton continueBtn = new TextButton("Continue", skin);
        continueBtn.setDisabled(!SaveManager.getInstance().anySaveExists());
        continueBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadingScreen(game, SaveManager.getInstance().loadMostRecent()));
            }
        });

        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(title).padBottom(60).row();
        table.add(newGameBtn).width(200).height(50).padBottom(20).row();
        table.add(continueBtn).width(200).height(50).padBottom(20).row();
        table.add(exitBtn).width(200).height(50);

        stage.addActor(table);
    }

    /**
     * Ciclo di rendering principale. 
     * Aggiorna la scena e disegna i componenti UI.
     * @param delta
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();
    }

    /**
     * Aggiorna la viewport quando la finestra viene ridimensionata.
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}


    /**
     * Libera le risorse utilizzate dalla schermata.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}