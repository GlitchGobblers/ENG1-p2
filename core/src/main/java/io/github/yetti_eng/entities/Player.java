package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Player extends Entity {
    private static final float PLAYER_SPEED = 6f;

    final ArrayList<Item> usedItems = new ArrayList<>();

    public Player(Texture tex, float x, float y) {
        super(tex, x, y, 1f, 1f, PLAYER_SPEED, false);
    }

    public boolean hasUsedItem(String itemID) {
        return usedItems.stream().anyMatch(i -> i.ID.equals(itemID));
    }
}
