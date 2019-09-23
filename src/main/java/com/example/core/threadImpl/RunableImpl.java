package com.example.core.threadImpl;

import com.example.resource.Counter;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class RunableImpl {
    private static Counter counter = new Counter();

    private static AtomicInteger result = new AtomicInteger();
    private static AtomicInteger valBefore = new AtomicInteger();


            //You can use it instead of Atomic (this case not safety)
    //private static volatile int result, valBefore;

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Runnable task = () -> {
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
            };

            Thread thread = new Thread(task);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Final result = " + result);
    }

    private static void doWork() throws InterruptedException {
        valBefore = result;

        for (int i = 0; i < 10; i++) {
            result.addAndGet(counter.count(i));

//            result += counter.count(i);
        }
    }
}
