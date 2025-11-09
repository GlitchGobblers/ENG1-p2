package io.github.yetti_eng.events;

import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;

public class DoorEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        // If the player has used a key, unlock the door
        if (player.hasUsedItem("key")) {
            game.spawnInteractionMessage("Unlocked door with check-in code");
            item.disable();
            item.hide();
            item.setSolid(false);
            return true;
        }
        return false;
    }

    @Override
    public int getScoreModifier() {
        return 0;
    }
}
