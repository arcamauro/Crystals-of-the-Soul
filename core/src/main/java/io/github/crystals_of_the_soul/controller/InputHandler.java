package io.github.crystals_of_the_soul.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
public class InputHandler {

    public float getDx() {
        float dx = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx--;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx++;

        return dx;
    }

    public float getDy() {
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy++;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy--;

        return dy;
    }
    public boolean isUsePotionPressed() {

        return Gdx.input.isKeyJustPressed(Input.Keys.H);
    }

    public boolean isCheatGoldPressed() {

        return Gdx.input.isKeyJustPressed(Input.Keys.L);
    }
}
