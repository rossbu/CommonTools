package com.demo.multithreading.threadlocal;

public class ThreadLocalDemo {
    public static void main(String[] args)
    {
        ParentThread1 gfg_pt = new ParentThread1();
        gfg_pt.start();
    }


}
class ParentThread1 extends Thread {
    // anonymous inner class  for overriding childValue method.
    public static InheritableThreadLocal gfg_tl = new InheritableThreadLocal() {
        public Object childValue(Object parentValue)
        {
            return "child data";
        }
    };

    public void run()
    {
        // setting the new value
        gfg_tl.set("parent data");

        // returns the ThreadLocal value associated with current thread
        System.out.println("Parent  Thread Value :" + gfg_tl.get());

        ChildThread1 gfg_ct = new ChildThread1();
        gfg_ct.start();
    }
}
class ChildThread1 extends Thread {
    public void run()
    {
        // returns  the ThreadLocal value associated with current thread
        System.out.println("Child Thread Value :" + ParentThread1.gfg_tl.get());
        /* null (parent thread variable
        thread local value is not available to child thread ) */
    }
}

