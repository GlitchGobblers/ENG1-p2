package io.github.glitchgobblers.tests;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.screens.MenuScreen;
import io.github.glitchgobblers.screens.NamingScreen;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class UITest extends GdxTestBase {

    @Test
    public void testMenuToNamingScreenTransition() {
        // Set up a Game instance without creating real GL resources
        YettiGame game = new YettiGame();
        game.batch = Mockito.mock(com.badlogic.gdx.graphics.g2d.SpriteBatch.class);
        game.viewport = new com.badlogic.gdx.utils.viewport.FitViewport(16, 9);
        game.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        com.badlogic.gdx.Gdx.input = Mockito.mock(com.badlogic.gdx.Input.class);

        // Start on the MenuScreen
        game.setScreen(new MenuScreen(game));
        assertTrue(game.getScreen() instanceof MenuScreen, "Game should start on MenuScreen");
        MenuScreen menuScreen = (MenuScreen) game.getScreen();

        // Find Play button
        Array<Actor> actors = menuScreen.getStage().getActors();
        TextButton playButton = null;
        for (Actor actor : actors) {
            if (actor instanceof TextButton) {
                playButton = (TextButton) actor;
                break;
            }
        }
        assertNotNull(playButton, "Play button should exist on stage");

        // Simulate click
        InputEvent eventDown = new InputEvent();
        eventDown.setType(InputEvent.Type.touchDown);
        playButton.fire(eventDown);

        InputEvent eventUp = new InputEvent();
        eventUp.setType(InputEvent.Type.touchUp);
        playButton.fire(eventUp);

        // Assert Screen Change
        assertTrue(game.getScreen() instanceof NamingScreen, "Clicking Play should transition to NamingScreen");
    }
}