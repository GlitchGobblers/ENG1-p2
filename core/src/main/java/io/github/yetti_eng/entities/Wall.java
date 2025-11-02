package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;

public class Wall extends Entity {
    private static final float WALL_SPEED = 0.0f;

    public Wall(Texture tex, float x, float y, float width, float height) {
        super(tex, x, y, width, height, WALL_SPEED);
    }

    public Wall(Texture tex, float x, float y) {
        this(tex, x, y, 1, 1);
    }
}
