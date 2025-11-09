package io.github.yetti_eng.events;

import io.github.yetti_eng.EventCounter;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;

public class DoorEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        // If the player has used a key, unlock the door
        if (player.hasUsedItem("key")) {
            EventCounter.incrementNegative(); //increment negative counter
            game.spawnInteractionMessage("Unlocked door with check-in code");
            item.disable();
            item.hide(); //door disappears
            item.setSolid(false); //player can walk through it
            return true;
        }
        return false; //no effect on item without key
    }

    @Override
    public int getScoreModifier() {
        return 0;
    }
}
