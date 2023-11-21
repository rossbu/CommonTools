package com.demo.async;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *  When several threads produce objects for the queue faster than consumers can consume and process them - a queue can overgrow in size.
 *  SynchronousQueue helps to control communication without any specific code in producers.
 *  In real life it's similar to a meeting where one person answers questions asked by others.
 *  Consider SynchronousQueue as a kind of secretary
 *  https://stackoverflow.com/questions/8591610/when-should-i-use-synchronousqueue-over-linkedblockingqueue
 */
public class BlockingQueueTwoStagePipeline {
    public static void main(String[] args) {
        /*
        In this example, we use a SynchronousQueue to connect the two stages of the pipeline
         // aka as a handoff or coordination queue
         */
        BlockingQueue<Object[][]> recordQueue = new SynchronousQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Stage 1: File Reader and Scanner
        executorService.execute(() -> {
            try {
                // Simulate reading and scanning the file
                Object[][] records = readAndScanFile();
                // Produce a block of records
                recordQueue.put(records);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Stage 2: Database Inserter
        executorService.execute(() -> {
            try {
                // Consume the block of records from the queue
                Object[][] records = recordQueue.take();
                // Simulate inserting records into the database
                insertIntoDatabase(records);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Shutdown the executor after completing the tasks
        executorService.shutdown();
    }

    private static Object[][] readAndScanFile() {
        // Simulate reading and scanning the file
        // Return a block of records (e.g., a two-dimensional array)
        return new Object[10][];
    }

    private static void insertIntoDatabase(Object[][] records) {
        // Simulate inserting records into the database
    }
}
