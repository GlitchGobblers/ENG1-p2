package io.github.yetti_eng;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.yetti_eng.screens.MenuScreen;

// Called "Game" in the architecture documentation; renamed to avoid clash with LibGDX class name
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class YettiGame extends Game {
    private static final float WORLD_SCALE = 80; // 16:9 * 80 = 1280:720

    public int score;

    public SpriteBatch batch;
    public FitViewport viewport;

    private FreeTypeFontGenerator robotoGenerator;
    private final FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
    public BitmapFont font;

    private boolean paused;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(scaled(16), scaled(9));

        robotoGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-VariableFont_wdth,wght.ttf"));
        fontParameter.size = (int) scaled(1);
        fontParameter.color = Color.WHITE.cpy();
        font = robotoGenerator.generateFont(fontParameter);

        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        robotoGenerator.dispose();
    }

    /**
     * @param original The original size, given the dimensions of the game's 16x9 grid.
     * @return The scaled size based on the program's WORLD_SCALE.
     */
    public static float scaled(float original) {
        return original * WORLD_SCALE;
    }

    /**
     * @return true if the game is currently paused; false otherwise.
     */
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
}
