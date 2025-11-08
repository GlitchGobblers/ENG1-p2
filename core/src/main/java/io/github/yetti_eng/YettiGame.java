package io.github.yetti_eng;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.screens.MenuScreen;

import java.util.ArrayList;

// Called "Game" in the architecture documentation; renamed to avoid clash with LibGDX class name
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class YettiGame extends Game {
    private static final float WORLD_SCALE = 80; // 16:9 * 80 = 1280:720

    public SpriteBatch batch;
    public FitViewport viewport;

    private FreeTypeFontGenerator robotoGenerator;
    public BitmapFont font;
    public BitmapFont fontSmall;

    private final ArrayList<Label> messages = new ArrayList<>();
    private boolean paused;

    public Timer timer;
    public int score;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(scaled(16), scaled(9));

        robotoGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-VariableFont_wdth,wght.ttf"));

        var fontParameter = new FreeTypeFontParameter();
        fontParameter.size = (int) scaled(1);
        fontParameter.color = Color.WHITE.cpy();
        font = robotoGenerator.generateFont(fontParameter);

        fontParameter.size = (int) scaled(0.5f);
        fontSmall = robotoGenerator.generateFont(fontParameter);

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

    /**
     * Spawn a large text label at the centre of the screen
     * that floats upwards and fades out. Used to alert the player.
     * @param text The text that should be displayed.
     */
    public void spawnLargeMessage(String text) {
        Label label = new Label(text, new Label.LabelStyle(font, Color.WHITE.cpy()));
        label.setPosition(scaled(8), scaled(4.5f), Align.center);
        messages.add(label);
    }

    /**
     * Spawn a small text label at the player's position
     * that floats upwards and fades out. Used when interacting with Items.
     * @param player The current Player object.
     * @param text The text that should be displayed.
     */
    public void spawnInteractionMessage(Player player, String text) {
        Label label = new Label(text, new Label.LabelStyle(fontSmall, Color.WHITE.cpy()));
        label.setPosition(player.getX(), player.getY()+(player.getHeight()/2), Align.center);
        messages.add(label);
    }

    /**
     * @return The final score for the game.
     */
    public int calculateFinalScore() {
        score += timer.getRemainingTime();
        return score;
    }

    @Override
    public void render() {
        super.render();
        batch.begin();
        for (Label l : messages) {
            l.setY(l.getY()+1);
            l.getColor().add(0, 0, 0, -0.01f);
            l.draw(batch, 1);
        }
        messages.removeIf(l -> l.getColor().a <= 0);
        batch.end();
    }
}
