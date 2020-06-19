package com.jacky.demo.test.clazz;

import com.jacky.tool.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Jacky on 2020/6/19
 * <p>
 * link: https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650238826&idx=1&sn=294d91d9a190ea56822696994f8a04b0&chksm=88639e05bf1417137f364037bb35a8fcc120679c92b924cfabffa7011199d8ac8bba69a25194&scene=38#wechat_redirect
 */
public class DiskClassLoaderTest {
    void start() {
        final DiskClassLoader diskClassLoader = new DiskClassLoader("D://");
        ClassLoader entry = diskClassLoader;

        while (entry != null) {
            Util.i("classLoader: %s", entry);
            entry = entry.getParent();
        }
    }

    public static void main(String[] args) {
        new DiskClassLoaderTest().start();
    }

    private static class DiskClassLoader extends ClassLoader {
        private final String mLibPath;

        public DiskClassLoader(String mLibPath) {
            this.mLibPath = mLibPath;
        }

        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] classData = getClassData(name);
            if (classData == null) {
                throw new ClassNotFoundException();
            } else {
                return defineClass(name, classData, 0, classData.length);
            }
        }

        private byte[] getClassData(String className) {
            String path = classNameToPath(className);
            try {
                InputStream ins = new FileInputStream(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                int bytesNumRead = 0;
                while ((bytesNumRead = ins.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesNumRead);
                }
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String classNameToPath(String className) {
            return mLibPath + File.separatorChar
                + className.replace('.', File.separatorChar) + ".class";
        }
    }
}
