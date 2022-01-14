package check_hash;

import java.util.HashMap;

public class CollisionCounter {
    private final HashMap<Long, MutableInt> counts;

    public CollisionCounter() {
        this.counts = new HashMap<>();
    }

    synchronized public void accumulate(final HashMap<Long, MutableInt> other) {
        for (Long key : other.keySet()) {
            MutableInt value = other.get(key);

            MutableInt count = counts.get(key);
            if (count == null) {
                counts.put(key, value);
            } else {
                count.increment(value.value);
            }
        }
    }

    public void report() {
        for (MutableInt v : counts.values()) {
            if (v.value > 1) {
                System.out.println(v.value);
            }
        }
    }
}
