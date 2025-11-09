package io.github.yetti_eng.events;

import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.screens.GameScreen;

public class HiddenDeductPointsEvent extends Event {
    @Override
    public boolean activate(GameScreen screen, Player player, Item item) {
        if (!item.isUsed()){
            item.show();
            screen.getSlipSfx().play(screen.getGame().volume);
            screen.spawnInteractionMessage("Tripped over in a water spill (" + getScoreModifier() + ")");
        }
        return true;
    }

    @Override
    public int getScoreModifier() {
        return -50;
    }
}
