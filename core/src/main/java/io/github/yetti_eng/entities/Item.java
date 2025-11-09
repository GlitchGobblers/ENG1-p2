package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.events.Event;

/**
 * Defines an item, or event trigger
 * When player interacts with the item, it activates the event and marks the item as used
 * Each item has an ID for event conditions
 */
public class Item extends Entity {
    private final Event event;
    private boolean used;

    public final String ID;

    /**
     *
     * @param event what activates because of the item
     * @param ID way to identify each item
     * @param tex items texture
     * @param x x position
     * @param y y position
     * @param width
     * @param height
     * @param speed usually 0/stationary
     * @param solid if item should be collided with
     */
    public Item(Event event, String ID, Texture tex, float x, float y, float width, float height, float speed, boolean solid) {
        super(tex, x, y, width, height, speed, solid);
        this.event = event;
        this.ID = ID;
    }

    public Item(Event event, String ID, Texture tex, float x, float y, float speed, boolean hidden, boolean solid) {
        this(event, ID, tex, x, y, 1, 1, speed, solid);
        if (hidden) hide();
    }

    public Item(Event event, String ID, Texture tex, float x, float y, boolean hidden, boolean solid) {
        this(event, ID, tex, x, y, 0, hidden, solid);
    }

    public Item(Event event, String ID, Texture tex, float x, float y) {
        this(event, ID, tex, x, y, false, false);
    }

    /**
     * Player interacts with the item, marking item as used.
     * @param game main game
     * @param player current player
     */
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
