package com.jacky.demo.test.thread;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/6/15
 */
public class InterruptTest {

    private void start() {
        final Thread[] threads = new Thread[3];
        Util.startThread(new Runnable() {
            @Override
            public void run() {
                threads[0] = Thread.currentThread();
                Util.sleepSafely(1000);
                Util.i("do 1");
            }
        });

        Util.startThread(new Runnable() {
            @Override
            public void run() {
                threads[1] = Thread.currentThread();
                Util.sleepSafely(2000);
                Util.i("do 2");
            }
        });

        Util.startThread(new Runnable() {
            @Override
            public void run() {
                threads[2] = Thread.currentThread();
                Util.sleepSafely(3000);
                Util.i("do 3");
            }
        });

        Util.sleepSafely(1500);
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public static void main(String[] args) {
        new InterruptTest().start();
    }
}
