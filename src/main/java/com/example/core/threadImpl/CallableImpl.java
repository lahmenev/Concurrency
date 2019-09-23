package com.example.core.threadImpl;

import com.example.resource.Counter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class CallableImpl {

    private static Counter counter = new Counter();

    private static AtomicInteger result = new AtomicInteger();
    private static AtomicInteger valBefore = new AtomicInteger();


            //You can use it instead of Atomic (this case not safety)
    //private static volatile int result, valBefore;

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Callable<Integer> task = () -> {
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

                return result.get();
            };

            FutureTask<Integer> future = new FutureTask<>(task);
            Thread thread = new Thread(future);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(format(thread.getName() +
                        ": valBefore = %d, result = %d", valBefore.get(), future.get()));
            } catch (InterruptedException | ExecutionException e) {
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
