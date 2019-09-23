package com.example.concurrent.lock;

import com.example.resource.Counter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class ReentrantLockExample {
    private Lock lock = new ReentrantLock();

    //          Only first thread will be work
    //private Lock lock = new ReentrantLock(false);
    private int count = 0;
    private Counter counter = new Counter();

    private void increaseCount() throws InterruptedException {
        boolean lockAcquired = lock.tryLock(1, TimeUnit.SECONDS);

        //lock.lock();

        if (lockAcquired) {
            try {

                System.out.println(Thread.currentThread().getName() + " gets Count: " + count);
                for (int i = 0; i < 10; i++) {
                    count += counter.count(i);
                }

            }  finally {
                lock.unlock();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " could not get lock");
        }
    }

    public static void main(String[] args) {
        ReentrantLockExample reentrantLockExample = new ReentrantLockExample();
        Thread t1 = new Thread(() -> {

            while (reentrantLockExample.count <= 1_000) {
                try {
                    reentrantLockExample.increaseCount();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {

            while (reentrantLockExample.count <= 1_000) {
                try {
                    reentrantLockExample.increaseCount();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }
}
