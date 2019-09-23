package com.example.resource;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class Counter {

    /**
     * Increases value and returns result
     *
     * @param a value that should to increase
     * @return result value
     */
    public int count(int a) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            a++;
            Thread.sleep(10);
        }

        return a;
    }
}
