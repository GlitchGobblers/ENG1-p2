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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private FitViewport viewport;

    Texture ballmanTexture;
    Sprite playerSprite;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(16, 9);
        ballmanTexture = new Texture("ballman.png");
        playerSprite = new Sprite(ballmanTexture);
        playerSprite.setSize(1, 1);
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

    // TODO: move this somewhere sensible
    private static float BALLMAN_SPEED = 2.5f;

    private void input() {
        float speedThisFrame = BALLMAN_SPEED * Gdx.graphics.getDeltaTime();
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.RIGHT, Input.Keys.D)) {
            playerSprite.translateX(speedThisFrame);
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.LEFT, Input.Keys.A)) {
            playerSprite.translateX(-speedThisFrame);
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.UP, Input.Keys.W)) {
            playerSprite.translateY(speedThisFrame);
        }
        if (InputHelper.anyOfTheseKeysPressed(Input.Keys.DOWN, Input.Keys.S)) {
            playerSprite.translateY(-speedThisFrame);
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();

        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, worldWidth - playerWidth));
        playerSprite.setY(MathUtils.clamp(playerSprite.getY(), 0, worldHeight - playerHeight));
    }

    private void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(image, 4, 4, 8, 1);
        playerSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        ballmanTexture.dispose();
    }
}
