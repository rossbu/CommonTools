import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionOrderWithCountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        // Create a CountDownLatch with a count of 2
        CountDownLatch latch = new CountDownLatch(2);

        // Create a thread pool with two threads
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // Task 1
        threadPool.submit(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task 1 executed.");
                // Count down the latch to signal completion of task 1
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Task 2
        threadPool.submit(() -> {
            try {
                Thread.sleep(999);
                System.out.println("Task 2 executed.");
                // Count down the latch to signal completion of task 2
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Wait for both tasks to complete
        latch.await();

        // Shutdown the thread pool
        threadPool.shutdown();

        System.out.println("Both tasks have completed.");
    }
}
