package check_hash;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;

import java.util.HashMap;

public class PartialCollisionCounter implements Runnable {
    private final String[] buffer;
    private final int size;
    private final HashFunction hf;
    private final HashMap<Long, Integer> counts;
    private final CollisionCounter collisionCounter;

    public PartialCollisionCounter(String[] buffer,
                                   int size,
                                   HashFunction hf,
                                   CollisionCounter collisionCounter) {
        this.buffer = buffer;
        this.size = size;
        this.hf = hf;
        this.counts = new HashMap<>();
        this.collisionCounter = collisionCounter;
    }

    @Override
    public void run() {
        for (int i = 0; i < size; ++i) {
            long hc = hf.newHasher()
                    .putString(buffer[i], Charsets.UTF_8)
                    .hash().asLong();

            counts.merge(hc, 1, Integer::sum);
        }

        collisionCounter.accumulate(counts);
    }
}
