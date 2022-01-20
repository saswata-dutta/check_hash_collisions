package check_hash;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;

import java.util.Base64;
import java.util.HashMap;

public class PartialCollisionCounter implements Runnable {
    private final String[] buffer;
    private final int size;
    private final HashFunction hf;
    private final HashMap<String, Integer> counts;
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
            String hc = Base64.getEncoder().encodeToString(
                    hf.newHasher().putString(buffer[i], Charsets.UTF_8).hash().asBytes());

            counts.merge(hc, 1, Integer::sum);
        }

        collisionCounter.accumulate(counts);
    }
}
