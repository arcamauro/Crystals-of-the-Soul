package io.github.crystals_of_the_soul.view;

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

    // Estensioni immagine supportate, provate in quest'ordine.
    private static final String[] EXTENSIONS = {".png", ".jpg", ".jpeg"};
    // Numero massimo di frame numerati cercati per direzione (<dir>1..<dir>N).
    private static final int MAX_NUMBERED_FRAMES = 16;

    public static AnimatedSprite fromFolder(String basePath) {
        AnimatedSprite s = new AnimatedSprite();
        // Le cartelle direzione sono in minuscolo; teniamo alcune varianti per robustezza.
        s.animDown = s.loadDirection(basePath, new String[]{"sotto", "down", "south"});
        s.animLeft = s.loadDirection(basePath, new String[]{"sinistra", "left", "west"});
        s.animRight = s.loadDirection(basePath, new String[]{"destra", "right", "east"});
        s.animUp = s.loadDirection(basePath, new String[]{"sopra", "up", "north"});

        // still: carica per nome esplicito (funziona anche dentro un JAR).
        FileHandle stillHandle = firstExisting(basePath + "/still", basePath + "/STILL");
        if (stillHandle != null) {
            Texture t = new Texture(stillHandle);
            s.textures.add(t);
            s.still = new TextureRegion(t);
        }

        // Se non abbiamo caricato alcuna texture, ritorniamo null per indicare assenza
        if (s.textures.size == 0) return null;

        return s;
    }

    /**
     * Carica l'animazione di una direzione senza elencare la cartella (FileHandle.list()
     * non funziona per risorse impacchettate in un JAR). I frame vengono cercati per nome
     * esplicito: "<dir>/<dir>.png" seguito da "<dir>/<dir>1.png", "<dir>/<dir>2.png", ...
     */
    private Animation<TextureRegion> loadDirection(String basePath, String[] dirNames) {
        for (String dir : dirNames) {
            String stem = basePath + "/" + dir + "/" + dir;
            Array<TextureRegion> frames = new Array<>();
            // frame base senza numero (es. "destra.png")
            addFrameIfExists(frames, stem);
            // frame numerati contigui (es. "destra1.png", "destra2.png", ...)
            for (int i = 1; i <= MAX_NUMBERED_FRAMES; i++) {
                if (!addFrameIfExists(frames, stem + i)) break;
            }
            if (frames.size > 0) {
                return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
            }
        }
        return null;
    }

    /** Aggiunge un frame se esiste un file con lo stem dato e una delle estensioni supportate. */
    private boolean addFrameIfExists(Array<TextureRegion> frames, String stem) {
        FileHandle fh = firstExisting(stem);
        if (fh == null) return false;
        Texture t = new Texture(fh);
        textures.add(t);
        frames.add(new TextureRegion(t));
        return true;
    }

    /** Restituisce il primo file esistente tra gli stem forniti, provando ogni estensione. */
    private static FileHandle firstExisting(String... stems) {
        for (String stem : stems) {
            for (String ext : EXTENSIONS) {
                FileHandle fh = Gdx.files.internal(stem + ext);
                if (fh.exists()) return fh;
            }
        }
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
