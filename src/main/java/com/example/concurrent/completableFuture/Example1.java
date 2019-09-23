package com.example.concurrent.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class Example1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        new Example1().testThenCombineAsync();
        long end = System.currentTimeMillis() - start;

        System.out.printf("%d sec passed", TimeUnit.MILLISECONDS.toSeconds(end));

    }

    private int slowInit() {
        int result = 1;
        System.out.println("start task slowInit: " + result);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    private int slowIncrement(int i) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finished increment with result " + ++i);

        return i++;
    }

    private void promiseTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(this::slowInit);

        int res = future.get();

        System.out.println("promiseTest is finished: " + res);
    }

    private void promiseTestNext() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future =
                CompletableFuture.supplyAsync(this::slowInit)
                .thenAccept(
                        (res)-> System.out.println("finished " + res)
                ).thenRun(() -> System.out.println("look at result"));

        future.get();

        System.out.println("promiseTextNext is finished");
    }

    private void promiseTestInc() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        CompletableFuture<Void> future =
                CompletableFuture.supplyAsync(this::slowInit) //1
                .thenApply(this::slowIncrement) //2
                .thenApply(this::slowIncrement)
                .thenAccept(res -> System.out.println("async result: " + res));

        future.get();

        long end = System.currentTimeMillis() - start;

        System.out.printf("%d sec passed", TimeUnit.MILLISECONDS.toSeconds(end));
    }

    private void promiseTestCompose2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(this::slowInit)
                .thenApply(this::slowIncrement);

        CompletableFuture<Integer> thenCompose =
                future1.thenCompose(res -> CompletableFuture.supplyAsync(() -> res))
                .thenApply(this::slowIncrement);

        System.out.println(thenCompose.get());
    }

    private void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture
                .supplyAsync(this::slowInit)
                .thenApply(this::slowIncrement); //2

        CompletableFuture<Integer> future2 = CompletableFuture.
                supplyAsync(this::slowInit).
                thenApply(this::slowIncrement); //2

        CompletableFuture<?> future3 = future1.thenCombine(future2, (x,y) -> x + y); //4

        System.out.println(future3.get());
    }

    private void testThenCombineAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> initial =
                CompletableFuture.supplyAsync(this::slowInit);

        CompletableFuture<Integer> future1 =
                initial.thenApplyAsync(this::slowIncrement);

        CompletableFuture<Integer> future2 =
                initial.thenApplyAsync(this::slowIncrement);

        CompletableFuture<Integer> future3 =
                future1.thenCombine(future2, (x, y) -> x + y);

        System.out.println(future3.get());


    }
}
