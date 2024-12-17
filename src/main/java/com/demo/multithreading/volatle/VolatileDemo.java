package com.demo.multithreading.volatle;

import java.util.concurrent.atomic.AtomicInteger;

/*
    * The volatile keyword in Java is used to indicate that a variable's value will be modified by different threads.
    * https://www.51cto.com/article/709935.html
    *
 */
public class VolatileDemo {

    private static boolean flag = false; // try  to remove volatile and see the difference
    static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 如果 flag 变量为 true 就终止执行
                long start = System.currentTimeMillis();
                while (!flag) {
//                    System.out.println("lopping .... index: " + atomicInteger.addAndGet(1));
                }
                System.out.println("终止执行... 耗时：" + (System.currentTimeMillis() - start) + "ms");
            }
        });
        t1.start();
//        Thread.sleep(1000);

        // print  thread’s cache
        System.out.println("t1.isAlive() = " + t1.isAlive());
        System.out.println("t1.getState() = " + t1.getState());
        System.out.println("t1.getPriority() = " + t1.getPriority());
        System.out.println("t1.getThreadGroup() = " + t1.getThreadGroup());
        System.out.println("t1.getUncaughtExceptionHandler() = " + t1.getUncaughtExceptionHandler());
        System.out.println("t1.getId() = " + t1.getId());
        System.out.println("t1.getName() = " + t1.getName());
        System.out.println("t1.getStackTrace() = " + t1.getStackTrace());

        // 1s 之后将 volatile flag 变量的值修改为 true, if not volatile, the flag will not be updated
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("设置 flag 变量的值为 true！");
                flag = true;
            }
        });
        t2.start();
    }
}
