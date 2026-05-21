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

/**
 * Schermata di game principale. Gestisce il ciclo di gioco, il menu di pausa e l'interazione con lo stato del gioco.
 * 
 */
public class GameScreen implements Screen {

    private final Main game; // Reference al gioco principale per poter cambiare schermata
    @SuppressWarnings("unused")
    private final AssetManager assets; // AssetManager passato dalla schermata di caricamento, per poter accedere alle risorse caricate
    private GameState state; // Stato attuale del gioco, contiene tutte le informazioni necessarie per rappresentare la partita in corso

    private Stage stage; // Stage per gestire la UI
    private Skin skin; // Skin per lo stile dei componenti UI
    private Table pauseTable; // Table per il menu di pausa
    private boolean paused = false; // Flag per tenere traccia dello stato di pausa del gioco
    private Label saveConfirmLabel; // Label per confermare al giocatore che il gioco è stato salvato, mostrata temporaneamente dopo il salvataggio

    /**
     * Costruttore per una nuova partita. Inizializza un nuovo GameState.
     * @param game
     * @param assets
     */
    public GameScreen(Main game, AssetManager assets) {
        this.game = game;
        this.assets = assets;
        this.state = GameState.createNew();
    }

    /**
     * Costruttore per caricare una partita esistente. Riceve un GameState preesistente da visualizzare.
     * @param game
     * @param assets
     * @param state
     */
    public GameScreen(Main game, AssetManager assets, GameState state) {
        this.game = game;
        this.assets = assets;
        this.state = state;
    }

    /**
     * Inizializza la schermata di gioco.
     * Crea un nuovo Stage e carica la skin per la UI. Costruisce il menu di pausa e lo aggiunge alla scena, inizialmente nascosto.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        buildPauseMenu();
        stage.addActor(pauseTable);
        pauseTable.setVisible(false);
    }

    //menu solo temporaneo per testare le funzionalità di pausa e salvataggio, da rifinire in futuro
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

    /**
     * Toggle dello stato di pausa del gioco.
     * Quando attivo, mostra il menu di pausa e imposta l'input processor per la UI. 
     * Quando disattivo, nasconde il menu e disabilita l'input processor.
     */
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


    /**
     * Ciclo di rendering principale. 
     * Aggiorna il tempo di gioco se non in pausa, gestisce l'input per il debug e il toggle di pausa, e disegna la scena.
     * @param delta
     */
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
            SaveManager.getInstance().autoSave(state);
            Gdx.app.log("DEBUG", "Floor: " + state.currentFloor);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

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

    /**
     * Gestisce la logica di pausa quando la schermata viene messa in pausa o ripresa. 
     * Se la schermata viene messa in pausa, attiva il menu di pausa. Se viene ripresa, disattiva il menu.
     */
    @Override
    public void pause() {
        if (!paused) togglePause();
    }

    /**
     * Chiamato quando l'applicazione riacquista il focus del sistema operativo.
     * Non riprende automaticamente il gioco. il giocatore deve premere Resume manualmente per evitare riprese inattese.
     */
    @Override
    public void resume() {
        // intenzionalmente vuoto
    }

    /**
     * Nasconde la schermata. Disabilita l'input processor e nasconde il menu di pausa.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        paused = false;
        pauseTable.setVisible(false);
    }

    /**
     * Libera le risorse utilizzate dalla schermata.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}