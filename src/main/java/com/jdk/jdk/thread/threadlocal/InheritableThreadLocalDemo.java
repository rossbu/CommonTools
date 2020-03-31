package com.jdk.jdk.thread.threadlocal;

/**
 *
 *
 * output : Thread-0:null/1002
 */
public class InheritableThreadLocalDemo {
    // 使用ThreadLocal不能继承父线程的ThreadLocal的内容
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    private static InheritableThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        threadLocal.set(1001); // father
        inheritableThreadLocal.set(1002); // father

        // create a new child thread in current thread now parent->child relation is created.
        new Thread(() -> System.out.println(Thread.currentThread().getName() + ":"
                + threadLocal.get() + "/"
                + inheritableThreadLocal.get()))
                .start();

    }
}
