package com.xhb.tools.asm.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

        if (!new File(args[0]).exists()) {
            throw new IllegalArgumentException("the class path does not exist");
        }

        try {
            FileInputStream is = new FileInputStream(args[0]);
            final VisitTrace visitTrace = new VisitTrace();
            visitTrace.start(is, new PrintWriter(System.out));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(InputStream inputStream, PrintWriter printWriter) throws Exception {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        final TraceClassVisitor traceClassVisitor =
            new TraceClassVisitor(cw, printWriter);
        cr.accept(traceClassVisitor, 0);
    }
}
