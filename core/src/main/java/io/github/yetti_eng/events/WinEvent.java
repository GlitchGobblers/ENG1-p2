package io.github.yetti_eng.events;

import com.badlogic.gdx.Screen;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.screens.WinScreen;

public class WinEvent extends Event {
    @Override
    public boolean activate(YettiGame game, Player player, Item item) {
        Screen prevScreen = game.getScreen();
        game.setScreen(new WinScreen(game));
        prevScreen.dispose();
        return true;
    }

    @Override
    public int getScoreModifier() {
        return 0;
    }
}
