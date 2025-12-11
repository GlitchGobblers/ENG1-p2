package io.github.glitchgobblers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

public class Leaderboard {

  private static final String FILE_NAME = "leaderboard.json";

  private final Json json;

  public Leaderboard() {
    json = new Json();
    json.setTypeName(null);
    json.setIgnoreUnknownFields(true);
  }

  public static class Entry implements Comparable<Entry> {
    public String id;     // UUID
    public String name;   // display name
    public int score;     // best score
    public long ts;       // timestamp

    public Entry() {}

    public Entry(String id, String name, int score, long ts) {
      this.id = id;
      this.name = (name == null || name.isEmpty()) ? "Anonymous" : name.trim();
      this.score = score;
      this.ts = ts;
    }

    @Override
    public int compareTo(Entry o) {
      int s = Integer.compare(o.score, this.score);
      if (s != 0) return s;
      return Long.compare(o.ts, this.ts);
    }
  }

  public Array<Entry> load() {
    FileHandle fh = Gdx.files.local(FILE_NAME);
    if (!fh.exists()) return new Array<>();

    String raw = fh.readString("UTF-8");
    Array<Entry> entries;

    try {
      entries = json.fromJson(Array.class, Entry.class, raw);
    } catch (Exception ex) {
      Gdx.app.error("Leaderboard", "Parse failed. NOT overwriting file.", ex);
      return new Array<>(); // do not save() on failure
    }

    if (entries == null) entries = new Array<>();

    entries = mergeById(entries);
    entries.sort();

    return entries;
  }

  private Array<Entry> mergeById(Array<Entry> entries) {
    ObjectMap<String, Entry> map = new ObjectMap<>();

    for (Entry e : entries) {
      if (e == null || e.id == null || e.id.isEmpty()) continue;

      Entry existing = map.get(e.id);
      if (existing == null) {
        map.put(e.id, e);
      } else {
        if (e.score > existing.score) {
          existing.score = e.score;
          existing.ts = e.ts;
          existing.name = (e.name == null || e.name.isEmpty()) ? existing.name : e.name;
        } else if (e.score == existing.score && e.ts > existing.ts) {
          existing.ts = e.ts;
          if (e.name != null && !e.name.isEmpty()) existing.name = e.name;
        } else if (e.ts > existing.ts && e.name != null && !e.name.isEmpty()) {
          existing.name = e.name;
          existing.ts = e.ts;
        }
      }
    }

    return new Array<>(map.values().toArray());
  }

  private void save(Array<Entry> entries) {
    entries.sort();
    while (entries.size > 100) entries.pop();

    String jsonOut = json.prettyPrint(json.toJson(entries, Array.class, Entry.class));
    Gdx.files.local(FILE_NAME).writeString(jsonOut, false, "UTF-8");
  }

  public void submit(String id, String name, int score) {
    if (id == null || id.isEmpty()) return;

    Array<Entry> entries = load();

    Entry hit = null;
    for (Entry e : entries) {
      if (id.equals(e.id)) {
        hit = e;
        break;
      }
    }

    long now = TimeUtils.millis();

    if (hit == null) {
      entries.add(new Entry(id, name, score, now));
    } else {
      if (score > hit.score) {
        hit.score = score;
        hit.ts = now;
      }
      if (name != null && !name.isEmpty()) {
        hit.name = name;
      }
    }

    save(entries);
  }

  public Array<Entry> top(int n) {
    Array<Entry> entries = load();
    entries.sort();
    if (entries.size <= n) return entries;

    Array<Entry> out = new Array<>(n);
    for (int i = 0; i < n; i++) out.add(entries.get(i));
    return out;
  }
}
