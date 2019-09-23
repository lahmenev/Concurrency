package com.example.concurrent.fork_join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class SumNumber {

    public static void main(String[] args) {
        //workFork();

        long result = 0;
        long[] numbers = LongStream.rangeClosed(1, 1_000_000).toArray();
        for (int i = 0; i < numbers.length; i++) {
            result += numbers[i];
        }

        System.out.println(result);
    }

    private static void workFork() {
        long[] numbers = LongStream.rangeClosed(1, 1_000_000).toArray();
        ForkJoinTask<Long> task = new ForkJoinAdd(numbers);

        ForkJoinPool pool = new ForkJoinPool(4);
        System.out.println(pool.invoke(task));
    }

    static class ForkJoinAdd extends RecursiveTask<Long> {
        private final long[] numbers;
        private final int start;
        private final int end;
        private static final long TRESHOLD = 10_000;

        public ForkJoinAdd(long[] numbers) {
            this(numbers, 0, numbers.length);
        }

        public ForkJoinAdd(long[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int length = end - start;
            if (length <= TRESHOLD) {
                return add();
            }

            ForkJoinAdd firstTask = new ForkJoinAdd(numbers, start, start + length / 2);
            firstTask.fork();//start asynchronously

            ForkJoinAdd secondTask = new ForkJoinAdd(numbers, start + length / 2, end);

            Long secondTaskResult = secondTask.compute();
            Long firstTaskResult = firstTask.join();

            return firstTaskResult + secondTaskResult;

        }

        private long add() {
            long result = 0;
            for (int i = start; i < end; i++) {
                result += numbers[i];
            }
            return result;
        }
    }
}
