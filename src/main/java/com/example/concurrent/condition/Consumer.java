package com.example.concurrent.condition;

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
public class Consumer implements Runnable {
    private Queue<Integer> queue;
    private Condition condition;
    private Lock lock;

    public Consumer(Queue<Integer> queue, Condition condition, Lock lock) {
        this.queue = queue;
        this.condition = condition;
        this.lock = lock;
    }

    @Override
    public void run() {

        for (int i = 0; i < 30; i++) {
            try {
                lock.lock();

                while (queue.size() == 0) {
                    condition.await();
                }

                System.out.println("get resource: "+ queue.poll() + ", queue's size: " + queue.size());
                TimeUnit.MILLISECONDS.sleep(200);
                condition.signal();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
