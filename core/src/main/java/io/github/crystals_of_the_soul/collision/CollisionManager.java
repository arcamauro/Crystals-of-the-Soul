package io.github.crystals_of_the_soul.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CollisionManager {

    public static class Portal {
        public final Rectangle rect;
        public final String destination;
        public final String condition;
        public final String portalType;

        Portal(Rectangle rect, String destination, String condition, String portalType) {
            this.rect = rect;
            this.destination = destination;
            this.condition = condition;
            this.portalType = portalType;
        }
    }

    private final Array<Rectangle> rects = new Array<>();
    private final Array<Portal> portals = new Array<>();

    public void load(TiledMap map) {
        rects.clear();
        portals.clear();
        collectTileCollisions(map.getLayers());
        loadPortalsFromLayers(map.getLayers());

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

    public Array<Portal> getPortals() {
        return portals;
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
                Vector2 pos = getObjectPosition(obj);
                if (pos != null) return pos;
            }
        }
        return new Vector2(100, 100);
    }

    public Array<Vector2> getEnemySpawns(TiledMap map) {
        Array<Vector2> spawns = new Array<>();
        MapLayer layer = map.getLayers().get("NPC");
        if (layer != null) {
            for (MapObject obj : layer.getObjects()) {
                Vector2 pos = getObjectPosition(obj);
                if (pos != null) spawns.add(pos);
            }
        }
        return spawns;
    }

    public Vector2 getBossSpawn(TiledMap map) {
        MapLayer layer = map.getLayers().get("Boss");
        if (layer != null) {
            for (MapObject obj : layer.getObjects()) {
                Vector2 pos = getObjectPosition(obj);
                if (pos != null) return pos;
            }
        }
        return new Vector2(300, 300);
    }

    private Vector2 getObjectPosition(MapObject obj) {
        if (obj instanceof PointMapObject) {
            Vector2 p = ((PointMapObject) obj).getPoint();
            return new Vector2(p.x, p.y);
        }
        if (obj instanceof RectangleMapObject) {
            Rectangle r = ((RectangleMapObject) obj).getRectangle();
            return new Vector2(r.x, r.y);
        }
        return null;
    }

    private void loadPortalsFromLayers(Iterable<MapLayer> layers) {
        for (MapLayer layer : layers) {
            if (layer instanceof MapGroupLayer) {
                loadPortalsFromLayers(((MapGroupLayer) layer).getLayers());
                continue;
            }
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof RectangleMapObject)) continue;
                String pType = obj.getProperties().get("type", String.class);
                if (!"porta".equals(pType) && !"ponte".equals(pType) && !"portaF".equals(pType)) continue;
                Rectangle r = ((RectangleMapObject) obj).getRectangle();
                String dest = obj.getProperties().get("dest", String.class);
                if (dest == null) dest = obj.getProperties().get("destinazione", String.class);
                String condition = obj.getProperties().get("condition", String.class);
                portals.add(new Portal(new Rectangle(r.x, r.y, r.width, r.height), dest, condition, pType));
                Gdx.app.log("Portal", "Loaded portal type=" + pType + " dest=" + dest + " condition=" + condition);
            }
        }
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
