package io.github.yetti_eng.events;

import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;

public class PointsEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        item.disable();
        game.spawnInteractionText(player, "Found Long Boi\n+" + getScoreModifier());
        return true;
    }

    @Override
    public int getScoreModifier() {
        // TODO placeholder value
        return 1000;
    }
}
