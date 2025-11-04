package io.github.yetti_eng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public final class InputHelper {
    public static boolean anyOfTheseKeysPressed(int... keys) {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean moveUpPressed() {
        return anyOfTheseKeysPressed(Input.Keys.UP, Input.Keys.W);
    }

    public static boolean moveDownPressed() {
        return anyOfTheseKeysPressed(Input.Keys.DOWN, Input.Keys.S);
    }

    public static boolean moveLeftPressed() {
        return anyOfTheseKeysPressed(Input.Keys.LEFT, Input.Keys.A);
    }

    public static boolean moveRightPressed() {
        return anyOfTheseKeysPressed(Input.Keys.RIGHT, Input.Keys.D);
    }
}
