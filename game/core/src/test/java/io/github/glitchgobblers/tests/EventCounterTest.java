package io.github.glitchgobblers.tests;

import io.github.glitchgobblers.EventCounter;
import org.junit.jupiter.api.Test;
import io.github.glitchgobblers.GdxTestBase;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventCounterTest extends GdxTestBase {
  @Test
  public void eventCounterTest()  throws Exception
  {
    //EventCounter counter = new EventCounter();
    // first get the private events from the event counter using the  reflect.field package
    // and set varies values to the events
    Field hidden = EventCounter.class.getDeclaredField("hidden");
    hidden.setAccessible(true);
    Field positive = EventCounter.class.getDeclaredField("positive");
    positive.setAccessible(true);
    Field negative = EventCounter.class.getDeclaredField("negative");
    negative.setAccessible(true);
    hidden.setInt(null, 1);
    positive.setInt(null, 2);
    negative.setInt(null, 3);
  }

  @Test
  public void testIncrement() throws Exception{
    eventCounterTest();

    assertEquals(1, EventCounter.getHiddenCount());
    assertEquals(2, EventCounter.getPositiveCount());
    assertEquals(3, EventCounter.getNegativeCount());

    EventCounter.incrementHidden();
    EventCounter.incrementPositive();
    EventCounter.incrementPositive();
    EventCounter.incrementNegative();

    assertEquals(2, EventCounter.getHiddenCount());
    assertEquals(4, EventCounter.getPositiveCount());
    assertEquals(4, EventCounter.getNegativeCount());

  }

}
