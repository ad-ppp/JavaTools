package com.jacky.demo.test.thread;

import com.jacky.tool.util.Util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Jacky on 2020/6/15
 * 测试 Executors 中断线程，线程的行为
 */
public class ExecutorInterruptTest {
    private Executor executor = Executors.newFixedThreadPool(5);

    void start() {
        final Thread[] threads = new Thread[1];

        for (int i = 0; i < 100; i++) {
            final int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    threads[0] = Thread.currentThread();

                    Util.sleepSafely(500);
                    Util.i("running %d", finalI);
                }
            });
        }

        Util.startThread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i <= 2) {
                    Util.sleepSafely(2000);
                    threads[0].interrupt();
                    i++;
                }
            }
        });
    }

    public static void main(String[] args) {
        new ExecutorInterruptTest().start();
    }
}
