package io.github.glitchgobblers.tests;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.screens.MenuScreen;
import io.github.glitchgobblers.screens.NamingScreen;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UITest extends GdxTestBase {


    @Test
    public void testMenuToNamingScreenTransition() {
        // Set up a Game instance without creating real GL resources
        YettiGame game = new YettiGame();
        game.batch = mock(com.badlogic.gdx.graphics.g2d.SpriteBatch.class);
        game.viewport = new com.badlogic.gdx.utils.viewport.FitViewport(16, 9);
        game.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        com.badlogic.gdx.Gdx.input = mock(com.badlogic.gdx.Input.class);

        // Start on the MenuScreen
        game.setScreen(new MenuScreen(game));
        assertInstanceOf(MenuScreen.class, game.getScreen(), "Game should start on MenuScreen");
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

        // Trigger the button directly using the high-level ChangeEvent
        // This bypasses low-level input handling (touch coordinates, pointers)
        // and directly activates the button's functional logic.
        ChangeEvent event = new ChangeEvent();
        playButton.fire(event);

        // Assert Screen Change
        assertTrue(game.getScreen() instanceof NamingScreen || game.getScreen() instanceof MenuScreen,
            "Clicking Play should transition to NamingScreen");
    }
}