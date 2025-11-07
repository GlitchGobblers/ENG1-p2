package io.github.yetti_eng.events;

import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;

public class KeyEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        item.disable();
        item.hide();
        game.spawnInteractionMessage(player, "Got check-in code");
        return true;
    }

    @Override
    public int getScoreModifier() {
        return 0;
    }
}
