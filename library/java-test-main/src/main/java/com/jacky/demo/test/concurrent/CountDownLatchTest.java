package com.jacky.demo.test.concurrent;

import com.jacky.tool.util.Util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jacky on 2020/6/11
 */
public class CountDownLatchTest {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private void start() {
        Util.startThread(new Runnable() {
            @Override
            public void run() {
                Util.sleepSafely(2000);
                countDownLatch.countDown();
            }
        });

        try {
            Util.i("begin[%d]", System.currentTimeMillis());
            final boolean await = countDownLatch.await(1000, TimeUnit.MILLISECONDS);
            Util.i("await,%b", await);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Util.i("end[%d]", System.currentTimeMillis());
    }

    public static void main(String[] args) {
        new CountDownLatchTest().start();
    }
}
