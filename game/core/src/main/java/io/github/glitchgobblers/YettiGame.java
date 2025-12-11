package io.github.glitchgobblers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.glitchgobblers.achievements.AchievementManager;
import io.github.glitchgobblers.screens.MenuScreen;
import com.badlogic.gdx.Preferences;
import java.util.UUID;

// Called "Game" in the architecture documentation; renamed to avoid clash with LibGDX class name
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class YettiGame extends Game {
  private static final float WORLD_SCALE = 80; // 16:9 * 80 = 1280:720

  public SpriteBatch batch;
  public FitViewport viewport;

  private FreeTypeFontGenerator robotoGenerator;
  public BitmapFont font;
  public BitmapFont fontSmall;
  public BitmapFont fontBordered;
  public BitmapFont fontBorderedSmall;
  public String playerName = "Bob";

  public float volume = 1.0f;
  private boolean paused;

  public Timer timer;
  public int score;
  public AchievementManager achievements;

  public String getOrCreatePlayerId() {
    Preferences prefs = Gdx.app.getPreferences("yetti-player");
    String id = prefs.getString("playerId", "");
    if (id == null || id.isEmpty()) {
      id = UUID.randomUUID().toString();
      prefs.putString("playerId", id);
      prefs.flush();
    }
    return id;
  }

  @Override
  public void create() {
    batch = new SpriteBatch();
    viewport = new FitViewport(scaled(16), scaled(9));

    robotoGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto.ttf"));

    var fontParameter = new FreeTypeFontParameter();
    fontParameter.size = (int) scaled(1);
    fontParameter.color = Color.WHITE.cpy();
    font = robotoGenerator.generateFont(fontParameter);

    fontParameter.size = (int) scaled(0.5f);
    fontSmall = robotoGenerator.generateFont(fontParameter);

    fontParameter.color = Color.BLACK.cpy();
    fontParameter.borderColor = Color.WHITE.cpy();
    fontParameter.borderWidth = 2;
    fontBorderedSmall = robotoGenerator.generateFont(fontParameter);

    fontParameter.size = (int) scaled(1f);
    fontParameter.borderWidth = 4;
    fontBordered = robotoGenerator.generateFont(fontParameter);

    achievements = new AchievementManager();

    setScreen(new MenuScreen(this));
  }

  @Override
  public void dispose() {
    super.dispose();
    batch.dispose();
    robotoGenerator.dispose();
    font.dispose();
    fontBordered.dispose();
    fontBorderedSmall.dispose();
    achievements.dispose();
  }

  /**
   * @param original The original size, given the dimensions of the game's 16x9 grid.
   * @return The scaled size based on the program's WORLD_SCALE.
   */
  public static float scaled(float original) {
    return original * WORLD_SCALE;
  }

  public boolean isPaused() {
    return paused;
  }

  @Override
  public void pause() {
    paused = true;
    super.pause();
  }

  @Override
  public void resume() {
    paused = false;
    super.resume();
  }

  public int calculateFinalScore() {
    return score + timer.getRemainingTime();
  }
}
