package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.events.Event;
import io.github.yetti_eng.screens.GameScreen;

public class Item extends Entity {
    private boolean used;


    public Item(Texture tex, float x, float y, float width, float height, boolean hidden, boolean solid) {
        super(tex, x, y, width, height, 0.0f, solid);
        if (hidden) hide();
    }

    public final void interact(final YettiGame game, final GameScreen screen, Player player, Event event) {
        boolean justUsed = event.activate(screen, player, this);
        if (justUsed) {
            used = true;
            player.inventory.add(this);
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
