package com.example.concurrent.lock;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class ReadWriteLockExample {

    public static void main(String[] args) {
        ThreadSafeArrayList<Integer> list = new ThreadSafeArrayList<>();

        Thread thread1 = new Thread(() -> {
            list.add(1);
            list.add(2);
            list.add(3);
            System.out.println(list.get(2));
        });

        Thread thread2 = new Thread(() -> {
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add(4);
            list.add(5);
            System.out.println(list.get(2));
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(list);
    }
}
