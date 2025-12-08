package io.github.glitchgobblers.entities;

import com.badlogic.gdx.graphics.Texture;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.events.Event;
import io.github.glitchgobblers.screens.GameScreen;

public class Item extends Entity {
  public final String id;

  public Item(Event event, String id, Texture tex, float x, float y, float width, float height, boolean hidden, boolean solid) {
    super(tex, x, y, width, height, 0.0f, solid);
    this.id = id;
    if (hidden) hide();
  }

  public Item(Event event, String id, Texture tex, float x, float y, float width, float height) {
    this(event, id, tex, x, y, width, height, false, false);
  }

  public Item(Event event, String id, Texture tex, float x, float y) {
    this(event, id, tex, x, y, 1, 1);
  }

  public final void interact(final YettiGame game, final GameScreen screen, Player player) {
      /*boolean justUsed = event.activate(screen, player, this);
      if (justUsed) {
          used = true;
          player.inventory.add(this);
          event.modifyScore(game);
      }*/
  }

  // Make setSolid public for Items
  @Override
  public void setSolid(boolean solid) {
    super.setSolid(solid);
  }
}
