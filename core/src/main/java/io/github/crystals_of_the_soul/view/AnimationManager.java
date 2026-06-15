package io.github.crystals_of_the_soul.view;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;

/**
 * Utility centralizzato per caricare AnimatedSprite
 * Uso semplice: AnimationManager.load("Player") -> cerca in assets/sprites/Player
 */
public class AnimationManager {
    private static final Map<String, AnimatedSprite> cache = new ConcurrentHashMap<>();

    /**
     * Carica (o recupera dalla cache) un AnimatedSprite a partire da una chiave.
     * Se la chiave contiene una slash, viene usata come sottopercorso sotto "sprites/".
     * Esempi: "Player" -> "sprites/Player"  , "enemy/goblin" -> "sprites/enemy/goblin"
     */
    public static AnimatedSprite load(String key) {
        if (key == null || key.isEmpty()) return null;
        return cache.computeIfAbsent(key, k -> {
            String path = "sprites/" + k;
            Gdx.app.log("AnimationManager", "Carico animazione da: " + path);
            AnimatedSprite s = AnimatedSprite.fromFolder(path);
            if (s == null) Gdx.app.log("AnimationManager", "Nessuna risorsa trovata in: " + path);
            return s;
        });
    }

    /**
     * Dispose e rimuove dalla cache la sprite caricata per la chiave.
     */
    public static void dispose(String key) {
        AnimatedSprite s = cache.remove(key);
        if (s != null) s.dispose();
    }

    /** Dispose di tutte le AnimatedSprite in cache. */
    public static void disposeAll() {
        for (AnimatedSprite s : cache.values()) {
            if (s != null) s.dispose();
        }
        cache.clear();
    }
}
