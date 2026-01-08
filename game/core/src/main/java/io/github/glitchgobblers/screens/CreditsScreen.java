package io.github.glitchgobblers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.glitchgobblers.YettiGame;

import java.util.ArrayList;

import static io.github.glitchgobblers.YettiGame.scaled;

public class CreditsScreen implements Screen {
  private final YettiGame game;
  private final Stage stage;

  private final ArrayList<Label> creditLabels = new ArrayList<>();

  public CreditsScreen(final YettiGame game) {
    this.game = game;
    stage = new Stage(game.viewport, game.batch);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);

    TextButton menuButton = new TextButton("Return to Menu", new TextButton.TextButtonStyle(null, null, null, game.font));
    menuButton.setPosition(scaled(16) / 2, scaled(1), Align.center);
    menuButton.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        game.setScreen(new MenuScreen(game));
        dispose();
        return true;
      }
    });
    stage.addActor(menuButton);

    // Left column
    addCreditHyperlinkLabel(
        "LibGDX (Apache License 2.0)", "https://github.com/libgdx/libgdx",
        scaled(0.5f), scaled(7f)
    );
    addCreditHyperlinkLabel(
        "Door Slam (Attribution 4)", "https://freesound.org/people/bennstir/sounds/80929/",
        scaled(0.5f), scaled(6f)
    );
    addCreditHyperlinkLabel(
        "Super Retro World (Custom licence)", "https://gif-superretroworld.itch.io/interior-pack",
        scaled(0.5f), scaled(5f)
    );
    addCreditHyperlinkLabel(
        "Roboto (Open Font License 1.1)", "https://github.com/googlefonts/roboto-3-classic/",
        scaled(0.5f), scaled(4f)
    );
    addCreditHyperlinkLabel(
        "(Roboto copyright notice)", "https://github.com/googlefonts/roboto-3-classic/blob/main/OFL.txt",
        scaled(1.5f), scaled(3f)
    );
  }

  private void addCreditHyperlinkLabel(String text, String href, float x, float y) {
    Label label = new Label(text, new Label.LabelStyle(game.fontSmall, Color.CYAN.cpy()));
    label.setPosition(x, y);
    label.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.net.openURI(href);
      }
    });
    creditLabels.add(label);
    stage.addActor(label);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0.4f, 0.4f, 0.4f, 1f);
    game.viewport.apply();
    game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    game.batch.begin();
    game.font.draw(game.batch, "Credits", 0, scaled(8.5f), scaled(16), Align.center, false);

    creditLabels.forEach(l -> l.draw(game.batch, 1.0f));

    game.fontSmall.draw(game.batch, "Click a link to go to the source", scaled(9f), scaled(2.5f));

    game.batch.end();

    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    game.viewport.update(width, height, true);
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void hide() {
  }

  @Override
  public void dispose() {
    stage.dispose();
  }
}
