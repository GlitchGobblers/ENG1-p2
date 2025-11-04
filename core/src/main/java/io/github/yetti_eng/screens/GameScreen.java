package io.github.yetti_eng.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.yetti_eng.InputHelper;
import io.github.yetti_eng.Timer;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.entities.Wall;

import static io.github.yetti_eng.YettiGame.scaled;

public class GameScreen implements Screen {
    private final YettiGame game;

    private static final int TIMER_LENGTH = 300; // 300s = 5min

    private Texture ballmanTexture;
    private Texture wallTexture;

    private Player player;
    private Wall testWall;

    private Timer timer;
    private Label timerText;

    public GameScreen(final YettiGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ballmanTexture = new Texture("ballman.png");
        wallTexture = new Texture("wall.png");

        player = new Player(ballmanTexture, 5, 5);
        testWall = new Wall(wallTexture, 10, 5);

        timer = new Timer(TIMER_LENGTH);
        timer.play();
        timerText = new Label(null, new Label.LabelStyle(game.font, Color.BLACK.cpy()));
        timerText.setPosition(scaled(0), scaled(5));
    }

    @Override
    public void render(float delta) {
        input(delta);
        logic(delta);
        draw(delta);
    }

    private void input(float delta) {
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

    private void logic(float delta) {
        // Only move the player if the game isn't paused
        if (!game.isPaused()) {
            player.doMove(delta);
        }

        // Detect collision with objects
        if (player.collidedWith(testWall)) {
            // If the player just collided with an object, move in the opposite direction
            // TODO: Make the player able to move laterally even when colliding with a wall
            player.reverseMovement();
            player.doMove(delta);
        }

        // Clamp to edges of screen
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        float playerWidth = player.getWidth();
        float playerHeight = player.getHeight();

        player.setX(MathUtils.clamp(player.getX(), 0, worldWidth - playerWidth));
        player.setY(MathUtils.clamp(player.getY(), 0, worldHeight - playerHeight));

        // Timer
        if (timer.hasElapsed()) {
            timer.finish();
            game.setScreen(new LoseScreen(game));
            dispose();
        }

        int timeRemaining = timer.getRemainingTime();
        String text = (timeRemaining / 60) + ":" + String.format("%02d", timeRemaining % 60);
        timerText.setText(text);
        timerText.setStyle(new Label.LabelStyle(game.font, (timer.isActive() ? Color.BLACK : Color.RED).cpy()));

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (game.isPaused()) {
                game.resume();
            } else {
                game.pause();
            }
        }
    }

    private void draw(float delta) {
        ScreenUtils.clear(0.15f, 0.6f, 0.2f, 1f);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        // batch.draw(image, 4, 4, 8, 1);
        player.draw(game.batch);
        testWall.draw(game.batch);

        timerText.draw(game.batch, 1.0f);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        timer.pause();
    }

    @Override
    public void resume() {
        timer.play();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        ballmanTexture.dispose();
        wallTexture.dispose();
    }
}
