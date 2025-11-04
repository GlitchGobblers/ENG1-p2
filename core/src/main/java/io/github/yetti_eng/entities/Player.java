package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity {
    private static final float PLAYER_SPEED = 2.5f;

    public Player(Texture tex, float x, float y) {
        // Player should be slightly smaller than 1 tile so as not to get stuck on walls
        super(tex, x, y, 0.9f, 0.9f, PLAYER_SPEED);
    }
}
