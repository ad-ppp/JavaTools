package com.jacky.demo.test.concurrent.cas;

import com.jacky.tool.util.Util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jacky on 2020/5/23
 */
public class CAS {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        Util.i(atomicInteger.compareAndSet(5, 50));
        Util.i(atomicInteger.get());

        Util.i(atomicInteger.compareAndSet(5, 100));
        Util.i(atomicInteger.get());


    }
}
