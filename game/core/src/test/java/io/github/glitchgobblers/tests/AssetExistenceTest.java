package io.github.glitchgobblers.tests;

import com.badlogic.gdx.Gdx;
import io.github.glitchgobblers.GdxTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssetExistenceTest extends GdxTestBase {

    @Test
    public void testCriticalAssetsExist() {
        // List of critical assets referenced in GameScreen.java
        // THIS MAY NEED TO BE UPDATED AS PROJECT DEVELOPS
        String[] requiredAssets = {
            // Character sprites
            "character/player_up.png",
            "character/player_down.png",
            "character/player_left.png",
            "character/player_right.png",
            "character/yeti.png",

            // Items
            "item/exit.png",
            "item/checkin_code.png",
            "item/door.png",
            "item/doorframe.png",
            "item/long_boi.png",
            "item/water_spill.png",

            // Map
            "map/map.tmx",

            // UI textures
            "ui/pause.png",
            "ui/slider_background.png",
            "ui/slider_knob.png",

            // Achievement icons
            "ui/ach_welcome.png",
            "ui/ach_pause.png",
            "ui/ach_dean.png",
            "ui/ach_timeout.png",
            "ui/ach_event.png",
            "ui/ach_completionist.png",

            // Audio
            "audio/duck_quack.mp3",
            "audio/paper_rustle.wav",
            "audio/dorm_door_opening.wav",
            "audio/cartoon_quick_slip.wav",
            "audio/deep_growl_1.wav",

            // Fonts
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