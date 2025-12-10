package io.github.glitchgobblers.screens;

import static io.github.glitchgobblers.YettiGame.scaled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.achievements.Achievement;
import java.util.List;

public class AchievementsScreen implements Screen {
  private final YettiGame game;
  private final Stage stage;

  public AchievementsScreen(YettiGame game) {
    this.game = game;
    this.stage = new Stage(game.viewport, game.batch);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);

    Table root = new Table();
    root.setFillParent(true);
    root.top().pad(scaled(0.4f));
    root.defaults().pad(scaled(0.2f)).left();

    Label title = new Label("Achievements",
        new Label.LabelStyle(game.fontBordered, Color.WHITE.cpy()));
    title.setAlignment(Align.center);
    root.add(title).colspan(3).center().padBottom(scaled(0.4f)).row();

    // Scrollable list container to ensure visibility even if more achievements are added
    Table listTable = new Table();
    listTable.top().defaults().pad(scaled(0.15f)).left();

    List<Achievement> achievements = game.achievements.getAchievements();
    for (Achievement achievement : achievements) {
      Texture icon = game.achievements.getIcon(achievement.getId());
      Image iconImage = new Image(icon);
      iconImage.setSize(scaled(0.7f), scaled(0.7f));

      Label name = new Label(achievement.getName(),
          new Label.LabelStyle(game.fontBorderedSmall, Color.WHITE.cpy()));
      Label desc = new Label(achievement.getDescription(),
          new Label.LabelStyle(game.fontSmall, Color.LIGHT_GRAY.cpy()));
      Label status = new Label(
          achievement.isUnlocked() ? "Unlocked" : "Locked",
          new Label.LabelStyle(game.fontBorderedSmall,
              achievement.isUnlocked() ? Color.GREEN.cpy() : Color.RED.cpy()));

      Table textTable = new Table();
      textTable.add(name).left().row();
      textTable.add(desc).left();

      listTable.add(iconImage).size(scaled(0.9f), scaled(0.9f));
      listTable.add(textTable).left().growX().padLeft(scaled(0.2f));
      listTable.add(status).right().padLeft(scaled(0.2f));
      listTable.row();
    }

    ScrollPane scrollPane = new ScrollPane(listTable);
    scrollPane.setFadeScrollBars(false);
    scrollPane.setScrollingDisabled(true, false);
    scrollPane.setOverscroll(false, false);

    root.add(scrollPane).colspan(3).grow().padBottom(scaled(0.3f)).row();

    TextButton backButton = new TextButton(
        "Back",
        new TextButton.TextButtonStyle(null, null, null, game.font));
    backButton.addListener(new ChangeListener() {
      @Override public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new MenuScreen(game));
        dispose();
      }
    });

    root.add(backButton).colspan(3).center().padTop(scaled(0.2f));
    stage.addActor(root);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0.12f, 0.12f, 0.16f, 1f);
    game.viewport.apply();
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    game.viewport.update(width, height, true);
    stage.getViewport().update(width, height, true);
  }

  @Override public void pause() {}

  @Override public void resume() {}

  @Override public void hide() {}

  @Override
  public void dispose() {
    stage.dispose();
  }
}

