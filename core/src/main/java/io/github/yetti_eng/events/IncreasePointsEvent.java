package io.github.yetti_eng.events;

import io.github.yetti_eng.EventCounter;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.screens.GameScreen;

public class IncreasePointsEvent extends Event {
    @Override
    public boolean activate(GameScreen screen, Player player, Item item) {
        EventCounter.incrementPositive(); //positive event recorded
        item.disable();
        item.hide();
        screen.getQuackSfx().play(screen.getGame().volume);
        screen.spawnInteractionMessage("Found Long Boi (+" + getScoreModifier() + ")");
        return true;
    }

    @Override
    public int getScoreModifier() {
        // TODO placeholder value
        return 100;
    }
}
