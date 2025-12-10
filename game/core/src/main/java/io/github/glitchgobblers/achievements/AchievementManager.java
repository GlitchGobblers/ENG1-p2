package io.github.glitchgobblers.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import io.github.glitchgobblers.YettiGame;
import java.util.ArrayList;
import java.util.List;


public class AchievementManager {
  private static final float POPUP_DURATION = 3f;
  private static final float POPUP_PADDING = 12f;
  private static final float POPUP_ICON_SIZE = 48f;

  private final Array<Achievement> achievements = new Array<>();
  private final ObjectMap<String, Texture> iconCache = new ObjectMap<>();
  private final Texture fallbackIcon;
  private final Array<Popup> popups = new Array<>();
  private final FileHandle saveFile = Gdx.files.local("achievements.txt");

  public AchievementManager() {
    fallbackIcon = createFallbackIcon();
    defineAll();
    loadProgress();
  }

  //return immutable copy of achievements to display in UI.
  public List<Achievement> getAchievements() {
    List<Achievement> list = new ArrayList<>(achievements.size);
    achievements.forEach(list::add);
    return list;
  }


  //Unlock an achievement by id. If newly unlocked, persists and queues a popup.
  public boolean unlock(String id, YettiGame game) {
    Achievement achievement = find(id);
    if (achievement == null || achievement.isUnlocked()) {
      return false;
    }

    achievement.setUnlocked(true);
    saveProgress();
    queuePopup(achievement, game);
    return true;
  }

  public boolean isUnlocked(String id) {
    Achievement achievement = find(id);
    return achievement != null && achievement.isUnlocked();
  }


  //Render and advance queued popups. Safe to call from any screen.
  public void renderPopups(YettiGame game, SpriteBatch batch, Matrix4 projection, BitmapFont font, float delta) {
    if (popups.isEmpty()) {
      return;
    }

    batch.setProjectionMatrix(projection);
    batch.begin();
    Array<Popup> toRemove = new Array<>();

    for (Popup popup : popups) {
      popup.elapsed += delta;
      float t = Math.min(1f, popup.elapsed / POPUP_DURATION);
      float alpha = 1f - t;

      // Background box anchored bottom-right
      float boxW = POPUP_ICON_SIZE + POPUP_PADDING * 3 + popup.textWidth;
      float boxH = Math.max(POPUP_ICON_SIZE, popup.textHeight) + POPUP_PADDING * 2;
      float margin = 16f;
      float worldW = game.viewport.getWorldWidth();
      float x = worldW - boxW - margin;
      float y = margin;

      Color prevBatchColor = batch.getColor();
      Color prevFontColor = new Color(font.getColor());
      batch.setColor(0f, 0f, 0f, 0.65f * alpha);
      batch.draw(popup.background, x, y, boxW, boxH);
      batch.setColor(1f, 1f, 1f, alpha);

      // Icon
      batch.draw(popup.icon, x + POPUP_PADDING, y + (boxH - POPUP_ICON_SIZE) / 2f, POPUP_ICON_SIZE, POPUP_ICON_SIZE);

      // Text (title + name stacked)
      float textX = x + POPUP_PADDING * 2 + POPUP_ICON_SIZE;
      float textTopY = y + boxH - POPUP_PADDING;

      font.setColor(1f, 1f, 1f, alpha);
      font.draw(batch, popup.titleText, textX, textTopY);
      font.setColor(prevFontColor);

      BitmapFont scaledFont = font;
      float prevScaleX = scaledFont.getData().scaleX;
      float prevScaleY = scaledFont.getData().scaleY;
      scaledFont.getData().setScale(0.9f);
      scaledFont.setColor(1f, 1f, 1f, alpha);
      scaledFont.draw(batch, popup.nameText, textX, textTopY - scaledFont.getLineHeight());
      scaledFont.setColor(prevFontColor);
      scaledFont.getData().setScale(prevScaleX, prevScaleY);

      batch.setColor(prevBatchColor);

      if (popup.elapsed >= POPUP_DURATION) {
        toRemove.add(popup);
      }
    }

    for (Popup popup : toRemove) {
      popup.background.dispose();
    }
    popups.removeAll(toRemove, true);
    batch.end();
  }

  public Texture getIcon(String id) {
    return iconCache.get(id, fallbackIcon);
  }

  // Convenience hooks for future event-based achievements.
  public void markEventTriggered(YettiGame game) {
    unlock("event_hunter", game);
  }

