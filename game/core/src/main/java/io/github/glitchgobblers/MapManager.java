package io.github.glitchgobblers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Manages the tile map of the game.
 * Responsible for its loading, rendering and collision detection with the player.
 */
public class MapManager {
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;
  private final OrthographicCamera camera;
  private TiledMapTileLayer collisionsLayer;

  public MapManager(OrthographicCamera camera) {
    this.camera = camera; // for rendering on the screen
  }

  /**
   * Loads the tilemap and defines the collision layer.
   *
   * @param mapName internal path of the map file
   */
  public void loadMap(String mapName) {
    map = new TmxMapLoader().load(mapName);
    renderer = new OrthogonalTiledMapRenderer(map, 1 / 48f); // 48x48 pixels per tile

    // "collisions" layer includes all tiles that cannot be walked on
    collisionsLayer = (TiledMapTileLayer) map.getLayers().get("collisions");
  }

  public void render() {
    renderer.setView(camera);
    renderer.render();
  }

  /**
   * Checks if a world coordinate is unwalkable/ causes a collision with the player.
   *
   * @param posX
   * @param posY
   * @return true if the position falls within a collision tile, false otherwise
   */
  public boolean isPositionInvalid(float posX, float posY) {
    float tileWidth = collisionsLayer.getTileWidth() * renderer.getUnitScale();
    float tileHeight = collisionsLayer.getTileHeight() * renderer.getUnitScale();

    // finds tile coordinates of a map position
    int x = (int) (posX / tileWidth);
    int y = (int) (posY / tileHeight);

    // if the cell at tile coords is null, the position is invalid
    TiledMapTileLayer.Cell cell = collisionsLayer.getCell(x, y);

    // position is invalid if it falls in an unwalkable tile
    return cell != null;
  }

  /**
   * Checks if any corner of a rectangle object falls in an invalid position.
   * Rectangle is blocked if any corners of the rectangle are invalid tiles.
   *
   * @param rect represents the hitbox of the player
   * @return true if any corner is in the collision layer
   */
  public boolean isRectValid(Rectangle rect) {
    return !isPositionInvalid(rect.x, rect.y) // bottom left corner
        && !isPositionInvalid(rect.x + rect.width, rect.y)  // bottom right
        && !isPositionInvalid(rect.x, rect.y + rect.height) // top left
        && !isPositionInvalid(rect.x + rect.width, rect.y + rect.height); // top right
  }

  public void dispose() {
    renderer.dispose();
    map.dispose();
  }

  public TiledMap getMap() {
    return map;
  }
}

