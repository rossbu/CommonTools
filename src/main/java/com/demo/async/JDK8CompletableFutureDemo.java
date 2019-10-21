package com.demo.async;

import com.pojo.User;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class JDK8CompletableFutureDemo extends FutureBase{

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        completableByAll();
        completableAllOf();
        supplyAsyncAll();
    }



    private static void supplyAsyncAll() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<Set> future2 = CompletableFuture.supplyAsync(() -> new HashSet());
        CompletableFuture<User> future3 = CompletableFuture.supplyAsync(() -> new User());
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);
        combinedFuture.join(); // return  3 results when all done, or throw exception if completed exceptionally
        System.out.println(future1.isDone());
        System.out.println(future2.isDone());
        System.out.println(future3.isDone());
        String s = future1.get();
        Set set = future2.get();
        User user = future3.get();
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
//    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        System.out.println("Looking up " + user);
        User results = restTemplate.getForObject("https://api.github.com/users/" + user, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }
}
