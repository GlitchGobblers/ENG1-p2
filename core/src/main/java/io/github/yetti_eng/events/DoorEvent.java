package io.github.yetti_eng.events;

import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;

public class DoorEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        // If the player has got the check-in code, unlock the door
        if (player.hasUsedItem("checkin_code")) {
            game.spawnInteractionMessage("Unlocked door with check-in code");
            item.disable();
            item.hide();
            return true;
        }
        return false;
    }

    @Override
    public int getScoreModifier() {
        return 0;
    }
}
