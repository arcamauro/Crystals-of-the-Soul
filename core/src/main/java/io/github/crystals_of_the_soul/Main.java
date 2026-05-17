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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    /*
    * combattimento
    * */
    private boolean inBattle = false;
    private boolean playerTurn = true;
    private boolean enemyHasAttacked = false;
    //prova
    Player player;
    InputHandler input;
    ShapeRenderer shape;

    Array<Enemy> enemies;
    Enemy currentEnemy;

    Rectangle attackButton;
    Rectangle talkButton;
    BitmapFont font;
   // SpriteBatch batch;

    //fine prova
    @Override
    public void create() {
        player = new Player(100, 100);
        input = new InputHandler();
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        enemies = new Array<>();

        enemies.add(new Enemy(300, 200));
        enemies.add(new Enemy(500, 300));
        enemies.add(new Enemy(700, 150));
        attackButton = new Rectangle(100, 50, 150, 60);
        talkButton = new Rectangle(300, 50, 150, 60);
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render() {

        if (inBattle) {
            renderBattle();
        } else {
            renderWorld();
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        image.dispose();
    }

    private void renderWorld()
    {

        float delta = Gdx.graphics.getDeltaTime();

        float dx = input.getDx();
        float dy = input.getDy();

        Vector2 dir = new Vector2(dx, dy);
        if (dir.len() > 0) dir.nor();

        player.update(dir.x, dir.y, delta);

        for (Enemy enemy : enemies) {

            float distance = Vector2.dst(
                player.getX(),
                player.getY(),
                enemy.getX(),
                enemy.getY()
            );

            if (distance < 50) {

                System.out.println("Premi E per interagire");

                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {

                    currentEnemy = enemy;

                    inBattle = true;
                }
            }
        }

        // pulizia schermo
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // disegna player e nemici
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(player.getX(), player.getY(), 32, 32); // quadrato

        for (Enemy enemy : enemies) {

            shape.rect(enemy.getX(), enemy.getY(), 32, 32);
        }
        shape.end();

    }
    private void renderBattle() {

        Gdx.gl.glClearColor(0.2f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // player
        shape.rect(100, 200, 64, 64);

        // nemico
        shape.rect(500, 200, 64, 64);

        // bottone ATTACCA
        shape.rect(
            attackButton.x,
            attackButton.y,
            attackButton.width,
            attackButton.height
        );

// bottone PARLA
        shape.rect(
            talkButton.x,
            talkButton.y,
            talkButton.width,
            talkButton.height
        );

        shape.end();

        batch.begin();

        font.draw(batch, "ATTACCA", 130, 85);
        font.draw(batch, "PARLA", 340, 85);

        batch.end();
        if (Gdx.input.justTouched()) {

            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // ATTACCA
            if (attackButton.contains(mouseX, mouseY)) {

                if (playerTurn) {

                    currentEnemy.takeDamage(10);

                    System.out.println("Enemy HP: " + currentEnemy.getHp());

                    if (currentEnemy.getHp() <= 0) {

                        enemies.removeValue(currentEnemy, true);

                        currentEnemy = null;

                        inBattle = false;

                        playerTurn = true;

                        System.out.println("Nemico sconfitto!");
                    }
                    endPlayerTurn();
                }

            }

            // PARLA
            if (talkButton.contains(mouseX, mouseY)) {

                System.out.println("PARLA!");
                endPlayerTurn();
            }
        }
        if (!playerTurn && !enemyHasAttacked) {

            player.takeDamage(5);

            System.out.println("Player HP: " + player.getHp());

            enemyHasAttacked = true;

            playerTurn = true;
        }
    }

    private void endPlayerTurn() {

        playerTurn = false;
        enemyHasAttacked = false;
    }
}
