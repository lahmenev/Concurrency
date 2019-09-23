package com.example.concurrent.synchronizers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class SemaphoreExample {
    private static Semaphore semaphore = new Semaphore(4,true);

    private static Runnable task = () -> {
        System.out.println(Thread.currentThread().getName() + ": acquiring lock...");
        System.out.println(Thread.currentThread().getName() + ": available Semaphore permits now: " +
                semaphore.availablePermits());

        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " got the permit");

            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " is performing operation " + i +
                        ", available Semaphore permits: " + semaphore.availablePermits());
                TimeUnit.SECONDS.sleep(2);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " releasing lock...");
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " available Semaphore permits now: " +
                    semaphore.availablePermits());
        }
    };

    public static void main(String[] args) {
        //workThread();
        workPool();
    }

    /**
     * Does 6 task using 6 Thread polls
     */
    private static void workPool() {
        ExecutorService service = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 6; i++) {
            service.submit(task);
        }
        service.shutdown();
    }

    /**
     * Does task within 6 thread
     */
    private static void workThread() {
        System.out.println("Total available Semaphore permits: " +
                semaphore.availablePermits());

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);
        Thread t4 = new Thread(task);
        Thread t5 = new Thread(task);
        Thread t6 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
    }
}
