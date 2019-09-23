package com.example.concurrent.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * email : s.lakhmenev@andersenlab.com
 *
 * @author Lakhmenev Sergey
 * @version 1.1
 */
public class ThreadSafeArrayList<E> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();
    private Lock writeLick = readWriteLock.writeLock();

    private List<E> list = new ArrayList<>();

    public void add(E o) {
        writeLick.lock();

        try {
            list.add(o);
            System.out.println("Adding element " + o + " by thread"+ Thread.currentThread().getName());
        } finally {
            writeLick.unlock();
        }
    }

    public E get(int i) {
        readLock.lock();

        try {
            System.out.println("Getting element by thread " + Thread.currentThread().getName());
            return list.get(i);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String toString() {
        return "list = " + list;
    }
}
