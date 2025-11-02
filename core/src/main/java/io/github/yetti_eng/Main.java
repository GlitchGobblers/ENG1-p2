package io.github.yetti_eng;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.entities.Wall;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private FitViewport viewport;

    private Texture ballmanTexture;
    private Texture wallTexture;

    Player player;
    Wall testWall;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(16, 9);

        ballmanTexture = new Texture("ballman.png");
        wallTexture = new Texture("wall.png");

        player = new Player(ballmanTexture, 5, 5);
        testWall = new Wall(wallTexture, 10, 5);
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
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.RIGHT, Input.Keys.D)) {
            player.addMovement(1, 0);
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.LEFT, Input.Keys.A)) {
            player.addMovement(-1, 0);
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.UP, Input.Keys.W)) {
            player.addMovement(0, 1);
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.DOWN, Input.Keys.S)) {
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
    }

    private void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        // batch.draw(image, 4, 4, 8, 1);
        player.draw(batch);
        testWall.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        ballmanTexture.dispose();
        wallTexture.dispose();
    }
}
