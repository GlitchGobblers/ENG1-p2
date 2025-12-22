package io.github.glitchgobblers.tests;

import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.Timer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

public class TimerTest extends GdxTestBase {

    @Test
    public void testTimerDurationAndExpiry() throws Exception {
        Timer timer = new Timer(10);
        assertFalse(timer.isActive());
        assertEquals(10, timer.getRemainingTime());

        timer.play();
        assertTrue(timer.isActive());

        // simulate time passing
        Field endTimeField = Timer.class.getDeclaredField("endTime");
        endTimeField.setAccessible(true);
        int timeNow = (int) (System.currentTimeMillis() / 1000);
        endTimeField.setInt(timer, timeNow - 1); // Set end time to the past

        assertTrue(timer.hasElapsed(), "Timer should report elapsed if end time passed");
    }

    @Test
    public void testPauseAndResume() {
        Timer timer = new Timer(300);
        timer.play();
        assertTrue(timer.isActive());

        timer.pause();
        assertFalse(timer.isActive(), "Timer should be inactive after pause");

        int timeAtPause = timer.getRemainingTime();
        assertTrue(timeAtPause <= 300);

        // Resume timer
        timer.play();
        assertTrue(timer.isActive());
        assertTrue(
                timer.getRemainingTime() <= timeAtPause,
                "Time should continue from pause point, not reset"
        );
    }
}
