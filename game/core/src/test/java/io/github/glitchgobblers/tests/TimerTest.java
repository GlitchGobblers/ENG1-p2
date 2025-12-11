package io.github.glitchgobblers.tests;

import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.Timer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTest extends GdxTestBase {

  @Test
  public void DurationExpiryTest() throws Exception {
    Timer timer = new Timer(10);
    assertFalse(timer.isActive());
    assertFalse(timer.isFinished());
    assertEquals(10, timer.getRemainingTime());
// testing the timer when it's played
    timer.play();
    assertTrue(timer.isActive());
    assertFalse(timer.isFinished());

    // force the timer to finish so the hasElapsed becomes true, via using
    Field endTime = Timer.class.getDeclaredField("endTime");
    endTime.setAccessible(true);
    int timenow = (int) (System.currentTimeMillis() / 1000);
    endTime.setInt(timer, timenow - 1); // meaning already elapsed


    assertTrue(timer.hasElapsed(), "timer should report elapsed");
  }
  @Test
 public void testPauseAndResume(){
    Timer timer = new Timer(20);
    timer.pause();
    assertFalse(timer.isActive());
    assertFalse(timer.isFinished());
    int remainingAfterPause = timer.getRemainingTime();


    // resume playing again
    timer.play();
    assertTrue(timer.isActive());


    // while active, remaining time must be <= what it was when paused, which make
    // the countdown to continue rather than resetting
    int remainingAfterResume = timer.getRemainingTime();
    assertTrue(remainingAfterResume <= remainingAfterPause,
      "remaining time after resuming should be less than or equal to time at pause");


  }
}
