package io.github.glitchgobblers.events;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.entities.Item;
import io.github.glitchgobblers.entities.Player;
import io.github.glitchgobblers.screens.GameScreen;
import io.github.glitchgobblers.screens.WinScreen;

public class Event extends Item {
  private int counter = 0;
  private final String eventId;

  private final String interactionMessage;
  private final Texture interactionImage;
  private final Vector2 interactionPosition;
  private final Vector2 interactionSize;

  private final int scoreModifier;

  private boolean used = false;

  private final boolean startsVisible;
  private boolean visible = true;
  private String key = null;
  private String lock = null;
  private boolean win = false;

  public Event(MapObject mapObject) {
    this(mapObject, parseProperties(mapObject));
  }

  private Event(MapObject mapObject, ParsedEventProps props) {
    super(new Texture(props.interactionImagePath), props.x, props.y, props.width, props.height, !props.visible, props.solid);

    eventId = resolveEventId(mapObject, props.x, props.y);
    startsVisible = props.visible;
    visible = props.visible;
    interactionMessage = props.interactionMessage;
    interactionImage = new Texture(props.interactionImagePath);
    interactionPosition = new Vector2(props.x, props.y);
    interactionSize = new Vector2(props.width, props.height);
    scoreModifier = props.scoreModifier;

    if (props.key != null) {
      key = props.key;
      super.addKey(key);
    }

    if (props.lock != null) {
      lock = props.lock;
    }

    win = props.win;
  }

  // Consolidated "scoreIncrement" and "scoreDecrement" into single "modifyScore" method
  public void modifyScore(final YettiGame game) {
    game.score += getScoreModifier();
  }

  public int getScoreModifier() {
    return scoreModifier;
  }

  public String getEventId() {
    return eventId;
  }

  public boolean isWinEvent() {
    return win;
  }

  public boolean isHiddenEvent() {
    return !startsVisible;
  }

  public boolean isPositiveEvent() {
    return scoreModifier > 0;
  }

  public boolean isNegativeEvent() {
    return scoreModifier < 0;
  }

  public void showInteractionMessage(GameScreen screen) {
    if (interactionMessage != null && !interactionMessage.isBlank()) {
      screen.spawnInteractionMessage(interactionMessage);
    }
  }

  @Override
  public void render(SpriteBatch batch) {
    batch.draw(interactionImage, interactionPosition.x, interactionPosition.y, interactionSize.x, interactionSize.y);
    batch.end();
  }

  public int getCounter() {
    return counter;
  }

  public void incrementCounter() {
    this.counter += 1;
  }

  public boolean activate(final GameScreen screen, Player player, Item item, YettiGame game) {
    if (win) {
      game.setScreen(new WinScreen(game, game.calculateFinalScore(), game.playerName));
    }

    if (lock != null) {
      if (player.hasKey(lock)) {
        super.setSolid(false);
        this.setSolid(false);
      }
    }

    if (!used) {
      used = true;
      this.toggleVisibility();
      if (interactionMessage != null && !interactionMessage.isBlank()) {
        screen.spawnInteractionMessage(interactionMessage);
      }
      return true;
    }

    return false;
  }

  @Override
  public boolean isVisible(){
    return visible;
  }

  private void toggleVisibility() {
    visible = !visible;
  }

  private static ParsedEventProps parseProperties(MapObject mapObject) {
    MapProperties properties = mapObject.getProperties();
    ParsedEventProps props = new ParsedEventProps();
    props.x = properties.get("x", 0f, Float.class) / 48f;
    props.y = properties.get("y", 0f, Float.class) / 48f;
    props.width = properties.get("width", 0f, Float.class) / 48f;
    props.height = properties.get("height", 0f, Float.class) / 48f;
    props.visible = properties.get("visible", true, Boolean.class);
    props.solid = properties.get("solid", false, Boolean.class);
    props.interactionImagePath = properties.get("interactionImagePath", "", String.class);
    props.interactionMessage = properties.get("interactionMessage", "", String.class);
    props.scoreModifier = properties.get("scoreModifier", 0, Integer.class);
    props.key = properties.containsKey("key") ? properties.get("key", String.class) : null;
    props.lock = properties.containsKey("lock") ? properties.get("lock", String.class) : null;
    props.win = properties.get("win", false, Boolean.class);
    return props;
  }

  private String resolveEventId(MapObject mapObject, float x, float y) {
    String name = mapObject.getName();
    String base = (name != null && !name.isBlank()) ? name : "event";
    return base + "@" + x + "," + y;
  }

  private static class ParsedEventProps {
    float x;
    float y;
    float width;
    float height;
    boolean visible;
    boolean solid;
    String interactionImagePath;
    String interactionMessage;
    int scoreModifier;
    String key;
    String lock;
    boolean win;
  }
}