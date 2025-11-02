package io.github.yetti_eng;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.yetti_eng.entities.Player;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private FitViewport viewport;

    private Texture ballmanTexture;
    Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(16, 9);
        ballmanTexture = new Texture("ballman.png");
        player = new Player(ballmanTexture, 5, 5, 1, 1);
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
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.RIGHT, Input.Keys.D)) {
            player.moveRight();
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.LEFT, Input.Keys.A)) {
            player.moveLeft();
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.UP, Input.Keys.W)) {
            player.moveUp();
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.DOWN, Input.Keys.S)) {
            player.moveDown();
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float playerWidth = player.getWidth();
        float playerHeight = player.getHeight();

        player.setX(MathUtils.clamp(player.getX(), 0, worldWidth - playerWidth));
        player.setY(MathUtils.clamp(player.getY(), 0, worldHeight - playerHeight));

        // TODO: add collision logic (including walls)
    }

    private void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        // batch.draw(image, 4, 4, 8, 1);
        player.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        ballmanTexture.dispose();
    }
}
