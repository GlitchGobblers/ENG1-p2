package io.github.glitchgobblers;

import java.util.UUID;

public final class identity {
  private static final String DEFAULT_NAME = "Player";

  private static final String playthroughId = UUID.randomUUID().toString();
  private static String name = DEFAULT_NAME;

  private identity() {}


  public static String getOrCreateId() {
    return playthroughId;
  }

  public static void setName(String newName) {
    name = (newName == null || newName.isEmpty()) ? DEFAULT_NAME : newName;
  }


  public static String getName() {
    return name;
  }
}
