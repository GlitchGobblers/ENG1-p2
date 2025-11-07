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
import io.github.yetti_eng.entities.*;
import io.github.yetti_eng.events.*;

import java.util.ArrayList;

import static io.github.yetti_eng.YettiGame.scaled;

public class GameScreen implements Screen {
    private final YettiGame game;

    private static final int TIMER_LENGTH = 300; // 300s = 5min

    private Texture ballmanTexture;
    private Texture wallTexture;
    private Texture exitTexture;
    private Texture keyTexture;
    private Texture doorTexture;
    private Texture duckTexture;
    private Texture surprisedTexture;
    private Texture angryTexture;

    private Player player;
    private Dean dean;
    private Item exit;
    private final ArrayList<Entity> entities = new ArrayList<>();

    private Label timerText;

    public GameScreen(final YettiGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ballmanTexture = new Texture("ballman.png");
        wallTexture = new Texture("wall.png");
        exitTexture = new Texture("exit.png");
        keyTexture = new Texture("key.png");
        doorTexture = new Texture("door.png");
        duckTexture = new Texture("duck.png");
        surprisedTexture = new Texture("surprised.png");
        angryTexture = new Texture("angry.png");

        player = new Player(ballmanTexture, 5, 5);
        dean = new Dean(angryTexture, 0, 0);
        exit = new Item(new WinEvent(), "exit", exitTexture, 14, 5);
        entities.add(new Wall(wallTexture, 10, 5));
        entities.add(new Item(new KeyEvent(), "key", keyTexture, 6, 3));
        entities.add(new Item(new DoorEvent(), "door", doorTexture, 9, 3, false, true));
        entities.add(new Item(new IncreasePointsEvent(), "long_boi", duckTexture, 7.5f, 8));
        entities.add(new Item(new HiddenDeductPointsEvent(), "surprised_student", surprisedTexture, 11, 5, true, false));

        game.timer = new Timer(TIMER_LENGTH);
        game.timer.play();
        timerText = new Label(null, new Label.LabelStyle(game.font, Color.BLACK.cpy()));
        timerText.setPosition(scaled(0), scaled(5));
    }

    @Override
    public void render(float delta) {
        input(delta);
        logic(delta);
        draw(delta);
        postLogic(delta); // Used for logic that should happen after rendering, normally screen changes
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
        player.normaliseMovement();
    }

    private void logic(float delta) {
        // Only move the player if the game isn't paused
        if (!game.isPaused()) {
            player.doMove(delta);
            dean.calculateMovement(player);
            dean.doMove(delta);
        }

        // Detect collision with objects
        entities.forEach(e -> {
            if (player.collidedWith(e) && e.isEnabled()) {
                // Check for collision with solid objects
                if (e.isSolid()) {
                    // If the player just collided with a solid object, move in the opposite direction
                    // TODO: Make the player able to move laterally even when colliding with a solid object
                    player.reverseMovement();
                    player.doMove(delta);
                }
                // Check for interaction with items
                if (e instanceof Item item) {
                    item.interact(game, player);
                }
            }
        });

        // Clamp to edges of screen
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        float playerWidth = player.getWidth();
        float playerHeight = player.getHeight();

        player.setX(MathUtils.clamp(player.getX(), 0, worldWidth - playerWidth));
        player.setY(MathUtils.clamp(player.getY(), 0, worldHeight - playerHeight));

        // Calculate remaining time
        int timeRemaining = game.timer.getRemainingTime();
        String text = (timeRemaining / 60) + ":" + String.format("%02d", timeRemaining % 60);
        timerText.setText(text);
        timerText.setStyle(new Label.LabelStyle(game.font, (game.timer.isActive() ? Color.BLACK : Color.RED).cpy()));

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

        // Draw only visible entities
        entities.forEach(e -> { if (e.isVisible()) e.draw(game.batch); });
        // Draw exit, player, and dean on top
        exit.draw(game.batch);
        player.draw(game.batch);
        dean.draw(game.batch);

        if (game.isPaused()) {
            game.font.draw(game.batch, "PAUSED", scaled(6), scaled(5));
        }

        timerText.draw(game.batch, 1.0f);

        game.batch.end();
    }

    private void postLogic(float delta) {
        // Exit collision
        if (player.collidedWith(exit) && exit.isEnabled()) {
            exit.interact(game, player);
            return;
        }
        // Dean collision
        if (player.collidedWith(dean)) {
            dean.getsPlayer(game);
            return;
        }
        // Timer
        if (game.timer.hasElapsed()) {
            game.timer.finish();
            game.setScreen(new LoseScreen(game));
            dispose();
            return;
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        game.timer.pause();
    }

    @Override
    public void resume() {
        game.timer.play();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        ballmanTexture.dispose();
        wallTexture.dispose();
        exitTexture.dispose();
        keyTexture.dispose();
        doorTexture.dispose();
        duckTexture.dispose();
        surprisedTexture.dispose();
        angryTexture.dispose();
    }
}
