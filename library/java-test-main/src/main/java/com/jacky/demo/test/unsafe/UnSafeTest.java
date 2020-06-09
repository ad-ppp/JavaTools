package com.jacky.demo.test.unsafe;

import com.jacky.tool.util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sun.misc.Unsafe;

/**
 * Created by Jacky on 2020/6/9
 * https://mp.weixin.qq.com/s/jVRTFTiwTtr7P7vyAj8G7A
 */
public class UnSafeTest {

    public static void main(String[] args) {
//        new Student();
//        final Unsafe unsafe = Unsafe.getUnsafe();

        // crash
        try {
            final Constructor<?>[] constructors = Student.class.getDeclaredConstructors();
            constructors[0].setAccessible(true);
            final Student s = (Student) constructors[0].newInstance();
            Util.i("constructor length:%d, age:%d", constructors.length, s.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // normal
        try {
            final Class<?> unSafeClass = Class.forName("sun.misc.Unsafe");
            final Field field = unSafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            final Unsafe unsafe = (Unsafe) field.get(null);
            final Method allocateInstance = unSafeClass.getMethod("allocateInstance", Class.class);
            final Student student = (Student) allocateInstance.invoke(unsafe, Student.class);
            Util.i(student.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Student {
        private Student() {
            throw new IllegalArgumentException("can not create");
        }

        int getAge() {
            return 10;
        }
    }
}
