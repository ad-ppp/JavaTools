package com.jacky.demo.test.thread;

import com.jacky.tool.util.Util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Jacky on 2020/6/16
 */
public class FutureTaskTest {
    static {
        Util.i("hello static model");
    }

    public FutureTaskTest() {
        Util.i("hello constructor");
    }

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
            Util.sleepSafely(1000);
            return 1;
        }
    });

    private void start() {
        executor.execute(futureTask);
        try {
            Util.i("start[%d]", System.currentTimeMillis());
            final Integer integer = futureTask.get();
            Util.i("end[%d] result:%d", System.currentTimeMillis(), integer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FutureTaskTest().start();
    }
}
