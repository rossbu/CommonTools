package com.jdk.jdk8;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class SupplierDeferredExecution {
    public static void main(String[] args) throws InterruptedException {
        // Create a reference to the current date-time object when the following line is
        // executed
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt);// Line-1

        // Create a reference to a functionality that will get the current date-time
        // whenever this functionality will be used
        Supplier<LocalDateTime> dateSupplier = LocalDateTime::now;

        // Sleep for 5 seconds
        Thread.sleep(5000);

        System.out.println(ldt);// Will display the same value as Line-1
        System.out.println(dateSupplier.get());// Will display the current date-time when this line will be executed

        // Sleep again for 5 seconds
        Thread.sleep(5000);

        System.out.println(ldt);// Will display the same value as Line-1
        System.out.println(dateSupplier.get());// Will display the current date-time when this line will be executed
    }
}