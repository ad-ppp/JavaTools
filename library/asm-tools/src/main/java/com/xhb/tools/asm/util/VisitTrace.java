package com.xhb.tools.asm.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileInputStream;
import java.io.PrintWriter;

/**
 * Created by Jacky on 2020-05-16
 * visit trace by TraceClassVisitor
 */
public class VisitTrace implements Opcodes {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("no class path");
        }

        if (!args[0].endsWith(".class")) {
            throw new IllegalArgumentException("the class path should end with class");
        }

        FileInputStream is;
        try {
            is = new FileInputStream(args[0]);
            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            final TraceClassVisitor traceClassVisitor =
                new TraceClassVisitor(cw, new PrintWriter(System.out));
            cr.accept(traceClassVisitor, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
