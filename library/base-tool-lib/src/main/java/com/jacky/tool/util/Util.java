package com.jacky.tool.util;

import com.jacky.tool.core.function.Function;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jacky on 2020/5/23
 */
public class Util {
    /**
     * for release
     *
     * @param format
     * @param obj
     */
    public static void r(Object format, Object... obj) {
        if (format instanceof String) {
            System.out.println(String.format(Locale.ENGLISH, (String) format, obj));
            return;
        }

        System.out.println(format.toString());
    }

    public static void i(Object format, Object... obj) {
        final String name = Thread.currentThread().getName();
        if (format instanceof String) {
            System.out.println(name + "," + String.format((String) format, obj));
            return;
        }

        System.out.println(name + "," + format.toString());
    }

    public static void showDivider(String tag) {
        Util.r("---------------------- %s ----------------------", tag);
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

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // return sun.misc.Unsafe;
    public static Object getUnsafe() {
        final Class<?> unSafeClass;
        try {
            unSafeClass = Class.forName("sun.misc.Unsafe");
            final Field field = unSafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T checkNoNull(T t) {
        if (t == null) {
            throw new NullPointerException("object must not be null");
        }
        return t;
    }

    public static <T, R> List<R> convertList(List<T> ts, Function<T, R> convert) {
        if (ts == null || ts.isEmpty()) {
            return null;
        }

        List<R> rs = new ArrayList<>();
        for (T t : ts) {
            rs.add(convert.apply(t));
        }

        return rs;
    }

}
