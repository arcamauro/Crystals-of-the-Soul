package io.github.crystals_of_the_soul.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CollisionManager {

    private final Array<Rectangle> rects = new Array<>();

    public void load(TiledMap map) {
        rects.clear();
        collectTileCollisions(map.getLayers());

        MapLayer objectLayer = map.getLayers().get("collisioni");
        if (objectLayer != null) {
            for (MapObject obj : objectLayer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Rectangle r = ((RectangleMapObject) obj).getRectangle();
                    rects.add(new Rectangle(r.x, r.y, r.width, r.height));
                }
            }
        }

        Gdx.app.log("Collision", "Loaded " + rects.size + " collision rects");
    }

    public boolean wouldCollide(float x, float y, float width, float height) {
        Rectangle probe = new Rectangle(x, y, width, height);
        for (Rectangle r : rects) {
            if (probe.overlaps(r)) return true;
        }
        return false;
    }

    public Vector2 getPlayerSpawn(TiledMap map) {
        for (String name : new String[]{"Start", "start"}) {
            MapLayer layer = map.getLayers().get(name);
            if (layer == null) continue;
            for (MapObject obj : layer.getObjects()) {
                String type = obj.getProperties().get("type", "", String.class);
                if ("startPoint".equals(type) && obj instanceof RectangleMapObject) {
                    Rectangle r = ((RectangleMapObject) obj).getRectangle();
                    return new Vector2(r.x, r.y);
                }
            }
        }
        return new Vector2(100, 100);
    }

    public Array<Vector2> getEnemySpawns(TiledMap map) {
        Array<Vector2> spawns = new Array<>();
        MapLayer layer = map.getLayers().get("enemySpawn");
        if (layer != null) {
            for (MapObject obj : layer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Rectangle r = ((RectangleMapObject) obj).getRectangle();
                    spawns.add(new Vector2(r.x, r.y));
                }
            }
        }
        if (spawns.isEmpty()) {
            spawns.add(new Vector2(300, 200));
            spawns.add(new Vector2(500, 300));
            spawns.add(new Vector2(200, 400));
        }
        return spawns;
    }

    private void collectTileCollisions(Iterable<MapLayer> layers) {
        float tw = 16, th = 16;
        for (MapLayer layer : layers) {
            if (layer instanceof MapGroupLayer) {
                collectTileCollisions(((MapGroupLayer) layer).getLayers());
            } else if (layer instanceof TiledMapTileLayer) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                for (int x = 0; x < tileLayer.getWidth(); x++) {
                    for (int y = 0; y < tileLayer.getHeight(); y++) {
                        TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                        if (cell == null) continue;
                        for (MapObject obj : cell.getTile().getObjects()) {
                            if (obj instanceof RectangleMapObject) {
                                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                                rects.add(new Rectangle(
                                    x * tw + rect.x,
                                    y * th + (th - rect.y - rect.height),
                                    rect.width,
                                    rect.height
                                ));
                            }
                        }
                    }
                }
            }
        }
    }
}
