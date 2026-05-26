package io.github.crystals_of_the_soul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.states.GameState;

public class NewGameScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;
    private TextField nameField;

    public NewGameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("New Game", skin);
        title.setFontScale(2f);

        Label prompt = new Label("Enter your name:", skin);

        nameField = new TextField("", skin);
        nameField.setMessageText("Player");
        nameField.setMaxLength(20);

        TextButton startBtn = new TextButton("Start", skin);
        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });

        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(title).padBottom(60).row();
        table.add(prompt).padBottom(10).row();
        table.add(nameField).width(300).height(50).padBottom(30).row();
        table.add(startBtn).width(200).height(50).padBottom(15).row();
        table.add(backBtn).width(200).height(50);

        stage.addActor(table);
        stage.setKeyboardFocus(nameField);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }
    }

    private void startGame() {
        String name = nameField.getText();
        GameState state = GameState.createNew(name);
        game.setScreen(new LoadingScreen(game, state));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void hide() { Gdx.input.setInputProcessor(null); }
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
