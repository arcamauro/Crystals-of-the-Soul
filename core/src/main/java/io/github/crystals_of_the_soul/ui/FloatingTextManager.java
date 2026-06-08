package io.github.crystals_of_the_soul.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FloatingTextManager {

    private String text = "";
    private float timer = 0;
    private float x;
    private float y;

    public void show(String text, float x, float y) {

        this.text = text;
        this.x = x;
        this.y = y;
        this.timer = 2f;
    }

    public void update() {

        if (timer > 0) {

            timer -= Gdx.graphics.getDeltaTime();

            y += 20 * Gdx.graphics.getDeltaTime();
        }
    }

    public void render(SpriteBatch batch, BitmapFont font) {

        if (timer <= 0)
            return;

        font.draw(batch, text, x, y);
    }
}
