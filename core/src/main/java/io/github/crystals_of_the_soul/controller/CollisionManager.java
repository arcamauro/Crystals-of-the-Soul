package io.github.crystals_of_the_soul.controller;

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
    // Optional Y offset (pixels) to apply to tile-object collision rectangles.
    // Use this to compensate for tileset/object origin mismatches from Tiled.
    private int tileObjectYOffset = 0;
    // If true, swap width/height of tile object rectangles (rotate 90 degrees). Useful
    // when tileset objects were authored rotated in the tileset to fake symmetry.
    private boolean rotateTileObjects90 = false;

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

    /**
     * Imposta un offset verticale (in pixel) applicato ai rettangoli ricavati dagli oggetti dei tile.
     * Valori positivi spostano i rettangoli verso l'alto, negativi verso il basso.
     */
    public void setTileObjectYOffset(int offset) {
        this.tileObjectYOffset = offset;
    }

    public int getTileObjectYOffset() {
        return tileObjectYOffset;
    }

    public boolean isRotateTileObjects90() {
        return rotateTileObjects90;
    }

    public void setRotateTileObjects90(boolean rotate) {
        this.rotateTileObjects90 = rotate;
    }

    public Array<Portal> getPortals() {
        return portals;
    }

    // DEBUG: restituisce i rettangoli di collisione caricati (per visualizzazione)
    public Array<Rectangle> getCollisionRects() {
        return rects;
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
                String aspetto = getStringProperty(obj, "aspetto");
                String type = getStringProperty(obj, "type");
                String typeTutorial = getStringProperty(obj, "type_tutorial");
                boolean isShop = "shop".equals(aspetto) || "shop".equals(type) || "shop".equals(typeTutorial);
                if (isShop) continue;

                Vector2 pos = getObjectPosition(obj);
                if (pos != null) spawns.add(pos);
            }
        }
        return spawns;
    }

    public Array<Vector2> getShopSpawns(TiledMap map) {
        Array<Vector2> spawns = new Array<>();
        MapLayer layer = map.getLayers().get("NPC");
        if (layer != null) {
            for (MapObject obj : layer.getObjects()) {
                String aspetto = getStringProperty(obj, "aspetto");
                String type = getStringProperty(obj, "type");
                String typeTutorial = getStringProperty(obj, "type_tutorial");
                boolean isShop = "shop".equals(aspetto) || "shop".equals(type) || "shop".equals(typeTutorial);
                if (isShop) {
                    Vector2 pos = getObjectPosition(obj);
                    if (pos != null) spawns.add(pos);
                }
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

    private String getStringProperty(MapObject obj, String key) {
        Object value = obj.getProperties().get(key);
        return value instanceof String ? (String) value : null;
    }

    private void loadPortalsFromLayers(Iterable<MapLayer> layers) {
        for (MapLayer layer : layers) {
            if (layer instanceof MapGroupLayer) {
                loadPortalsFromLayers(((MapGroupLayer) layer).getLayers());
                continue;
            }
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof RectangleMapObject)) continue;
                String pType = getStringProperty(obj, "type");
                if (!"porta".equals(pType) && !"ponte".equals(pType) && !"portaF".equals(pType)) continue;
                Rectangle r = ((RectangleMapObject) obj).getRectangle();
                String dest = getStringProperty(obj, "dest");
                if (dest == null) dest = getStringProperty(obj, "destinazione");
                String condition = getStringProperty(obj, "condition");
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
                                // Use rect.y directly. Tiled stores object coordinates relative to tile;
                                // previous inversion caused misalignment when tileset objects used top-left origin.
                                // Debug log original tile-object values for first items
                                Gdx.app.log("Collision", String.format("TileObj raw x=%.1f y=%.1f w=%.1f h=%.1f at tile(%d,%d)", rect.x, rect.y, rect.width, rect.height, x, y));

                                if (rotateTileObjects90) {
                                    // Swap width/height
                                    rects.add(new Rectangle(
                                        x * tw + rect.x,
                                        y * th + rect.y + tileObjectYOffset,
                                        rect.height,
                                        rect.width
                                    ));
                                    Gdx.app.log("Collision", "TileObj rotated 90deg applied");
                                } else {
                                    rects.add(new Rectangle(
                                        x * tw + rect.x,
                                        y * th + rect.y + tileObjectYOffset,
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
}
