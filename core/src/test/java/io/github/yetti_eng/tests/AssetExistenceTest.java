package io.github.yetti_eng.tests;

import com.badlogic.gdx.Gdx;
import io.github.yetti_eng.GdxTestBase;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssetExistenceTest extends GdxTestBase {

    @Test
    public void testCriticalAssetsExist() {
        // List of critical assets referenced in GameScreen.java
        // THIS MAY NEED TO BE UPDATED AS PROJECT DEVELOPS
        String[] requiredAssets = {
            "character/player_up.png",
            "character/player_down.png",
            "character/yeti.png",
            "item/checkin_code.png",
            "item/door.png",
            "map/map.tmx",
            "audio/duck_quack.mp3",
            "Roboto.ttf"
        };

        // Check if "assets" folder is found
        for (String path : requiredAssets) {
            boolean exists = Gdx.files.internal(path).exists();

            if (!exists) {
                System.err.println("Asset NOT found: " + Gdx.files.internal(path).file().getAbsolutePath());
            }

            assertTrue(exists, "Critical asset missing: " + path);
        }
    }
}