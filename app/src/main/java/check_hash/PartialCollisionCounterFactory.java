package check_hash;

import com.google.common.hash.HashFunction;

public class PartialCollisionCounterFactory {
    private final HashFunction hf;
    private final CollisionCounter collisionCounter;

    public PartialCollisionCounterFactory(HashFunction hf, CollisionCounter collisionCounter) {
        this.hf = hf;
        this.collisionCounter = collisionCounter;
    }

    public PartialCollisionCounter create(final String[] buffer) {
        return new PartialCollisionCounter(buffer, hf, collisionCounter);
    }
}
