package io.github.glitchgobblers.tests;

import com.badlogic.gdx.graphics.Texture;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.entities.Dean;
import io.github.glitchgobblers.entities.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class DeanTest extends GdxTestBase {
    @Test
    public void testDeanPursuitLogic() {
        //mock texture
        Texture tex = Mockito.mock(Texture.class);
        Mockito.when(tex.getWidth()).thenReturn(32);
        Mockito.when(tex.getHeight()).thenReturn(32);

        //spawn Dean at (10, 10) and Player at (0, 10)
        Dean dean = new Dean(tex, 10, 10);
        Player player = new Player(tex, 0, 10);

        dean.calculateMovement(player);
        dean.doMove(1f); //move for 1s

        assertTrue(dean.getX() < 10, "Dean should move left towards player");
        assertEquals(10, dean.getY(), 0.1f, "Dean should stay on same Y axis");
    }
}

