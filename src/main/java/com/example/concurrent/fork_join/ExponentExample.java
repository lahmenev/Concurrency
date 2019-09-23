package com.example.concurrent.fork_join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class ExponentExample {

    public static void main(String[] args) throws InterruptedException {
        final int componentValue = 10_000;

        Long beginT = System.currentTimeMillis();

        Stream task = new Stream(0, componentValue);
        ForkJoinPool pool = new ForkJoinPool(5);
        //TimeUnit.SECONDS.sleep(3);

        pool.invoke(task);

//        for (int i = 0; i < componentValue; i++) {
//            Stream.calculate(componentValue);
//        }

        Long endT = System.currentTimeMillis();


        Long finishTime = endT - beginT;
        System.out.println("=== time === " + finishTime / 1000 + " seconds");

    }

    static class Stream extends RecursiveAction {
        final int countLimit = 5000;
        private int start;
        private int end;

        public Stream(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= countLimit) {
                System.out.println("=run=");

                for (int i = start; i < end; i++) {
                    calculate(i);
                }
            } else {
                int middle = (start + end) / 2;
                Stream stream1 = new Stream(0, middle);
                stream1.fork();
                Stream stream2 = new Stream(middle + 1, end);
                stream2.fork();
                stream1.join();
                stream2.join();
                //invokeAll(stream1, stream2);
            }
        }

        private static void calculate(int numberForCalc) {
            for (int i = 0; i < numberForCalc; i++) {
                double pow = Math.pow(numberForCalc, 100);
            }
        }
    }
}
