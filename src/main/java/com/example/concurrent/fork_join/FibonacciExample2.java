package com.example.concurrent.fork_join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class FibonacciExample2 {

    public static void main(String[] args) {
        FibonacciTask task = new FibonacciTask(50);
        ForkJoinPool pool = new ForkJoinPool(4);

        long start = System.currentTimeMillis();

        System.out.println(pool.invoke(task));

        System.out.println(System.currentTimeMillis() - start);
    }

    static class FibonacciTask extends RecursiveTask<Integer> {

        private final int n;

        public FibonacciTask(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {

            if (n <= 1) {
                return n;
            }

            FibonacciTask f1 = new FibonacciTask(n - 1);
            f1.fork();
            FibonacciTask f2 = new FibonacciTask(n - 2);
            f2.fork();

            return f2.join() + f1.join();
        }
    }


}
