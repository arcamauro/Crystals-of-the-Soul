package io.github.crystals_of_the_soul.view;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Utility riutilizzabile per caricare animazioni a partire da una cartella
 * contenente sottocartelle per direzioni (es. "sotto", "sinistra", "destra", "sopra")
 * e disegnare il frame corretto in base al movimento.
 */
public class AnimatedSprite {

    public enum Direction { DOWN, LEFT, RIGHT, UP }

    private Animation<TextureRegion> animDown, animLeft, animRight, animUp;
    private TextureRegion still;
    private Array<Texture> textures = new Array<>();
    private float frameDuration = 0.14f;

    private float stateTime = 0f;
    private Direction lastDirection = Direction.DOWN;

    public AnimatedSprite() {}

    public static AnimatedSprite fromFolder(String basePath) {
        AnimatedSprite s = new AnimatedSprite();
        // Proviamo nomi comuni italiani/inglesi
        s.animDown = s.loadAnimationTry(basePath, new String[]{"sotto","Sotto","down","south"});
        s.animLeft = s.loadAnimationTry(basePath, new String[]{"sinistra","Sinistra","left","west"});
        s.animRight = s.loadAnimationTry(basePath, new String[]{"destra","Destra","right","east"});
        s.animUp = s.loadAnimationTry(basePath, new String[]{"sopra","Sopra","up","north"});

        // still (case-insensitive): cerca qualsiasi file con nome "still" ignorando il case
        FileHandle baseDir = Gdx.files.internal(basePath);
        if (baseDir.exists() && baseDir.isDirectory()) {
            FileHandle[] children = baseDir.list();
            if (children != null) {
                for (FileHandle fh : children) {
                    if (fh.name().toLowerCase().startsWith("still") && (fh.name().toLowerCase().endsWith(".png") || fh.name().toLowerCase().endsWith(".jpg") || fh.name().toLowerCase().endsWith(".jpeg"))) {
                        Texture t = new Texture(fh);
                        s.textures.add(t);
                        s.still = new TextureRegion(t);
                        break;
                    }
                }
            }
        }

        // Se non abbiamo caricato alcuna texture, ritorniamo null per indicare assenza
        if (s.textures.size == 0) return null;

        return s;
    }

    private Animation<TextureRegion> loadAnimationTry(String basePath, String[] names) {
        for (String n : names) {
            // prova a trovare la directory sia con nome diretto che ignorando case
            Animation<TextureRegion> a = loadAnimation(resolveDirIgnoreCase(basePath, n));
            if (a != null) return a;
        }
        return null;
    }

    private Animation<TextureRegion> loadAnimation(FileHandle dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) return null;

        FileHandle[] files = dir.list();
        if (files == null || files.length == 0) return null;

        // Ordina per nome per avere ordine stabile
        Arrays.sort(files, Comparator.comparing(FileHandle::name));

        Array<TextureRegion> frames = new Array<>();
        for (FileHandle fh : files) {
            String name = fh.name().toLowerCase();
            if (!(name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"))) continue;
            Texture t = new Texture(fh);
            textures.add(t);
            frames.add(new TextureRegion(t));
        }

        if (frames.size == 0) return null;
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    /**
     * Risolve una directory figlia di basePath cercando il nome ignorando il case.
     * Restituisce il FileHandle della directory trovata, oppure null.
     */
    private FileHandle resolveDirIgnoreCase(String basePath, String childName) {
        FileHandle baseDir = Gdx.files.internal(basePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) return null;
        FileHandle[] children = baseDir.list();
        if (children == null) return null;
        for (FileHandle fh : children) {
            if (fh.isDirectory() && fh.name().equalsIgnoreCase(childName)) return fh;
        }
        // fallback: try direct path (useful quando basePath already includes child)
        FileHandle direct = Gdx.files.internal(basePath + "/" + childName);
        if (direct.exists() && direct.isDirectory()) return direct;
        return null;
    }

    /**
     * Aggiorna stato interno: direzione e tempo animazione.
     * @param delta delta time
     * @param dx movimento orizzontale in input (-1..1)
     * @param dy movimento verticale in input (-1..1)
     */
    public void update(float delta, float dx, float dy) {
        boolean moving = Math.abs(dx) > 0.0001f || Math.abs(dy) > 0.0001f;
        if (moving) {
            stateTime += delta;
            if (Math.abs(dx) > Math.abs(dy)) {
                lastDirection = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                lastDirection = dy > 0 ? Direction.UP : Direction.DOWN;
            }
        } else {
            stateTime = 0f;
        }
    }

    public TextureRegion getFrame() {
        Animation<TextureRegion> a = null;
        switch (lastDirection) {
            case LEFT: a = animLeft; break;
            case RIGHT: a = animRight; break;
            case UP: a = animUp; break;
            default: a = animDown; break;
        }

        if (a == null) {
            return still;
        }

        if (stateTime == 0f) {
            // fermo -> prova a mostrare primo frame o still
            if (still != null) return still;
            return a.getKeyFrame(0f);
        }
        return a.getKeyFrame(stateTime, true);
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        TextureRegion f = getFrame();
        if (f != null) batch.draw(f, x, y, width, height);
    }

    public void dispose() {
        for (Texture t : textures) {
            if (t != null) t.dispose();
        }
        textures.clear();
    }
}
