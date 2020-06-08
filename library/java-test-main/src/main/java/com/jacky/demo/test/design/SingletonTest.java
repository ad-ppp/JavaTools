package com.jacky.demo.test.design;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/6/8
 */
public class SingletonTest {

    public static void main(String[] args) {
        Util.i("start:%s", System.currentTimeMillis());

        for (int i = 0; i < 50; i++) {
            final int finalI = i;
            Util.startThread(new Runnable() {
                @Override
                public void run() {
                    if (finalI != 0) {
                        Util.sleepSafely(500);
                    }

                    final int count = Hello.getInstance().getCount();
                    Util.i("time:%d, count:%d", System.currentTimeMillis(), count);
                }
            });
        }

        Util.sleepSafely(500);
        Util.i("begin[%d]", System.currentTimeMillis());
        final long start = System.currentTimeMillis();
        final int result = Hello.getInstance().getCount();
        Util.i("%d cost time: %d ms", result, (System.currentTimeMillis() - start));
        Util.i("end:%s", System.currentTimeMillis());
    }

    static class Hello {
        private static Hello instance = new Hello();
        private int count = 0;

        private Hello() {
            // mock evil method
            Util.i("[i:%s] %d", Thread.currentThread().getName(), System.currentTimeMillis());
            count++;
            Util.sleepSafely(2000);
            Util.i("[o:%s] %d", Thread.currentThread().getName(), System.currentTimeMillis());
        }

        static Hello getInstance() {
            return instance;
        }

        int getCount() {
            return count;
        }
    }
}
