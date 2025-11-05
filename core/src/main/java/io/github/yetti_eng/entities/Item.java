package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.events.Event;

public class Item extends Entity {
    private final Event event;
    private boolean used;

    public final String ID;

    public Item(Event event, String ID, Texture tex, float x, float y, float width, float height, float speed, boolean solid) {
        super(tex, x, y, width, height, speed, solid);
        this.event = event;
        this.ID = ID;
    }

    public Item(Event event, String ID, Texture tex, float x, float y, float speed, boolean solid) {
        this(event, ID, tex, x, y, 1, 1, speed, solid);
    }

    public Item(Event event, String ID, Texture tex, float x, float y, boolean solid) {
        this(event, ID, tex, x, y, 0, solid);
    }

    public Item(Event event, String ID, Texture tex, float x, float y) {
        this(event, ID, tex, x, y, false);
    }

    public final void interact(final YettiGame game, Player player) {
        used = event.activate(game, player, this);
        if (used) {
            player.usedItems.add(this);
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
