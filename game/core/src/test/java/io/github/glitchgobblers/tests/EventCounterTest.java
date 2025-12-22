package io.github.glitchgobblers.tests;

import io.github.glitchgobblers.EventCounter;
import io.github.glitchgobblers.GdxTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventCounterTest extends GdxTestBase {
    @Test
    public void testEventIncrementLogic() {
        // reset counters to 0 before test
        EventCounter.reset();
        assertEquals(0, EventCounter.getHiddenCount());

        //simulate triggering different event types
        EventCounter.incrementHidden();
        EventCounter.incrementPositive();
        EventCounter.incrementPositive();
        EventCounter.incrementNegative();

        assertEquals(1, EventCounter.getHiddenCount(), "Hidden count incorrect");
        assertEquals(2, EventCounter.getPositiveCount(), "Positive count incorrect");
        assertEquals(1, EventCounter.getNegativeCount(), "Negative count incorrect");
    }
}
