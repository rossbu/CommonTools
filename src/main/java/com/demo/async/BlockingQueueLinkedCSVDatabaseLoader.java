import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueLinkedCSVDatabaseLoader {
    public static void main(String[] args) {
        BlockingQueue<String> csvDataQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Record> recordsQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Stage 1: Scan CSV
        executorService.execute(() -> {
            try {
                // Simulate reading and scanning the CSV file
                for (String line : readAndScanCSV()) {
                    // Produce records and put them in the queue
                    recordsQueue.put(parseCSVLine(line));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Stage 2: Insert Records
        executorService.execute(() -> {
            try {
                while (true) {
                    // Consume records from the queue
                    Record record = recordsQueue.take();
                    // Simulate inserting the record into the database
                    insertIntoDatabase(record);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Shutdown the executor after completing the tasks
        executorService.shutdown();
    }

    private static Iterable<String> readAndScanCSV() {
        // Simulate reading and scanning the CSV file
        // Return an iterable collection of CSV lines
        return List.of(
                "John,Doe,30",
                "Jane,Smith,25",
                "Bob,Johnson,40"
        );
    }

    private static Record parseCSVLine(String line) {
        // Simulate parsing a CSV line into a Record object
        String[] parts = line.split(",");
        return new Record(parts[0], parts[1], Integer.parseInt(parts[2]));
    }

    private static void insertIntoDatabase(Record record) {
        // Simulate inserting a record into the database
        System.out.println("Inserting record: " + record);
    }

    static class Record {
        private String firstName;
        private String lastName;
        private int age;

        public Record(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Record{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
