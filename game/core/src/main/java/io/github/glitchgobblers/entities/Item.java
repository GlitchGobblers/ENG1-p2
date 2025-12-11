package io.github.glitchgobblers.entities;

import com.badlogic.gdx.graphics.Texture;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.events.Event;
import io.github.glitchgobblers.screens.GameScreen;

public class Item extends Entity {
  private boolean used;
  private String key;

  public Item(Texture tex, float x, float y, float width, float height, boolean hidden, boolean solid) {
    super(tex, x, y, width, height, 0.0f, hidden, solid);

    if (hidden) {
      hide();
    }
  }

  public void addKey(String key) {
    this.key = key;
  }

  public void interact(YettiGame game,
                       GameScreen screen,
                       Player player,
                       Event event) {
    boolean justUsed = event.activate(screen, player, this, game);

    if (justUsed) {
      used = true;
      player.inventory.add(key);
      event.modifyScore(game);
    }
  }

  public boolean isUsed() {
    return used;
  }

  // Make setSolid public for Items
  @Override
  public void setSolid(boolean solid) {
    super.setSolid(solid);
  }
}
