package com.jacky.demo.test.reflect;

import com.jacky.tool.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Jacky on 2020/7/7
 *
 * 子线程中反射，跟主线程反射一样，没有任何问题
 */
public class ReflectInThreadMain {

    void start() {
        final Person jack = new Person(10, "jack");
        Util.i(jack.toString());

        Util.startThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Class<?> aClass = Class.forName("com.jacky.demo.test.reflect.Person");
                    final Field ageField = aClass.getDeclaredField("age");
                    ageField.setAccessible(true);
                    ageField.set(jack, 20);
                    Util.i(jack.toString());

                    ageField.set(jack, 30);
                    final Method sayHello = aClass.getDeclaredMethod("sayHello", null);
                    sayHello.setAccessible(true);
                    final String hello = (String) sayHello.invoke(jack, null);
                    Util.i(hello);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new ReflectInThreadMain().start();
    }
}
