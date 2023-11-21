import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutionOrder {

    public static void main(String[] args) {
        // Create a thread pool with two threads
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        // Submit the first task
        threadPool.submit(() -> {
            try {
                Thread.sleep(1000); // Simulate some work
                System.out.println("Thread 1 executed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Submit the second task
        threadPool.submit(() -> {
            try {
                Thread.sleep(900); // Simulate some work
                System.out.println("Thread 2 executed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Shutdown the thread pool to stop other threads coming.
        threadPool.shutdown();

        try {
            // Wait for both tasks to complete
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Both threads have completed.");
    }
}