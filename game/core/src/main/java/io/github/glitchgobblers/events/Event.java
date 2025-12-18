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
  private final String lockedMessage;
  private final Texture interactionImage;
  private final Vector2 interactionPosition;
  private final Vector2 interactionSize;

  private final int scoreModifier;

  private boolean used = false;

  private final boolean startsVisible;
  private final EventType eventType;
  private boolean visible = true;
  private String key = null;
  private String lock = null;
  private final boolean AlwaysLocked;
  private boolean win = false;
  private Float speedMultiplier = null;
  private Float speedDuration = null;
  private Float barrierDuration = null;
  private float barrierTimeLeft = 0f;
  private boolean barrierCountingDown = false;

  public Event(MapObject mapObject) {
    this(mapObject, parseProperties(mapObject));
  }

  private Event(MapObject mapObject, ParsedEventProps props) {
    super(new Texture(props.interactionImagePath), props.x, props.y, props.width, props.height, !props.visible, props.solid);

    eventId = resolveEventId(mapObject, props.x, props.y);
    startsVisible = props.visible;
    visible = props.visible;
    interactionMessage = props.interactionMessage;
    eventType = props.eventType;

    String lm = props.lockedMessage;
    lockedMessage = (lm != null) ? lm : "This door is locked";

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

    speedMultiplier = (props.speedMultiplier != 0f) ? props.speedMultiplier : null;
    speedDuration   = (props.speedDuration   != 0f) ? props.speedDuration   : null;
    barrierDuration = (props.barrierDuration != 0f) ? props.barrierDuration : null;
    AlwaysLocked = props.AlwaysLocked;

  }

  @Override
  public void update(float delta) {
    if (!barrierCountingDown) {
      return;
    }

    barrierTimeLeft -= delta;

    if (barrierTimeLeft <= 0f) {
      barrierCountingDown = false;
      barrierTimeLeft = 0f;

      super.setSolid(false);
      toggleVisibility();
      disable();
    }
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
    return eventType == EventType.HIDDEN;
  }

  public boolean isPositiveEvent() {
    return eventType == EventType.POSITIVE;
  }

  public boolean isNegativeEvent() {
    return eventType == EventType.NEGATIVE;
  }

  public EventType getEventType() {
    return eventType;
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

    if (barrierDuration != null) {
      if (!barrierCountingDown) {
        barrierCountingDown = true;
        barrierTimeLeft = barrierDuration;

        this.toggleVisibility();
        this.setSolid(true);
        super.setSolid(true);

        screen.spawnInteractionMessage(interactionMessage);
      }
      return false;
    }

    if (lock != null && !player.hasKey(lock)) {
      screen.spawnInteractionMessage(lockedMessage);
      return false;
    }

    if (AlwaysLocked && lock != null && player.hasKey(lock)) {

      if (!used) {
        used = true;
        if (interactionMessage != null && !interactionMessage.isBlank()) {
          screen.spawnInteractionMessage(interactionMessage);
        }
        return true;
      }
      if (interactionMessage != null && !interactionMessage.isBlank()) {
        screen.spawnInteractionMessage(interactionMessage);
      }
      return false;
    }

    if (!AlwaysLocked && lock != null && player.hasKey(lock)) {
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

      if (speedMultiplier != null && speedDuration != null) {
        player.applySpeedBoost(speedMultiplier, speedDuration);
      }
      return true;
    }

    return false;
  }

  @Override
  public boolean isVisible() {
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
    props.lockedMessage = properties.get("lockedMessage", "", String.class);
    props.AlwaysLocked = properties.get("AlwaysLocked", false, Boolean.class);

    props.scoreModifier = properties.get("scoreModifier", 0, Integer.class);

    props.key = properties.containsKey("key") ? properties.get("key", String.class) : null;
    props.lock = properties.containsKey("lock") ? properties.get("lock", String.class) : null;

    props.win = properties.get("win", false, Boolean.class);

    String rawType = properties.get("eventType", "", String.class);
    props.eventType = parseEventType(rawType, props.visible, props.scoreModifier);

    if (properties.containsKey("speedMultiplier")) {
      props.speedMultiplier = properties.get("speedMultiplier", Float.class);
    }

    if (properties.containsKey("speedDuration")) {
      props.speedDuration = (Float) properties.get("speedDuration");
    }

    if (properties.containsKey("barrierDuration")) {
      props.barrierDuration = (Float) properties.get("barrierDuration");
    }

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
    String lockedMessage;
    int scoreModifier;
    String key;
    String lock;
    boolean win;
    float speedMultiplier;
    float speedDuration;
    float barrierDuration;
    EventType eventType;
    boolean AlwaysLocked;
  }
  private static EventType parseEventType(String rawType, boolean visible, int scoreModifier) {
    if (rawType != null) {
      String t = rawType.trim().toLowerCase();
      switch (t) {
        case "positive": return EventType.POSITIVE;
        case "negative": return EventType.NEGATIVE;
        case "hidden":   return EventType.HIDDEN;
      }
    }
    return null;
  }
}