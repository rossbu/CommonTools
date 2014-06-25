package com.tools.jdk.basic;

/**
 * Created by tbu on 6/25/2014.
 */

import java.util.Queue;
import java.util.LinkedList;

/**
 * Queue使用时要尽量避免Collection的add()和remove()方法，而是要使用offer()来加入元素，使用poll()来获取并移出元素
 * add()和remove()方法在失败的时候会抛出异常
 * LinkedList类实现了Queue接口，因此我们可以把LinkedList当成Queue来用
 * FIFO
 *
 * @author tb088e
 */
public class TestQueue {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<String>();
        queue.offer("Hello");
        queue.offer("World ");
        queue.offer(" ross!");
        System.out.println("before poll queue size is :" + queue.size());
        String str;
        while ((str = queue.poll()) != null) {
            System.out.print(str);
        }
        System.out.println();
        System.out.println("after poll queue size is :" + queue.size());
    }
}

