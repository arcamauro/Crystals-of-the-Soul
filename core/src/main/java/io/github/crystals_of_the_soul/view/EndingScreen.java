package io.github.crystals_of_the_soul.view;

import com.badlogic.gdx.Gdx;
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
import io.github.crystals_of_the_soul.Main;
import io.github.crystals_of_the_soul.model.GameState;
import io.github.crystals_of_the_soul.model.ending.CitizenEndingStrategy;
import io.github.crystals_of_the_soul.model.ending.EndingStrategy;
import io.github.crystals_of_the_soul.model.ending.GuardianEndingStrategy;

public class EndingScreen implements Screen {

    private final Main game;
    private final GameState state;
    private final EndingStrategy ending;

    private Stage stage;
    private Skin skin;

    /**
     * Costruttore principale — il finale viene determinato automaticamente
     * dal GameState tramite cristallo e contatori morali.
     */
    public EndingScreen(Main game, GameState state) {
        this.game = game;
        this.state = state;
        this.ending = state.determineEnding();
    }

    /**
     * Costruttore usato per il finale CHOICE — il giocatore ha scelto
     * esplicitamente il proprio destino tra CITIZEN e GUARDIAN.
     */
    public EndingScreen(Main game, GameState state, EndingStrategy ending) {
        this.game = game;
        this.state = state;
        this.ending = ending;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label titleLabel = new Label(ending.getTitle(), skin);
        titleLabel.setFontScale(2f);

        Label narrativeLabel = new Label(ending.getNarrative(), skin);
        narrativeLabel.setWrap(true);

        table.add(titleLabel).padBottom(40).row();
        table.add(narrativeLabel).width(600).padBottom(40).row();

        if (ending.isInteractive()) {
            TextButton exitBtn = new TextButton("Lascia il dungeon", skin);
            exitBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new EndingScreen(game, state, new CitizenEndingStrategy()));
                }
            });

            TextButton stayBtn = new TextButton("Resta come guardiano", skin);
            stayBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new EndingScreen(game, state, new GuardianEndingStrategy()));
                }
            });

            table.add(exitBtn).width(250).height(50).padBottom(15).row();
            table.add(stayBtn).width(250).height(50).row();
        } else {
            TextButton menuBtn = new TextButton("Torna al Menu", skin);
            menuBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new MainMenuScreen(game));
                }
            });
            table.add(menuBtn).width(200).height(50).row();
        }

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
