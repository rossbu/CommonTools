package com.demo.async;

import com.pojo.User;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.demo.async.AsyncUtils.sleep;

/**
 * this demo includes jdk8 , 9 and upper version example.
   tasks : uncomment each of method below and do the coding, comparing your result with existing functions

 */
public class JDK8CompletableFutureDemo extends FutureBase{

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        JDK8CompletableFutureDemo demo = new JDK8CompletableFutureDemo();
        completableByAll();
//        completableAllOf();
//        jdk9delayExecutor();
//        futureAcceptTtest()
//        supplyAsyncTest();
//        thenAcceptTest();
//        demo.futureThenApplyThenApplyAsync();
    }

    /**
     If I just create a CompletableFuture with supplyAssync method or something like that, it is OK.
     It waits patiently for me to call get or join method to compute
     */
    private static void thenAcceptTest() {
        CompletableFuture<String> alreadyDoneTask1 = CompletableFuture.completedFuture("I am already Completed -1");
        CompletableFuture<String> alreadyDoneTask2 = CompletableFuture.completedFuture("I am already Completed -2");
        CompletableFuture<String> sleep5secondsTask3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "slept 5 seconds";
        });
        CompletableFuture<String> sleep10secondsTask4 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "slept 10 seconds";
        });
    alreadyDoneTask1.thenAccept(
        e -> System.out.println(e)); // Non Blocking,notify method will be called automaticall
        alreadyDoneTask2.thenAccept(e -> System.out.println(e));     //Non Blocking,notify method will be called automaticall
    sleep5secondsTask3.thenAccept(
        e -> System.out.println(e)); // Non Blocking,notify method will be called automaticall
        sleep10secondsTask4.thenAccept(e -> System.out.println(e)); //Non Blocking,notify method will be called automaticall

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     get() to join(),
         as you will imediately get a compiler error saying that neither InterruptedException nor ExecutionException are thrown in the try block

         Well get exists, because CompletableFuture implements the Future interface which mandates it.
         join() most likely has been introduced, to avoid needing to catch checked exceptions in lambda expressions when combining futures.
         In all other use cases, feel free to use whatever you prefer.

     supplyAsync
        When performing operations as provided by CompletableFuture with supplyAsync, thenAccept etc.
        they're all performed by a thread in the threadpool. That allows you to do "fire and forget" operations and continuing work in the main thread as you see fit.
     */
    private static void supplyAsyncTest() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future1:" + Thread.currentThread().getName());
            return "Hello";
        });
        CompletableFuture<Set> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future2:" + Thread.currentThread().getName());
            return new HashSet();}
            );
        CompletableFuture<User> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future3:"+Thread.currentThread().getName());
            return new User();
        });

        Thread.sleep(5000);
        System.out.println(future1.isDone());
        System.out.println(future2.isDone());
        System.out.println(future3.isDone());
        String s = future1.get();  // use get
        Set set = future2.join();  // use join without the need to hanlde thrown exceptions
        User user = future3.join();
        System.out.println(s);
        System.out.println(set.size());
        System.out.println(user.getName());
    }

    private static void completableAllOf() throws InterruptedException, ExecutionException {
        JDK8CompletableFutureDemo completableFutureDemo = new JDK8CompletableFutureDemo();
        // Kick of multiple, asynchronous lookups
        CompletableFuture<User> page1 = completableFutureDemo.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = completableFutureDemo.findUser("CloudFoundry");
        CompletableFuture<User> page3 = completableFutureDemo.findUser("Spring-Projects");
        // Wait until they are all done
        CompletableFuture.allOf(page1,page2,page3).join();

        // print out
        System.out.println("<==>"+ page1.get());
        System.out.println("<==>"+ page2.get());
        System.out.println("<==>"+ page3.get());
    }


    private static void completableByAll() throws InterruptedException, ExecutionException {
        JDK8CompletableFutureDemo completableFutureDemo = new JDK8CompletableFutureDemo();
        // Kick of multiple, asynchronous lookups
        CompletableFuture<User> page1 = completableFutureDemo.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = completableFutureDemo.findUser("CloudFoundry");
        CompletableFuture<User> page3 = completableFutureDemo.findUser("Spring-Projects");
        List<CompletableFuture<User>> listofUsers = new ArrayList<>();
        listofUsers.add(page1);
        listofUsers.add(page2);
        listofUsers.add(page3);
        CompletableFuture<List<User>> listOfCompletableFutures = all(listofUsers);
        List<User> users = listOfCompletableFutures.get();
        users.stream()
                .map(User::getName)
                .forEach(System.out::println);
    }

    /**
      Waits for *all* futures to complete and returns a list of results.
      If *any* future completes exceptionally then the resulting future will also complete exceptionally.
     */
    public static <T> CompletableFuture<List<T>> all(List<CompletableFuture<T>> completableFutures) {
        CompletableFuture[] cfs = completableFutures.toArray(new CompletableFuture[completableFutures.size()]);
        return CompletableFuture.allOf(cfs)
                .thenApply(ignored -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    /**
        2 ways
        @Async
     */
    @Async("asyncExecutor")
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        System.out.println("Looking up " + user);
        User results = restTemplate.getForObject("https://api.github.com/users/" + user, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }

    private static void jdk9delayExecutor() throws InterruptedException {
        // For example, I want to calculate the sum of two numbers and for some conditions, I want it to happen after 2 seconds, my code will look like this:
        int a = 2;
        int b = 5;
        Supplier<Integer> integerSupplier = () -> a + b;
        Executor executor = CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS);
        CompletableFuture.supplyAsync(integerSupplier, executor).thenAccept(result -> System.out.println(result));
        TimeUnit.SECONDS.sleep(10);
        // When running the above example, you will see after 2s, the results will appear:
    }
    // SETUP
    Runnable notifier = new Runnable() {
        @Override
        public void run() {
            System.out.print(".");
        }
    };
    Runnable notifier2 = new Runnable() {
        @Override
        public void run(){
            while(!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(1000); //exclude try/catch for brevity
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                System.out.print("^");
            }
        }
    };

    /**
     4

     I must point out that the names thenApply and thenApplyAsync is absolutely horrible and confusing.
     There is nothing in thenApplyAsync that is more asynchronous than thenApply from the contract of these methods.

     The difference has to do with on which thread the function is run. The function supplied to thenApply may run on any of the threads that

     call complete
     call thenApply on the same instance
     while thenApplyAsync either uses a default Executor (aka. thread pool), or a supplied Executor.

     The asynchronous part of these function has to do with the fact that an asynchronous operation eventually calls complete or completeExceptionally.
     The idea came from Javascript, which has nothing to do with multithreading.


     26 votes up
     The difference has to do with the Executor that is responsible for running the code. Each operator on CompletableFuture generally has 3 versions.

     thenApply(fn) - runs fn on a thread defined by the CompleteableFuture on which it is called, so you generally cannot know where this will be executed.It might immediately execute if the result is already available.
     thenApplyAsync(fn) - runs fn on a environment-defined executor regardless of circumstances. For CompletableFuture this will generally be ForkJoinPool.commonPool().
     thenApplyAsync(fn,exec) - runs fn on exec.
     In the end the result is the same, but the scheduling behavior depends on the choice of method.

     */
    public void futureThenApplyThenApplyAsync() {

        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(2);
        ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        newSingleThreadScheduledExecutor.scheduleAtFixedRate(notifier, 1, 1, TimeUnit.SECONDS);
        newFixedThreadPool.submit(notifier);
        CompletableFuture<Integer> completableFuture =
                CompletableFuture.supplyAsync(this::findAccountNumber,newFixedThreadPool)
                        .thenApplyAsync(this::calculateBalance,newSingleThreadScheduledExecutor)
                        .thenApplyAsync(this::notifyBalance);
        Integer balance = completableFuture.join();
        System.out.println("balance is : " + balance);
        newSingleThreadScheduledExecutor.shutdown();
        newFixedThreadPool.shutdown();
    }

    public int findAccountNumber() {
        System.out.println(Thread.currentThread() + " findAccountNumber");
        sleep(1);
        return 10;
    }

    public int calculateBalance(int accountNumber) {
        System.out.println(Thread.currentThread() + " calculateBalance");
        sleep(5);
        return accountNumber * accountNumber;
    }

    public Integer notifyBalance(Integer balance) {
        System.out.println(Thread.currentThread() + "Sending Notification");
        sleep(10);
        return balance;
    }

}
