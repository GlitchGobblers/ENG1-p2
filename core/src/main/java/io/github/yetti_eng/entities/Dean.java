package io.github.yetti_eng.entities;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.screens.LoseScreen;

import java.util.ArrayList;

public class Dean extends Entity {
    private static final float PLAYER_SPEED = 1.5f;

    final ArrayList<Item> usedItems = new ArrayList<>();

    public Dean(Texture tex, float x, float y) {
        // Dean should be slightly larger than 1 tile to appear imposing (he has no collision)
        super(tex, x, y, 1.1f, 1.1f, PLAYER_SPEED, false);
    }

    /**
     * Run the logic for when the Dean gets the Player and the Player loses.
     * @param game The current YettiGame object.
     */
    public void getsPlayer(final YettiGame game) {
        game.timer.finish();
        Screen prevScreen = game.getScreen();
        game.setScreen(new LoseScreen(game));
        prevScreen.dispose();
    }

    /**
     * Calculate this Dean's movement towards the Player.
     * @param player The current Player object.
     */
    public void calculateMovement(final Player player) {
        resetMovement();
        // Calculate vertical and horizontal distance to Player
        float xDistance = player.getX() - this.getX();
        float yDistance = player.getY() - this.getY();
        // Set the Dean's movement to this distance
        addMovement(xDistance, yDistance);
        // Normalise the movement vector so the dean only moves 1 tile
        normaliseMovement();
    }
}