  public void markAllEventsCompleted(YettiGame game) {
    unlock("campus_completionist", game);
  }

  public void dispose() {
    for (Texture texture : iconCache.values()) {
      texture.dispose();
    }
    fallbackIcon.dispose();
    for (Popup popup : popups) {
      popup.background.dispose();
    }
  }

  private Achievement find(String id) {
    for (Achievement achievement : achievements) {
      if (achievement.getId().equals(id)) {
        return achievement;
      }
    }
    return null;
  }

  private void queuePopup(Achievement achievement, YettiGame game) {
    Popup popup = new Popup();
    popup.icon = getTextureWithFallback(achievement.getIconPath(), achievement.getId());
    popup.titleText = "Achievement";
    popup.nameText = achievement.getName();

    GlyphLayout layoutTitle = new GlyphLayout(game.fontBorderedSmall, popup.titleText);
    GlyphLayout layoutName = new GlyphLayout(game.fontSmall, popup.nameText);
    popup.textWidth = Math.max(layoutTitle.width, layoutName.width);
    popup.textHeight = layoutTitle.height + layoutName.height + POPUP_PADDING * 0.25f;

    popup.background = createBackgroundTexture();
    popups.add(popup);
  }

  private void defineAll() {
    achievements.clear();
    achievements.add(
        new Achievement("welcome_campus", "Welcome to Campus",
            "Start a run for the first time.",
            "ui/ach_welcome.png"));
    achievements.add(
        new Achievement("pause_once", "Take a Breather",
            "Pause the game once.",
            "ui/ach_pause.png"));
    achievements.add(
        new Achievement("caught_by_dean", "Dean's Dinner",
            "Get caught by the Dean.",
            "ui/ach_dean.png"));
    achievements.add(
        new Achievement("times_up", "Time's Up",
            "Run out of time.",
            "ui/ach_timeout.png"));
    achievements.add(
        new Achievement("event_hunter", "Event Hunter",
            "Trigger any event.",
            "ui/ach_event.png"));
    achievements.add(
        new Achievement("campus_completionist", "Campus Completionist",
            "Trigger all events and finish the level.",
            "ui/ach_completionist.png"));

    // Prime icon cache for definitions
    achievements.forEach(a -> getTextureWithFallback(a.getIconPath(), a.getId()));
  }

  private void loadProgress() {
    if (!saveFile.exists()) {
      return;
    }
    String raw = saveFile.readString();
    String[] lines = raw.split("\\r?\\n");
    for (String line : lines) {
      if (line.trim().isEmpty()) {
        continue;
      }
      String[] parts = line.split("=");
      if (parts.length != 2) {
        continue;
      }
      String id = parts[0].trim();
      boolean unlocked = Boolean.parseBoolean(parts[1].trim());
      Achievement achievement = find(id);
      if (achievement != null) {
        achievement.setUnlocked(unlocked);
      }
    }
  }

  private void saveProgress() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < achievements.size; i++) {
      Achievement a = achievements.get(i);
      builder.append(a.getId())
          .append("=")
          .append(a.isUnlocked());
      if (i < achievements.size - 1) {
        builder.append(System.lineSeparator());
      }
    }
    String contents = builder.toString();
    saveFile.writeString(contents, false);
  }

  private Texture getTextureWithFallback(String path, String cacheKey) {
    if (iconCache.containsKey(cacheKey)) {
      return iconCache.get(cacheKey);
    }

    if (Gdx.files.internal(path).exists()) {
      Texture texture = new Texture(path);
      iconCache.put(cacheKey, texture);
      return texture;
    }

    return fallbackIcon;
  }

  private Texture createFallbackIcon() {
    Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
    pixmap.setColor(0.2f, 0.6f, 1f, 1f);
    pixmap.fill();
    pixmap.setColor(Color.WHITE);
    pixmap.drawLine(0, 0, 31, 31);
    pixmap.drawLine(0, 31, 31, 0);
    Texture texture = new Texture(pixmap);
    pixmap.dispose();
    return texture;
  }

  private Texture createBackgroundTexture() {
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.fill();
    Texture texture = new Texture(pixmap);
    pixmap.dispose();
    return texture;
  }

  private static class Popup {
    Texture icon;
    Texture background;
    String titleText;
    String nameText;
    float textWidth;
    float textHeight;
    float elapsed = 0f;
  }
}

