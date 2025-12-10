/*package io.github.glitchgobblers.tests;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.entities.Item;
import io.github.glitchgobblers.entities.Player;
import io.github.glitchgobblers.events.DoorEvent;
import io.github.glitchgobblers.screens.GameScreen;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class EventSystemTest extends GdxTestBase {

    @Test
    public void testDoorUnlockWithKey() {
        // Mock dependencies (Textures, Sounds, Game, Screen)
        Texture mockTex = Mockito.mock(Texture.class);
        Sound mockSound = Mockito.mock(Sound.class);
        YettiGame mockGame = Mockito.mock(YettiGame.class);
        GameScreen mockScreen = Mockito.mock(GameScreen.class);

        // Configure Mocks
        when(mockScreen.getDoorSfx()).thenReturn(mockSound);
        when(mockScreen.getGame()).thenReturn(mockGame);
        when(mockScreen.getDoorframeTexture()).thenReturn(mockTex);

        // Give the texture a fake size so Entity math works
        when(mockTex.getWidth()).thenReturn(32);
        when(mockTex.getHeight()).thenReturn(32);

        mockGame.volume = 1.0f;

        // Create Player and Items
        Player player = new Player(mockTex, 0, 0);

        // The key (checkin code)
        Item keyItem = new Item(null, "checkin_code", mockTex, 0, 0);

        // The door
        Item doorItem = new Item(new DoorEvent(), "door", mockTex, 10, 10);

        // Test when Player has NO key
        boolean successNoKey = new DoorEvent().activate(mockScreen, player, doorItem);
        assertFalse(successNoKey, "Door should NOT open without check-in code");

        // Test when Player HAS key
        player.inventory.add(keyItem);

        boolean successWithKey = new DoorEvent().activate(mockScreen, player, doorItem);
        assertTrue(successWithKey, "Door SHOULD open with check-in code");

        // Verify audio was played
        Mockito.verify(mockSound).play(1.0f);
    }
}*/