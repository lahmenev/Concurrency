package com.example.concurrent.synchronizers;

import java.util.concurrent.*;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class CycleBarrierExample {
    private final static CyclicBarrier barrier = new CyclicBarrier(4, () -> {
        System.out.println("all threads go through barrier");
    });

    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 2; i++) {
            TimeUnit.SECONDS.sleep(1);

            service.submit(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " waiting on barrier");
                    System.out.println(barrier.getNumberWaiting());
                    barrier.await(10, TimeUnit.SECONDS);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    System.out.println("finish program");
                } catch (TimeoutException e) {
                    barrier.reset();
                }
            });
        }

        service.shutdown();
    }
}
