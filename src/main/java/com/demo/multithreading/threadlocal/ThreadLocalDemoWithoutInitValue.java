package com.demo.multithreading.threadlocal;

public class ThreadLocalDemoWithoutInitValue {
    public static void main(String[] args)
    {
        ParentThread gfg_pt = new ParentThread();
        gfg_pt.start();
    }


}
class ParentThread extends Thread {
    public static ThreadLocal gfg_tl = new ThreadLocal();
    public void run()
    {
        // setting the new value
        gfg_tl.set("parent data");

        // returns the ThreadLocal value associated with current thread
        System.out.println("Parent  Thread Value :" + gfg_tl.get());

        ChildThread gfg_ct = new ChildThread();
        gfg_ct.start();
    }
}
class ChildThread extends Thread {
    public void run()
    {
        // returns  the ThreadLocal value associated with current thread
        System.out.println("Child Thread Value :" + ParentThread.gfg_tl.get());
        /* null (parent thread variable
        thread local value is not available to child thread ) */
    }
}

