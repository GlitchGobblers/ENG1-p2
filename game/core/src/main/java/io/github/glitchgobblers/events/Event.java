package io.github.glitchgobblers.events;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.entities.Item;
import io.github.glitchgobblers.entities.Player;
import io.github.glitchgobblers.screens.GameScreen;
import io.github.glitchgobblers.screens.WinScreen;

public class Event extends Item {
  private int counter = 0;

  private final String interactionMessage;
  private final String lockedMessage;
  private final Texture interactionImage;
  private final Vector2 interactionPosition;
  private final Vector2 interactionSize;

  private final int scoreModifier;
  private SpriteBatch batch;

  private boolean used = false;

  private boolean visible = true;
  private String key = null;
  private String lock = null;
  private boolean win = false;
  private Float speedMultiplier = null;
  private Float speedDuration = null;
  private Float barrierDuration = null;
  private float barrierTimeLeft = 0f;
  private boolean barrierCountingDown = false;


  public Event(MapProperties properties) {
    super(new Texture((String) properties.get("interactionImagePath")),
      (Float) properties.get("x") / 48,
      (Float) properties.get("y") / 48,
      (Float) properties.get("width") / 48,
      (Float) properties.get("height") / 48,
      (Boolean) properties.get("visible"),
      (Boolean) properties.get("solid")
    );

    interactionMessage = (String) properties.get("interactionMessage");
    String lm = (String) properties.get("lockedMessage");
    lockedMessage = (lm != null) ? lm : "This door is locked";

    String interactionImagePath = (String) properties.get("interactionImagePath");
    interactionImage = new Texture(interactionImagePath);

    interactionPosition = new Vector2(
      (Float) properties.get("x") / 48,
      (Float) properties.get("y") / 48
    );
    interactionSize = new Vector2(
      (Float) properties.get("width") / 48,
      (Float) properties.get("height") / 48
    );

    visible = (Boolean) properties.get("visible");

    scoreModifier = (Integer) properties.get("scoreModifier");

    if (properties.get("key") != null) {
      key = (String) properties.get("key");
      super.addKey(key);
    }

    if (properties.get("lock") != null) {
      lock = (String) properties.get("lock");
    }

    if (properties.get("win") != null) {
      win = (Boolean) properties.get("win");
    }
    if (properties.get("speedMultiplier") != null) {
      speedMultiplier = (Float) properties.get("speedMultiplier");
    }
    if (properties.get("speedDuration") != null) {
      speedDuration = (Float) properties.get("speedDuration");
    }
    if (properties.get("barrierDuration") != null) {
      barrierDuration = (Float) properties.get("barrierDuration");
    }
  }
  @Override
  public void update(float delta) {
    if (!barrierCountingDown) return;

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

  public void showInteractionMessage(GameScreen screen) {
    screen.spawnInteractionMessage(interactionMessage);
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

    if (lock != null && player.hasKey(lock)) {
      if (player.hasKey(lock)) {
        super.setSolid(false);
        this.setSolid(false);
      }
    }

    if (!used) {
      used = true;
      this.toggleVisibility();
      screen.spawnInteractionMessage(interactionMessage);
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
}