package io.github.glitchgobblers.achievements;

public class Achievement {
  private final String id;
  private final String name;
  private final String description;
  private final String iconPath;
  private boolean unlocked;

  public Achievement(String id, String name, String description, String iconPath) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.iconPath = iconPath;
    this.unlocked = false;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getIconPath() {
    return iconPath;
  }

  public boolean isUnlocked() {
    return unlocked;
  }

  public void setUnlocked(boolean unlocked) {
    this.unlocked = unlocked;
  }
}

