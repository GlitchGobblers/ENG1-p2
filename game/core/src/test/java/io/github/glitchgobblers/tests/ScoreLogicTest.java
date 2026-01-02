package io.github.glitchgobblers.tests;

import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.Timer;
import io.github.glitchgobblers.YettiGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreLogicTest extends GdxTestBase {
    @Test
    public void testFinalScoreCalculation() {
        YettiGame game = new YettiGame();
        // simulate collecting 500 points via events
        game.score = 500;

        // mock a timer with 100secs remaining
        game.timer = new Timer(100);

        int result = game.calculateFinalScore();

        assertEquals(600, result, "Score should equal collected points + remaining seconds");
    }

}
