package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity {
    private static final float PLAYER_SPEED  = 2.5f;

    public Player(Texture tex, float x, float y, float width, float height) {
        super(tex, x, y, width, height, PLAYER_SPEED);
    }
}
