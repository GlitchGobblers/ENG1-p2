package io.github.yetti_eng.tests;

import com.badlogic.gdx.math.Vector2;
import io.github.yetti_eng.GdxTestBase;
import io.github.yetti_eng.InputHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EntityMovementTest extends GdxTestBase {

    @Test
    public void testEntityMovementNormalization() {
        // Simulate a diagonal input (holding UP and RIGHT)
        // Without normalisation, speed would be sqrt(1^2 + 1^2) = 1.41
        Vector2 rawInput = new Vector2(1, 1);

        // Run the normalisation logic used by Entities
        Vector2 normalized = InputHelper.makeUnitVector(rawInput);

        // Assert the length is approximately 1.0
        float length = normalized.len();
        assertEquals(1.0f, length, 0.001f, "Vector length should be normalized to 1");

        // Assert direction is maintained (both x and y should be positive and equal)
        assertTrue(normalized.x > 0 && normalized.y > 0, "Direction should be preserved");
        assertEquals(normalized.x, normalized.y, 0.001f, "X and Y components should be equal for diagonal");
    }
}