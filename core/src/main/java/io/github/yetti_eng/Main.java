package io.github.yetti_eng;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.entities.Wall;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private static final int TIMER_LENGTH = 300; // 300s = 5min

    private SpriteBatch batch;
    private FitViewport viewport;

    private Texture ballmanTexture;
    private Texture wallTexture;

    Player player;
    Wall testWall;

    private FreeTypeFontGenerator robotoGenerator;
    private final FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
    private BitmapFont roboto32;

    private Timer timer;
    private Label timerText;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(16, 9);

        ballmanTexture = new Texture("ballman.png");
        wallTexture = new Texture("wall.png");

        player = new Player(ballmanTexture, 5, 5);
        testWall = new Wall(wallTexture, 10, 5);

        robotoGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-VariableFont_wdth,wght.ttf"));
        fontParameter.size = 32;
        roboto32 = robotoGenerator.generateFont(fontParameter);

        timer = new Timer(TIMER_LENGTH);
        timer.play(); // TODO: move this to after leaving the menu screen
        timerText = new Label(null, new Label.LabelStyle(roboto32, Color.BLACK.cpy()));
        timerText.setPosition(0, 10);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        player.resetMovement();
        if (InputHelper.moveRightPressed()) {
            player.addMovement(1, 0);
        }
        if (InputHelper.moveLeftPressed()) {
            player.addMovement(-1, 0);
        }
        if (InputHelper.moveUpPressed()) {
            player.addMovement(0, 1);
        }
        if (InputHelper.moveDownPressed()) {
            player.addMovement(0, -1);
        }
    }

    private void logic() {
        player.doMove();

        // Detect collision with objects
        if (player.collidedWith(testWall)) {
            // If the player just collided with an object, move in the opposite direction
            // TODO: Make the player able to move laterally even when colliding with a wall
            player.reverseMovement();
            player.doMove();
        }

        // Clamp to edges of screen
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float playerWidth = player.getWidth();
        float playerHeight = player.getHeight();

        player.setX(MathUtils.clamp(player.getX(), 0, worldWidth - playerWidth));
        player.setY(MathUtils.clamp(player.getY(), 0, worldHeight - playerHeight));

        // Timer
        // For testing purposes
        if (!timer.isActive()) {
            timerText.setStyle(new Label.LabelStyle(roboto32, Color.RED.cpy()));
        } else {
            timerText.setStyle(new Label.LabelStyle(roboto32, Color.BLACK.cpy()));
        }

        if (timer.hasElapsed()) {
            timer.finish();
            System.out.println("Timer elapsed!");
            timer.finish();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (timer.isActive()) {
                timer.pause();
            } else {
                timer.play();
            }
        }
        // For testing purposes END
    }

    private void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // batch.draw(image, 4, 4, 8, 1);
        player.draw(batch);
        testWall.draw(batch);

        int timeRemaining = timer.getRemainingTime();
        String text = (timeRemaining / 60) + ":" + String.format("%02d", timeRemaining % 60);
        timerText.setText(text);
        timerText.draw(batch, 1.0f);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        ballmanTexture.dispose();
        wallTexture.dispose();
        robotoGenerator.dispose();
    }
}
