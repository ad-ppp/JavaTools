package com.jacky.tool.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectUtil {

    public static <T> T reflectObject(Object instance, String name) {
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method reflectMethod(Object instance, String name, Class<?>... argTypes) {
        try {
            Method method = instance.getClass().getDeclaredMethod(name, argTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
