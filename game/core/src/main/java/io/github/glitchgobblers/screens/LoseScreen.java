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

public class LoseScreen implements Screen {
  private final YettiGame game;
  private final int baseScore;
  private final String playerName;

  private Stage stage;
  private Table leaderboardTable;

  private Label.LabelStyle textStyle;

  private BitmapFont fontLarge;
  private BitmapFont fontSmall;
  private Texture solidBlackTex;

  private final Leaderboard leaderboard = new Leaderboard();
  private boolean scoreSaved = false;

  public LoseScreen(YettiGame game, int finalScore, String playerName) {
    this.game = game;
    this.baseScore = finalScore;
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

    int penalisedScore = Math.max(0, baseScore / 3); // nerfed score to punish players for losing

    root.add(new Label("YOU LOSE!", titleStyle)).colspan(2).align(Align.center);
    root.row();
    root.add(new Label("Score: " + penalisedScore, textStyle)).colspan(2).align(Align.center);
    root.row();
    root.add(new Label("Saved for: " + playerName, textStyle)).colspan(2).align(Align.center);
    root.row().padTop(12);

    if (!scoreSaved) {
      String id = identity.getOrCreateId();
      identity.setName(playerName);
      leaderboard.submit(id, playerName, penalisedScore);
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


    TextButton retry = new TextButton("Retry", buttonStyle);
    retry.addListener(new ChangeListener() {
      @Override public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new GameScreen(game));
        dispose();
      }
    });
    root.add(retry).colspan(2).width(260);
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
    pm.setColor(0f, 0f, 0f, 1f);
    pm.fill();
    Texture t = new Texture(pm);
    pm.dispose();
    return t;
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.25f, 0.05f, 0.07f, 1f);
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
