package check_hash;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;

import java.util.HashMap;

public class PartialCollisionCounter implements Runnable {
    private final String[] buffer;
    private final HashFunction hf;
    private final HashMap<Long, MutableInt> counts;
    private final CollisionCounter collisionCounter;

    public PartialCollisionCounter(String[] buffer,
                                   HashFunction hf,
                                   CollisionCounter collisionCounter) {
        this.buffer = buffer;
        this.hf = hf;
        this.counts = new HashMap<>();
        this.collisionCounter = collisionCounter;
    }

    @Override
    public void run() {
        for (String s : buffer) {
            long hc = hf.newHasher()
                    .putString(s, Charsets.UTF_8)
                    .hash().asLong();

            MutableInt count = counts.get(hc);
            if (count == null) {
                counts.put(hc, new MutableInt());
            } else {
                count.increment();
            }
        }

        collisionCounter.accumulate(counts);
    }
}
