package io.github.glitchgobblers.entities;

import com.badlogic.gdx.graphics.Texture;
import io.github.glitchgobblers.screens.GameScreen;


import java.util.ArrayList;

public class Player extends Entity {
  private static final float PLAYER_SPEED = 6f; // Normal value
  // private static final float PLAYER_SPEED = 25f;  // for testing when you dont want to be slow

  private final float baseSpeed = PLAYER_SPEED;
  private float speedBoostTimeLeft = 0f;

  final ArrayList<String> inventory = new ArrayList<>();

  public Player(Texture tex, float x, float y) {
    super(tex, x, y, 0.9f, 1.6f, PLAYER_SPEED, false, false);
  }

  public void applySpeedBoost(float multiplier, float durationSeconds) {
    setSpeed(baseSpeed * multiplier);
    speedBoostTimeLeft = durationSeconds;
  }

  public void update(float delta, GameScreen screen) {
    if (speedBoostTimeLeft > 0f) {
      speedBoostTimeLeft -= delta;
      if (speedBoostTimeLeft <= 0f) {
        speedBoostTimeLeft = 0f;
        setSpeed(baseSpeed);
        screen.spawnInteractionMessage("Your coffee wore off");
      }
    }
  }

  public boolean hasKey(String lock) {
    return inventory.contains(lock);
  }

/*
  public boolean hasUsedItem(String itemID) {
    return inventory.stream().anyMatch(i -> i.ID.equals(itemID));
  }*/
}
