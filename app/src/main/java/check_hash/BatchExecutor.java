package check_hash;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BatchExecutor implements AutoCloseable {
    private final int BATCH_SIZE;
    private String[] batch;

    private final ExecutorService executorService;
    private int count = 0;

    private final PartialCollisionCounterFactory partialCollisionCounterFactory;

    public BatchExecutor(final int batch_size, final int pool_size,
                         final PartialCollisionCounterFactory partialCollisionCounterFactory) {

        this.partialCollisionCounterFactory = partialCollisionCounterFactory;

        BATCH_SIZE = batch_size;
        batch = new String[batch_size];

        // fixed thread pool
        executorService =
                new ThreadPoolExecutor(
                        pool_size,
                        pool_size,
                        0L,
                        TimeUnit.MILLISECONDS,
                        new ExecutorBlockingQueue<>(pool_size));
    }

    public void submit(String task) {
        if (task == null || task.length() < 1) return;
        batch[count] = task;
        ++count;
        if (count >= BATCH_SIZE) execute();
    }

    private void execute() {
        if (count < 1) return;

        executorService.submit(partialCollisionCounterFactory.create(batch));
        batch = new String[BATCH_SIZE];
        count = 0;
    }

    @Override
    public void close() throws Exception {
        execute();
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
            System.err.println("Waited for 10 minutes, forced exit");
            System.exit(0);
        }
    }
}
