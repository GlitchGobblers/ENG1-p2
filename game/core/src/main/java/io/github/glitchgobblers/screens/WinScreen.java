package io.github.glitchgobblers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.identity;
import io.github.glitchgobblers.Leaderboard;

public class WinScreen implements Screen {
  private final YettiGame game;
  private final int finalScore;
  private final String playerName;

  private Stage stage;
  private Table leaderboardTable;

  private Label.LabelStyle textStyle;

  private BitmapFont fontLarge;
  private BitmapFont fontSmall;
  private Texture solidBlackTex;

  private final Leaderboard leaderboard = new Leaderboard();
  private boolean scoreSaved = false;

  public WinScreen(YettiGame game, int finalScore, String playerName) {
    this.game = game;
    this.finalScore = finalScore;
    this.playerName = (playerName == null || playerName.trim().isEmpty()) ? "Anonymous" : playerName.trim();
  }

  @Override
  public void show() {
    stage = new Stage(game.viewport, game.batch);
    Gdx.input.setInputProcessor(stage);


    fontLarge = new BitmapFont();
    fontSmall = new BitmapFont();
    Label.LabelStyle titleStyle = new Label.LabelStyle(fontLarge, Color.WHITE);
    textStyle  = new Label.LabelStyle(fontSmall, Color.WHITE);

    TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    buttonStyle.font = fontSmall;
    buttonStyle.fontColor = Color.WHITE;

    Table root = new Table();
    root.setFillParent(true);
    root.defaults().pad(8);
    stage.addActor(root);

    root.add(new Label("Victory!", titleStyle)).colspan(2).align(Align.center);
    root.row();
    root.add(new Label("Your score: " + finalScore, textStyle)).colspan(2).align(Align.center);
    root.row();
    root.add(new Label("Saved for: " + playerName, textStyle)).colspan(2).align(Align.center);
    root.row().padTop(12);


    if (!scoreSaved) {
      String id = identity.getOrCreateId();
      identity.setName(playerName);
      leaderboard.submit(id, playerName, finalScore);
      scoreSaved = true;
    }


    root.add(new Label("Top Scores", titleStyle)).colspan(2).align(Align.center);
    root.row();


    Table leaderboardWrapper = new Table();
    leaderboardWrapper.defaults().pad(6);
    leaderboardWrapper.pad(8);

    solidBlackTex = makeSolid();
    leaderboardWrapper.setBackground(new TextureRegionDrawable(new TextureRegion(solidBlackTex)));


    leaderboardTable = new Table();
    leaderboardTable.defaults().pad(4).left();
    leaderboardWrapper.add(leaderboardTable).growX();


    root.add(leaderboardWrapper).colspan(2).width(520).fillX();
    refreshLeaderboard();

    root.row().padTop(12);

    TextButton reset = new TextButton("Restart", buttonStyle);
    reset.addListener(new ChangeListener() {
      @Override public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new GameScreen(game));
        dispose();
      }
    });
    root.add(reset).colspan(2).width(260);
    root.row();

    TextButton back = new TextButton("Main Menu", buttonStyle);
    back.addListener(new ChangeListener() {
      @Override public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new MenuScreen(game));
        dispose();
      }
    });
    root.add(back).colspan(2).width(260);
  }

  private void refreshLeaderboard() {
    leaderboardTable.clear();

    leaderboardTable.add(new Label("#", textStyle)).width(30);
    leaderboardTable.add(new Label("Name", textStyle)).width(320);
    leaderboardTable.add(new Label("Score", textStyle)).width(100);
    leaderboardTable.row();

    Array<Leaderboard.Entry> top = leaderboard.top(10);
    for (int i = 0; i < top.size; i++) {
      Leaderboard.Entry e = top.get(i);
      leaderboardTable.add(new Label(String.valueOf(i + 1), textStyle)).width(30);
      leaderboardTable.add(new Label(e.name, textStyle)).width(320);
      leaderboardTable.add(new Label(String.valueOf(e.score), textStyle)).width(100);
      leaderboardTable.row();
    }
  }

  private Texture makeSolid() {
    Pixmap pm = new Pixmap(1, 1, Format.RGBA8888);
    pm.setColor((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0);
    pm.fill();
    Texture t = new Texture(pm);
    pm.dispose();
    return t;
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0.6f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.act(delta);
    stage.draw();
  }

  @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
  @Override public void pause() {}
  @Override public void resume() {}
  @Override public void hide() {}

  @Override
  public void dispose() {
    if (stage != null) stage.dispose();
    if (solidBlackTex != null) solidBlackTex.dispose();
    if (fontLarge != null) fontLarge.dispose();
    if (fontSmall != null) fontSmall.dispose();
  }
}
