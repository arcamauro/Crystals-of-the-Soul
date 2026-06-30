package io.github.crystals_of_the_soul.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsManager {
    private static SettingsManager instance;
    private final Preferences prefs;

    private static final String PREFS_NAME = "crystals_of_the_soul_settings";
    private static final String KEY_VSYNC = "vsync";
    private static final String KEY_FPS_LIMIT = "fps_limit";
    private static final String KEY_FULLSCREEN = "fullscreen";

    private SettingsManager() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public boolean isVsyncEnabled() {
        return prefs.getBoolean(KEY_VSYNC, true);
    }

    public void setVsyncEnabled(boolean enabled) {
        prefs.putBoolean(KEY_VSYNC, enabled);
        prefs.flush();
        Gdx.graphics.setVSync(enabled);
    }

    public int getFpsLimit() {
        // Default value: 60 FPS
        return prefs.getInteger(KEY_FPS_LIMIT, 60);
    }

    public void setFpsLimit(int limit) {
        prefs.putInteger(KEY_FPS_LIMIT, limit);
        prefs.flush();
        Gdx.graphics.setForegroundFPS(limit);
    }

    public boolean isFullscreenEnabled() {
        return prefs.getBoolean(KEY_FULLSCREEN, false);
    }

    public void setFullscreenEnabled(boolean enabled) {
        prefs.putBoolean(KEY_FULLSCREEN, enabled);
        prefs.flush();
        applyFullscreen(enabled);
    }

    public void applyFullscreen(boolean enabled) {
        if (enabled) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(1280, 720);
        }
    }

    public void applySettings() {
        Gdx.graphics.setVSync(isVsyncEnabled());
        Gdx.graphics.setForegroundFPS(getFpsLimit());
        if (prefs.contains(KEY_FULLSCREEN)) {
            applyFullscreen(isFullscreenEnabled());
        }
    }
}
