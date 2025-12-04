package io.github.yetti_eng.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.yetti_eng.YettiGame;

import static io.github.yetti_eng.YettiGame.scaled;

public class LoseScreen implements Screen {
  private final YettiGame game;
  private int score;
  private Stage stage;
  private Texture texUp, texDown;

  public LoseScreen(final YettiGame game) {
    this.game = game;
  }

  @Override
  public void show() {
    score = game.calculateFinalScore();
    stage = new Stage(game.viewport, game.batch);
    Gdx.input.setInputProcessor(stage);


    TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

    Pixmap pmUp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pmUp.setColor(new Color(0f, 0f, 0f, 0.6f));
    pmUp.fill();
    texUp = new Texture(pmUp);
    pmUp.dispose();

    Pixmap pmDown = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pmDown.setColor(new Color(1f, 1f, 1f, 0.25f));
    pmDown.fill();
    texDown = new Texture(pmDown);
    pmDown.dispose();

    style.up   = new TextureRegionDrawable(new TextureRegion(texUp));
    style.down = new TextureRegionDrawable(new TextureRegion(texDown));
    style.over = style.down;
    style.font = game.fontBorderedSmall;
    style.fontColor = Color.WHITE.cpy();


    TextButton retryBtn = new TextButton("Retry", style);
    TextButton menuBtn = new TextButton("Main Menu", style);

    float btnW = scaled(4f), btnH = scaled(1.2f);
    float centerX = game.viewport.getWorldWidth() / 2f;
    float y = scaled(2.0f);

    retryBtn.setSize(btnW, btnH);
    menuBtn.setSize(btnW, btnH);

    retryBtn.setPosition(centerX - btnW - scaled(0.5f), y);
    menuBtn.setPosition(centerX +           scaled(0.5f), y);

    retryBtn.addListener(new ChangeListener() {
      @Override public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
        game.setScreen(new GameScreen(game)); // restart gameplay
        dispose();
      }
    });
    menuBtn.addListener(new ChangeListener() {
      @Override public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
        game.setScreen(new MenuScreen(game)); // back to main menu
        dispose();
      }
    });

    stage.addActor(retryBtn);
    stage.addActor(menuBtn);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0.4f, 0.15f, 0.2f, 1f);

    game.viewport.apply();
    game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    game.batch.begin();
    game.font.draw(game.batch, "You lost :(", 0, scaled(5.5f), scaled(16), Align.center, false);
    game.font.draw(game.batch, "Score: " + score, 0, scaled(4.5f), scaled(16), Align.center, false);
    game.batch.end();
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    game.viewport.update(width, height, true);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    if (stage != null) stage.dispose();
    if (texUp != null) texUp.dispose();
    if (texDown != null) texDown.dispose();
  }
}
