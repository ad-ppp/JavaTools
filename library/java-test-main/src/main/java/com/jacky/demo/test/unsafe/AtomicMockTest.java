package com.jacky.demo.test.unsafe;

import com.jacky.tool.util.Util;

import sun.misc.Unsafe;

/**
 * Created by Jacky on 2020/6/18
 */
public final class AtomicMockTest {
    private static long VALUE;
    private volatile int value;
    private static Unsafe U;

    static {
        Util.i("hello static model");

        try {
            final Unsafe U = Util.getUnsafe();
            AtomicMockTest.U = U;
            VALUE = U.objectFieldOffset(AtomicMockTest.class.getDeclaredField("value"));
            Util.i("value in static=%d", VALUE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public AtomicMockTest(int i) {
        this.value = i;
        Util.i("hello constructor");
    }

    public static void main(String[] args) {
        new AtomicMockTest(10).start();
    }

    private void start() {
        final Unsafe U = AtomicMockTest.U;
        if (U == null) {
            throw new IllegalStateException();
        }

        U.getAndSetLong(this, VALUE, 100);
        Util.i("value:%d", value);

        U.compareAndSwapLong(this, VALUE, 100, 101);
        Util.i("value:%d", value);

        U.compareAndSwapLong(this, VALUE, 100, 102);
        Util.i("value:%d", value);

        U.getAndSetLong(this, VALUE, 1000);
        Util.i("value:%d", value);
    }
}
