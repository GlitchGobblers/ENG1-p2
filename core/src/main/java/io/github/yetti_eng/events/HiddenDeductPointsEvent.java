package io.github.yetti_eng.events;

import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;

public class HiddenDeductPointsEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        //event only occurs once and its effects remain
        if (!item.isUsed()) {
            item.show();
            game.spawnInteractionMessage("Knocked down by student (" + getScoreModifier() + ")");
        }
        return true;
    }

    @Override
    public int getScoreModifier() {
        return -50;
    }
}
