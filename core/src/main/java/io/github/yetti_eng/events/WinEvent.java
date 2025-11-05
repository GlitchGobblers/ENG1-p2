package io.github.yetti_eng.events;

import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.screens.WinScreen;

public class WinEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        game.setScreen(new WinScreen(game));
        game.getScreen().dispose();
        return true;
    }

    @Override
    public int getScoreModifier() {
        return 0;
    }
}
