package com.xhb.tools.asm.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Jacky on 2020-05-16
 * <p>
 * 制作一个.class文件拷贝
 */
public class ClassCopy {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("no class path && no out class path");
        }

        FileInputStream is;
        try {
            is = new FileInputStream(args[0]);
            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            cr.accept(cw, 0);

            FileOutputStream fos = new FileOutputStream(args[1]);
            fos.write(cw.toByteArray());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
