package io.github.glitchgobblers.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.achievements.Achievement;
import io.github.glitchgobblers.achievements.AchievementManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementTest extends GdxTestBase {
  AchievementManager manager;
  YettiGame game;
  @BeforeEach
  public void beforeEach(){
    Gdx.files.local("achievements.txt").delete();
     manager = new AchievementManager();


    game = new YettiGame();
    game.fontSmall = new BitmapFont();
    game.fontBorderedSmall = new BitmapFont();
    game.viewport = new FitViewport(16, 9);
  }


  @Test
  public void testAchievementFields() {
    Achievement a = new Achievement(
      "ID-test", "Name-test",
      "DES-test",
      "icon-test");

    assertEquals("ID-test", a.getId());
    assertEquals("Name-test", a.getName());
    assertEquals("DES-test", a.getDescription());
    assertEquals("icon-test", a.getIconPath());

    assertFalse(a.isUnlocked());
    a.setUnlocked(true);
    assertTrue(a.isUnlocked());
  }

  @Test
  public void testAchievementUnlockBehaviour() {

    //"Welcome campus" should exist but start locked
    assertFalse(manager.isUnlocked("welcome_campus"),
      "Welcome campus should exist but start locked");

    // first time calling unlock should succeed
    boolean firstUnlock = manager.unlock("welcome_campus", game);
    assertTrue(firstUnlock, "first unlock call should return true");
    assertTrue(manager.isUnlocked("welcome_campus"),
      "welcome_campus should now be unlocked");

    // second unlock call should return false
    boolean secondUnlock = manager.unlock("welcome_campus", game);
    assertFalse(secondUnlock, "second unlock call should return false");
  }

  @Test
  public void testAchievementEventHooks() {

    // event achievement should be locked when the game start
    assertFalse(manager.isUnlocked("event_hunter"),
      "event_hunter should be locked when the game start");
    assertFalse(manager.isUnlocked("campus_completionist"),
      "campus_completionist should start locked");

    // when a single event is triggered it should unlock the event_hunter
    manager.markEventTriggered(game);
    assertTrue(manager.isUnlocked("event_hunter"),
      " triggering a single event should unlock event_hunter");

    // completing all events should unlock campus_completionist
    manager.markAllEventsCompleted(game);
    assertTrue(manager.isUnlocked("campus_completionist"),
      "completing all events should unlock campus_completionist");
  }
}
