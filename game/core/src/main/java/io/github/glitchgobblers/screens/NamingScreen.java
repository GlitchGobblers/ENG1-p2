package io.github.glitchgobblers.screens;

import static io.github.glitchgobblers.YettiGame.scaled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.identity;

public class NamingScreen implements Screen {
  private final YettiGame game;
  private final Stage stage;

  private TextField nameField;

  private Texture btnBgTex;
  private Texture btnBorderTex;
  private Texture btnHoverBgTex;
  private Texture btnHoverBorderTex;
  private Texture fieldBgTex;
  private Texture fieldBorderTex;

  private String errorText = "";
  private static final int MAX_LEN = 16;

  public NamingScreen(YettiGame game) {
    this.game = game;
    this.stage = new Stage(game.viewport, game.batch);
  }

  @Override
  public void show() {
    String current = identity.getName();
    if (current != null && !current.isEmpty() && !"Player".equals(current)) {
      game.playerName = current;
      game.setScreen(new GameScreen(game));
      dispose();
      return;
    }

    Gdx.input.setInputProcessor(stage);

    btnBgTex = makeColorTex(1, 1, 0f, 0f, 0f, 0.4f);
    btnBorderTex = makeColorTex(1, 1, 1f, 1f, 1f, 0.9f);
    btnHoverBgTex = makeColorTex(1, 1, 0.3f, 0.3f, 0.3f, 0.6f);
    btnHoverBorderTex = makeColorTex(1, 1, 1f, 1f, 1f, 1f);
    fieldBgTex = makeColorTex(1, 1, 1f, 1f, 1f, 0.18f);
    fieldBorderTex = makeColorTex(1, 1, 1f, 1f, 1f, 0.9f);

    TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
    btnStyle.up = bordered(btnBgTex, btnBorderTex);
    btnStyle.down = bordered(btnHoverBgTex, btnHoverBorderTex);
    btnStyle.over = bordered(btnHoverBgTex, btnHoverBorderTex);
    btnStyle.font = game.font;

    TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
    tfStyle.font = game.font;
    tfStyle.fontColor = Color.WHITE;
    tfStyle.background = bordered(fieldBgTex, fieldBorderTex);
    tfStyle.cursor = new TextureRegionDrawable(fieldBorderTex);


    nameField = new TextField("", tfStyle);
    nameField.setMessageText("Type name…");
    nameField.setMaxLength(MAX_LEN);
    nameField.setAlignment(Align.center);

    float fieldW = scaled(8f);
    float fieldH = scaled(1.4f);
    nameField.setSize(fieldW, fieldH);
    nameField.setPosition(scaled(16) / 2f - fieldW / 2f, scaled(6f) - fieldH / 2f);

    TextButton startButton = new TextButton("Start", btnStyle);
    startButton.setPosition(scaled(16) / 2f, scaled(4.4f), Align.center);
    startButton.addListener(new InputListener() {
      @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
        confirm();
        return true;
      }
    });

    TextButton backButton = new TextButton("Back", btnStyle);
    backButton.setPosition(scaled(16) / 2f, scaled(2.9f), Align.center);
    backButton.addListener(new InputListener() {
      @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) {
        game.setScreen(new MenuScreen(game));
        dispose();
        return true;
      }
    });

    nameField.setTextFieldListener((f, c) -> {
      if (c == '\r' || c == '\n') {
        confirm();
      }
    });
    stage.addListener(new InputListener() {
      @Override public boolean keyDown(InputEvent e, int keycode) {
        if (keycode == Input.Keys.ENTER) {
          confirm();
          return true;
        } else {
          return false;
        }
      }
    });

    stage.addActor(nameField);
    stage.addActor(startButton);
    stage.addActor(backButton);
    stage.setKeyboardFocus(nameField);
  }

  private TextureRegionDrawable bordered(Texture bg, Texture border) {
    return new TextureRegionDrawable(bg) {
      @Override public void draw(Batch batch, float x, float y, float w, float h) {
        super.draw(batch, x, y, w, h);
        batch.draw(border, x, y, w, 1);
        batch.draw(border, x, y + h - 1, w, 1);
        batch.draw(border, x, y, 1, h);
        batch.draw(border, x + w - 1, y, 1, h);
      }
    };
  }

  private Texture makeColorTex(int w, int h, float r, float g, float b, float a) {
    Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
    p.setColor(r, g, b, a);
    p.fill();
    Texture t = new Texture(p);
    p.dispose();
    return t;
  }

  private void confirm() {
    String raw = nameField.getText();
    String cleaned = sanitise(raw);

    if (cleaned.isEmpty()) {
      errorText = "Name can't be empty.";
      return;
    }

    identity.setName(cleaned);
    game.playerName = cleaned;

    game.setScreen(new GameScreen(game));
    dispose();
  }

  private String sanitise(String s) {
    if (s == null) return "";
    String t = s.replaceAll("\\p{Cntrl}", "").trim();
    if (t.length() > MAX_LEN) t = t.substring(0, MAX_LEN);
    return t;
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    game.viewport.apply();
    game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    game.batch.begin();

    game.font.draw(game.batch, "Choose Your Name",
      0, scaled(8f), scaled(16), Align.center, false);

    if (!errorText.isEmpty()) {
      game.font.setColor(Color.RED);
      game.font.draw(
        game.batch,
        errorText,
        0,
        scaled(2.2f),
        scaled(16),
        Align.center,
        false
      );
      game.font.setColor(Color.WHITE);
    }

    game.batch.end();

    stage.act(delta);
    stage.draw();
  }

  @Override public void resize(int width, int height) {
    game.viewport.update(width, height, true);
  }

  @Override public void pause() {}
  @Override public void resume() {}
  @Override public void hide() {}

  @Override
  public void dispose() {
    stage.dispose();
    if (btnBgTex != null) btnBgTex.dispose();
    if (btnBorderTex != null) btnBorderTex.dispose();
    if (btnHoverBgTex != null) btnHoverBgTex.dispose();
    if (btnHoverBorderTex != null) btnHoverBorderTex.dispose();
    if (fieldBgTex != null) fieldBgTex.dispose();
    if (fieldBorderTex != null) fieldBorderTex.dispose();
  }
}
