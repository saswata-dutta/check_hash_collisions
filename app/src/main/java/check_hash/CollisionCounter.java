package check_hash;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CollisionCounter {
    private final ConcurrentHashMap<String, Integer> counts;

    public CollisionCounter() {
        this.counts = new ConcurrentHashMap<>();
    }

    public void accumulate(final HashMap<String, Integer> other) {
        for (String key : other.keySet()) {
            Integer value = other.get(key);

            counts.merge(key, value, Integer::sum);
        }
    }

    public void report() {
        for (Integer v : counts.values()) {
            if (v > 1) {
                System.out.println(v);
            }
        }
    }
}
