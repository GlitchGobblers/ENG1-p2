package io.github.glitchgobblers.events;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import io.github.glitchgobblers.entities.Entity;
import io.github.glitchgobblers.screens.GameScreen;

public class Event extends Entity {
  private final String interactionMessage;
  private final Texture interactionImage;
  private final Vector2 interactionPosition;
  private final Vector2 interactionSize;
  private final float scoreModifier;
  private boolean visible;
  private SpriteBatch batch;

  public Event(MapProperties properties) {
    super(new Texture((String) properties.get("interactionImagePath")), (Float) properties.get("x"), (Float) properties.get("y"), (Float) properties.get("width"), (Float) properties.get("height"), 0, true);
    interactionMessage = (String) properties.get("interactionMessage");
    String interactionImagePath = (String) properties.get("interactionImagePath");
    interactionImage = new Texture(interactionImagePath);
    interactionPosition = new Vector2((Float) properties.get("x"), (Float) properties.get("y"));
    interactionSize = new Vector2((Float) properties.get("width"), (Float) properties.get("height"));
    scoreModifier = (Float) properties.get("scoreModifier");
    if (properties.get("visible") != null) {
      visible = (Boolean) properties.get("visible");
    }
  }

  public float getScoreModifier() {
    return scoreModifier;
  }

  public void showInteractionMessage(GameScreen screen) {
    screen.spawnInteractionMessage(interactionMessage);
  }

  public void render() {
    batch.begin();
    batch.draw(interactionImage, interactionPosition.x, interactionPosition.y, interactionSize.x, interactionSize.y);
    batch.end();
  }
}