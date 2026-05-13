package io.github.crystals_of_the_soul;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.crystals_of_the_soul.input.InputHandler;
import io.github.crystals_of_the_soul.player.Player;
import io.github.crystals_of_the_soul.entity.Enemy;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    //prova
    Player player;
    InputHandler input;
    ShapeRenderer shape;
    Enemy enemy;

    //fine prova
    @Override
    public void create() {
        player = new Player(100, 100);
        input = new InputHandler();
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        enemy = new Enemy(300, 200);
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        float dx = input.getDx();
        float dy = input.getDy();

        Vector2 dir = new Vector2(dx, dy);
        if (dir.len() > 0) dir.nor();

        player.update(dir.x, dir.y, delta);

        float distance = Vector2.dst(
            player.getX(),
            player.getY(),
            enemy.getX(),
            enemy.getY()
        );

        if (distance < 50) {
            System.out.println("Premi E per interagire");

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                System.out.println("COMBATTIMENTO!");
            }
        }

        // pulizia schermo
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // disegna player
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(player.getX(), player.getY(), 32, 32); // quadrato
        shape.rect(enemy.getX(), enemy.getY(), 32, 32);
        shape.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
