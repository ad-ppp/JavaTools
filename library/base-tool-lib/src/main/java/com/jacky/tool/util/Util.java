package com.jacky.tool.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

import sun.misc.Unsafe;

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

    public static boolean sleepSafely(long time) {
        try {
            Thread.sleep(time);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void closeQuietly(Closeable closeable){
        if (closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Unsafe getUnsafe() {
        final Class<?> unSafeClass;
        try {
            unSafeClass = Class.forName("sun.misc.Unsafe");
            final Field field = unSafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
