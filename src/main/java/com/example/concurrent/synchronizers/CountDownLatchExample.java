package com.example.concurrent.synchronizers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class CountDownLatchExample {
    private static CountDownLatch latch = new CountDownLatch(4);
    private static final int trackLength = 500000;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {

            new Thread(new Car(i, (int) (Math.random() * 100 + 50))).start();
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    private static class Car implements Runnable {
        private int carNumber;

        private int carSpeed;

        public Car(int carNumber, int carSpeed) {
            this.carNumber = carNumber;
            this.carSpeed = carSpeed;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль № %d подъехал к старту\n", carNumber);
                latch.countDown();
                latch.await();

                TimeUnit.MILLISECONDS.sleep(trackLength / carSpeed);
                System.out.printf("Автомобиль № %d финишировал\n", carNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
