package com.example.concurrent.condition;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class Producer implements Runnable {
    private Queue<Integer> queue;
    private Condition condition;
    private Lock lock;

    public Producer(Queue<Integer> queue, Condition condition, Lock lock) {
        this.queue = queue;
        this.condition = condition;
        this.lock = lock;
    }

    @Override
    public void run() {

        for (int i = 0; i < 30; i++) {
            lock.lock();

            try {
                while (queue.size() >= 10) {
                    condition.await();
                }

                queue.add(i);
                TimeUnit.MILLISECONDS.sleep(200);
                System.out.println("add resource " + i + ", queue's size: " + queue.size());
                condition.signal();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
//        int i = 0;
//
//        while (true) {
//
//        }
    }
}
