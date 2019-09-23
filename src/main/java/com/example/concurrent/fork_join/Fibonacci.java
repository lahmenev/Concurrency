package com.example.concurrent.fork_join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class Fibonacci {

    public static void main(String[] args) {
        workFork();
//        long start = System.currentTimeMillis();
//
//        System.out.println(ForkJoinFibonacci.fib(50));
//
//        System.out.println(System.currentTimeMillis() - start);
    }

    private static void workFork() {
        ForkJoinFibonacci task = new ForkJoinFibonacci(50);
        ForkJoinPool pool = new ForkJoinPool(4);

        long start = System.currentTimeMillis();
        pool.invoke(task);
        System.out.println(System.currentTimeMillis() - start);

        System.out.println(task.number);
    }

    static class ForkJoinFibonacci extends RecursiveAction {
        private static final long TRESHOLD = 10;
        private long number;

        public ForkJoinFibonacci(long number) {
            this.number = number;
        }

        @Override
        protected void compute() {
            long n = number;

            if (n <= TRESHOLD) {
                number = fib(n);
            } else {
                ForkJoinFibonacci f1 = new ForkJoinFibonacci(n- 1);
                ForkJoinFibonacci f2 = new ForkJoinFibonacci(n - 2);
                invokeAll(f1, f2);
                number = f1.number + f2.number;
            }
        }

        private static long fib(long n) {

            if (n <= 1) {
                return n;
            } else {
                return fib(n - 1) + fib(n - 2);
            }
        }
    }
}
