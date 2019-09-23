package com.example.concurrent.excecutors;

import java.util.concurrent.*;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class CachedThreadPoolImpl {

    public static void main(String[] args) {
        ExecutorService cachedPool = Executors.newCachedThreadPool();

        Callable<String> callableTask = () -> {
            String message = "Callable is done!";

            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("Callable is doing something");
                TimeUnit.SECONDS.sleep(1);
            }

            return message;
        };

        Runnable runnableTask = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("Runable is doing");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Future<String> callableFuture = cachedPool.submit(callableTask);
        Future<?> runableFuture = cachedPool.submit(runnableTask);

        // check if tasks are done or not
        if(callableFuture.isDone()){
            System.out.println("\t\tCallable is done !");
        }else{
            System.out.println("\t\tCallable is not done !");
        }

        if(runableFuture.isDone()){
            System.out.println("\t\tRunnable is done !");
        }else{
            System.out.println("\t\tRunnable is not done !");
        }

        try {
            // get() waits for the task to finish and then gets the result
            String returnedValue = callableFuture.get();
            System.out.println(returnedValue);
        } catch (InterruptedException e) {
            // thrown if task was interrupted before completion
            e.printStackTrace();
        } catch (ExecutionException e) {
            // thrown if the task threw an execption while executing
            e.printStackTrace();
        }

        cachedPool.shutdown(); // shutdown the pool.
    }
}
