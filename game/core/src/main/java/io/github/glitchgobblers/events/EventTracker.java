package io.github.glitchgobblers.events;

import io.github.glitchgobblers.EventCounter;
import io.github.glitchgobblers.YettiGame;
import java.util.HashSet;
import java.util.Set;

/**
 * Tracks which map events have been triggered in the current run and
 * drives the event-related achievements and event counters.
 */
public class EventTracker {
  private final Set<String> registeredEventIds = new HashSet<>();
  private final Set<String> triggeredEventIds = new HashSet<>();

  /** Register an event so progress can be tracked without hardcoding ids. */
  public void register(Event event) {
    if (event == null || event.getEventId() == null) {
      return;
    }
    registeredEventIds.add(event.getEventId());
  }

  /**
   * Marks an event as triggered, updates counters and unlocks achievements.
   * Subsequent triggers of the same event are ignored.
   */
  public void onEventTriggered(Event event, YettiGame game) {
    if (event == null || game == null || event.getEventId() == null) {
      return;
    }

    boolean firstTime = triggeredEventIds.add(event.getEventId());
    if (!firstTime) {
      return;
    }

    if (event.isHiddenEvent()) {
      EventCounter.incrementHidden();
    } else if (event.isPositiveEvent()) {
      EventCounter.incrementPositive();
    } else if (event.isNegativeEvent()) {
      EventCounter.incrementNegative();
    }

    game.achievements.markEventTriggered(game);
    if (event.isWinEvent() && allEventsTriggered()) {
      game.achievements.markAllEventsCompleted(game);
    }
  }

  public boolean allEventsTriggered() {
    return !registeredEventIds.isEmpty() && triggeredEventIds.containsAll(registeredEventIds);
  }

  public void clear() {
    registeredEventIds.clear();
    triggeredEventIds.clear();
  }
}

