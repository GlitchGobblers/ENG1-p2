package io.github.yetti_eng;

import com.badlogic.gdx.Gdx;

public final class InputHelper {
    public static boolean anyOfTheseKeysPressed(int... keys) {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }
}
