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
import io.github.crystals_of_the_soul.model.SettingsManager;

public class SettingsScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;

    public SettingsScreen(Main game) {
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

        Label title = new Label("Settings", skin);
        title.setFontScale(2f);

        final SettingsManager settings = SettingsManager.getInstance();

        // VSync Button
        final TextButton vsyncBtn = new TextButton("VSync: " + (settings.isVsyncEnabled() ? "ON" : "OFF"), skin);
        vsyncBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean newValue = !settings.isVsyncEnabled();
                settings.setVsyncEnabled(newValue);
                vsyncBtn.setText("VSync: " + (newValue ? "ON" : "OFF"));
            }
        });

        // FPS Limit Button
        int currentFps = settings.getFpsLimit();
        String fpsText = currentFps == 0 ? "Unlimited" : String.valueOf(currentFps);
        final TextButton fpsBtn = new TextButton("FPS Limit: " + fpsText, skin);
        fpsBtn.addListener(new ClickListener() {
            private final int[] fpsOptions = {60, 120, 240, 0};

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int currentLimit = settings.getFpsLimit();
                int nextIndex = 0;
                for (int i = 0; i < fpsOptions.length; i++) {
                    if (fpsOptions[i] == currentLimit) {
                        nextIndex = (i + 1) % fpsOptions.length;
                        break;
                    }
                }
                int nextLimit = fpsOptions[nextIndex];
                settings.setFpsLimit(nextLimit);
                String nextText = nextLimit == 0 ? "Unlimited" : String.valueOf(nextLimit);
                fpsBtn.setText("FPS Limit: " + nextText);
            }
        });

        // Fullscreen Button
        final TextButton fullscreenBtn = new TextButton("Screen Mode: " + (settings.isFullscreenEnabled() ? "FULLSCREEN" : "WINDOWED"), skin);
        fullscreenBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean newValue = !settings.isFullscreenEnabled();
                settings.setFullscreenEnabled(newValue);
                fullscreenBtn.setText("Screen Mode: " + (newValue ? "FULLSCREEN" : "WINDOWED"));
            }
        });

        // Back Button
        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.add(title).padBottom(50).row();
        table.add(vsyncBtn).width(250).height(50).padBottom(20).row();
        table.add(fpsBtn).width(250).height(50).padBottom(20).row();
        table.add(fullscreenBtn).width(250).height(50).padBottom(20).row();
        table.add(backBtn).width(200).height(50);

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
