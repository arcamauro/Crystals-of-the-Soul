package io.github.crystals_of_the_soul;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.crystals_of_the_soul.screens.MainMenuScreen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.crystals_of_the_soul.input.InputHandler;
import io.github.crystals_of_the_soul.player.Player;

public class Main extends Game {

    public SpriteBatch batch;

    Player player;
    InputHandler input;
    ShapeRenderer shape;
    @Override
    public void create() {
        player = new Player(100, 100);
        input = new InputHandler();
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
        image = new Texture("libgdx.png");
    }

    @Override
    public void render() {
        /*
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();*/
        float delta = Gdx.graphics.getDeltaTime();

        float dx = input.getDx();
        float dy = input.getDy();

        Vector2 dir = new Vector2(dx, dy);
        if (dir.len() > 0) dir.nor();

        player.update(dir.x, dir.y, delta);

        // pulizia schermo
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // disegna player
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(player.getX(), player.getY(), 32, 32); // quadrato
        shape.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
