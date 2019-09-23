package com.example.concurrent.condition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class Main {
    private static Queue<Integer> queue = new LinkedList<>();
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    private static Producer producer = new Producer(queue, condition, lock);
    private static Consumer consumer = new Consumer(queue, condition, lock);

    public static void main(String[] args) {
        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer);
        t1.start();
        t2.start();
    }
}
