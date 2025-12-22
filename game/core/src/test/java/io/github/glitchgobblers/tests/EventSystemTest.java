package io.github.glitchgobblers.tests;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.YettiGame;
import io.github.glitchgobblers.entities.Item;
import io.github.glitchgobblers.entities.Player;
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

        // The door starts solid/locked
        Item doorItem = new Item(mockTex, 10, 10, 1f, 1f, false, true);

        // Door event stub that unlocks when player holds "checkin_code"
        DoorEventStub doorEvent = new DoorEventStub("checkin_code");

        // Test when Player has NO key
        boolean successNoKey = doorEvent.activate(mockScreen, player, doorItem);
        assertFalse(successNoKey, "Door should NOT open without check-in code");
        assertTrue(doorItem.isSolid(), "Door remains locked/solid without key");

        // Test when Player HAS key
        player.addKey("checkin_code");

        boolean successWithKey = doorEvent.activate(mockScreen, player, doorItem);
        assertTrue(successWithKey, "Door SHOULD open with check-in code");
        assertFalse(doorItem.isSolid(), "Door becomes passable after unlocking");
    }

    /**
     * Minimal stub to mirror the lock/key behaviour expected for a door.
     */
    private static class DoorEventStub {
        private final String requiredKey;

        DoorEventStub(String requiredKey) {
            this.requiredKey = requiredKey;
        }

        boolean activate(GameScreen screen, Player player, Item door) {
            boolean hasKey = player.hasKey(requiredKey);
            if (hasKey) {
                door.setSolid(false);
            }
            return hasKey;
        }
    }
}