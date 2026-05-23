package io.github.crystals_of_the_soul;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.crystals_of_the_soul.screens.MainMenuScreen;

public class Main extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
