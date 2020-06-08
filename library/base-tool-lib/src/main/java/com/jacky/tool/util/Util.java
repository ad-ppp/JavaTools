package com.jacky.tool.util;

/**
 * Created by Jacky on 2020/5/23
 */
public class Util {
    public static void i(Object format, Object... obj) {
        final String name = Thread.currentThread().getName();
        if (format instanceof String) {
            System.out.println(name + "," + String.format((String) format, obj));
            return;
        }

        System.out.println(name + "," + format.toString());
    }

    public static void startThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static void startThread(String threadName, Runnable runnable) {
        new Thread(runnable, threadName).start();
    }

    public static void sleepSafely(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
