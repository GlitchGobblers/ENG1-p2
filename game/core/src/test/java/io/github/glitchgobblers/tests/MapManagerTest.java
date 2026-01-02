package io.github.glitchgobblers.tests;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.MapManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class MapManagerTest extends GdxTestBase {

  private MapManager createMapWithLayer(TiledMapTileLayer layer, float unitScale) throws Exception {
    // build mapManager and mock the collisionsLayer and the renderer

    OrthographicCamera cam = new OrthographicCamera();
    MapManager manager = new MapManager(cam);

    OrthogonalTiledMapRenderer rendererMock = Mockito.mock(OrthogonalTiledMapRenderer.class);
    Mockito.when(rendererMock.getUnitScale()).thenReturn(unitScale);

    Field mockRenderer = MapManager.class.getDeclaredField("renderer");
    mockRenderer.setAccessible(true);
    mockRenderer.set(manager, rendererMock);

    Field mockLayer = MapManager.class.getDeclaredField("collisionsLayer");
    mockLayer.setAccessible(true);
    mockLayer.set(manager, layer);

    return manager;
  }

  @Test
  public void testCollisionDetection() throws Exception {
    //  create 48X48 tiles each tile is 3x3
    TiledMapTileLayer layer = new TiledMapTileLayer(3, 3, 48, 48);
    TiledMapTileLayer.Cell blocked = new TiledMapTileLayer.Cell();
    // mark the 1,1 tile as blocked
    layer.setCell(1, 1, blocked);

    MapManager manager = createMapWithLayer(layer, 1f / 48f);

    // tile 0,0 should be free
    assertFalse(manager.isPositionInvalid(0.2f, 0.2f),
      "area inside  the empty tile should be valid");

    // tile 1,1 should be blocked
    assertTrue(manager.isPositionInvalid(1.5f, 1.5f),
      "area inside the blocked tile should be invalid");
  }

}
