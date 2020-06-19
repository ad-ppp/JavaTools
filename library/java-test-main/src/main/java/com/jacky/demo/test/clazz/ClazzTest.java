package com.jacky.demo.test.clazz;

import com.jacky.tool.util.Util;

import sun.security.provider.Sun;

/**
 * Created by Jacky on 2020/6/18
 *
 * link: https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650238826&idx=1&sn=294d91d9a190ea56822696994f8a04b0&chksm=88639e05bf1417137f364037bb35a8fcc120679c92b924cfabffa7011199d8ac8bba69a25194&scene=38#wechat_redirect
 *
 * 装载：（loading）找到class对应的字节码文件。
 * 连接（验证，准备，解析）：（linking）将对应的字节码文件读入到JVM中。
 * 初始化：（initializing）对class做相应的初始化动作。
 * <p>
 * <p>
 * Class.forName();              loading;linking;initializing；静态代码块会被执行.
 * ClassLoader.loadClass()       just loading;
 * <p>
 * <p>
 * java classLoader:
 * main,classLoader:sun.misc.Launcher$AppClassLoader@18b4aac2
 * main,classLoader:sun.misc.Launcher$ExtClassLoader@6e0be858
 */
public final class ClazzTest {

    void start() {
        final String clazz = "com.jacky.demo.test.clazz.ClazzTest$Person";
        try {
            final Class<?> aClass = Class.forName(clazz);
            final Object o = aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final ClassLoader classLoader = ClazzTest.class.getClassLoader();
            ClassLoader entry = classLoader;
            while (entry != null) {
                Util.i("classLoader:%s", entry);
                entry = entry.getParent();
            }
            // just loading class
            classLoader.loadClass(clazz);

            // BootstrapClassLoader 加载路径
            Util.i("boot load path:\n%s\n", System.getProperty("sun.boot.class.path")
                .replace(":", "\n"));

            // ExtClassLoader 加载路径
            Util.i("ext load path:\n%s\n", System.getProperty("java.ext.dirs")
                .replace(":", "\n"));

            // AppClassLoader
            Util.i("java class load path:\n%s\n", System.getProperty("java.class.path")
                .replace(":", "\n"));

            // BootstrapClassLoader 加载的 Class 为什么 getClassLoader为null;
            Util.i("sun classLoader:%s", Sun.class.getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClazzTest().start();
    }

    static class Person {
        static {
            Util.i("static block");
        }

        public Person() {
            Util.i("construct");
        }
    }
}
