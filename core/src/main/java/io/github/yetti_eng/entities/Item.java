package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;
import io.github.yetti_eng.events.Event;

public class Item extends Entity {
    private final Event event;
    private boolean used;

    public Item(Event event, Texture tex, float x, float y, float width, float height, float speed, boolean solid) {
        super(tex, x, y, width, height, speed, solid);
        this.event = event;
    }

    public void interact(final Player player) {
        used = event.activate(this, player);
        if (used) {
            player.usedItems.add(this);
        }
    }

    // Make setSolid public for Items
    @Override
    public void setSolid(boolean solid) {
        super.setSolid(solid);
    }


}
