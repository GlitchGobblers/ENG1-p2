package io.github.yetti_eng.tests;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.yetti_eng.GdxTestBase;
import io.github.yetti_eng.Timer;
import io.github.yetti_eng.YettiGame;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreLogicTest extends GdxTestBase {

    @Test
    public void testFinalScoreCalculation() {
        // 1. Mock the Game
        YettiGame game = new YettiGame();
        
        // Mock internal graphics stuff so the game doesn't crash if it tries to draw
        game.batch = Mockito.mock(SpriteBatch.class);
        game.viewport = Mockito.mock(FitViewport.class);
        game.font = Mockito.mock(BitmapFont.class);

        // 2. Set up a specific scenario
        // The player collected 500 points worth of items
        game.score = 500;

        // 3. Set up the Timer
        // The timer has 120 seconds left
        game.timer = new Timer(300); // 300s total duration
        // We manually inject the state to simulate 120s remaining
        // Since we can't easily "wait" 180 seconds in a unit test, 
        // we rely on the fact that calculateFinalScore uses timer.getRemainingTime().
        // For this test, we can mock the Timer OR just trust the Timer logic if we know it works.
        // Let's rely on the simpler calculation: Score + Remaining Time.
        
        // Actually, easiest way to test 'calculateFinalScore' without waiting 
        // is to subclass/mock Timer or simply Pause it at a specific remaining time.
        
        game.timer.play(); 
        // Force the timer logic: 
        // Since Timer uses System.currentTimeMillis, it's hard to control exactly.
        // A better test for YettiGame logic is simply checking the math:
        
        // Let's assume the timer just started (300 remaining)
        // Score (500) + Time (300) = 800
        int expected = 500 + 300; 
        
        // Note: In a real run, 0.001ms might pass, so we allow a tiny margin or just check logic.
        // However, YettiGame.calculateFinalScore() is: return score + timer.getRemainingTime();
        
        // Let's act as if we just finished the level instantly.
        int result = game.calculateFinalScore();
        
        // There's a chance 1 second ticked over during test execution, so we accept 799 or 800.
        assertTrue(result >= expected - 1 && result <= expected, 
            "Score should be Score + Remaining Time (approx 800)");
    }
    
    private void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}