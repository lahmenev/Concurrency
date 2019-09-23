package com.example.concurrent.fork_join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class MaxOfArray {

    public static void main(String[] args) {
        int[] array = {3, 4, 45, 46, 47, 34, 44,
                       67, 22, 23, 1, 8, 9, 11};

        System.out.println(findMax(array, 1));
    }

    public static int findMax(int[] array, int nThreads) {
        Solver solver = new Solver(array, 0, array.length);

        ForkJoinPool pool = new ForkJoinPool(nThreads);
        pool.invoke(solver);
        return  solver.result;
    }

    static class Solver extends RecursiveAction {
        private int start;
        private int end;
        private int result;
        private int[] array;

        public Solver(int[] array , int start, int end ) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {

            if (end - start == 1) {
                result = array[start];
            } else {
                int mid = (start + end) / 2;
                Solver solver1 = new Solver(array, start, mid);
                Solver solver2 = new Solver(array, mid, end);
                invokeAll(solver1, solver2);
                result = Math.max(solver1.result, solver2.result);
            }
        }
    }
}
