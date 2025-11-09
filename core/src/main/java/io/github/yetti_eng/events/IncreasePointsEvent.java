package io.github.yetti_eng.events;

import io.github.yetti_eng.EventCounter;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;

/**
 * Defines positive events which increase the points of the player.
 *      Increments positive event counter.
 *      Once activated, item is disabled and hidden from screen to prevent
 *      reactivation.
 */
public class IncreasePointsEvent extends Event {
    /**
     * Activates event after item is interacted with
     *
     * @param game the main game
     * @param player The current player object.
     * @param item The item triggering this event.
     * @return true if event is activated correctly
     */
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        EventCounter.incrementPositive(); //positive event recorded
        item.disable(); //disable to prevent reactivation
        item.hide();
        //message to user displayed with event details
        game.spawnInteractionMessage("Found Long Boi (+" + getScoreModifier() + ")");
        return true;
    }

    /**
     *Returns the number of points the player gains from this event
     *
     * @return score modifier
     */
    @Override
    public int getScoreModifier() {
        // TODO placeholder value
        return 100;
    }
}
