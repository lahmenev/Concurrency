package com.example.concurrent.excecutors;

import com.example.resource.Counter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class FixedThreadPoolImpl {

    private static Counter counter = new Counter();
    private static AtomicInteger result = new AtomicInteger();
    private static AtomicInteger valBefore = new AtomicInteger();

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        Future<?> future = null;

        for (int i = 0; i < 10; i++) {
            future = threadPool.submit(() -> {
                try {
                    doWork();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(format(Thread.currentThread().getName() +
                        ": valBefore = %d, result = %d", valBefore.get(), result.get()));
            });
        }

        threadPool.shutdown();

        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final result = " + result);

        System.out.println(future.isDone());
    }

    /**
     * Gets result from increment method
     */
    private static void doWork() throws InterruptedException {
        valBefore = result;

        for (int i = 0; i < 10; i++) {
            result.addAndGet(counter.count(i));
        }
    }
}
