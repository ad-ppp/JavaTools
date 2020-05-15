package com.xhb.tools.asm.test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jacky on 2020-05-15
 */
public class BytecodeGen implements Opcodes {
    final static String HOST = "library/asm-tools/gen/src/com/xhb/tools/asm/TestWorld.class";
    final static String HOST_CLASS = "com/xhb/tools/asm/TestWorld";
    final static File outFile = new File(HOST);

    public static void main(String[] args) {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, HOST_CLASS, null, "java/lang/Object", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            mv.visitMethodInsn(INVOKESTATIC, HOST_CLASS, "printOne", "()V", false);
            mv.visitMethodInsn(INVOKESTATIC, HOST_CLASS, "printOne", "()V", false);
            mv.visitMethodInsn(INVOKESTATIC, HOST_CLASS, "printTwo", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "printOne", "()V", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("Hello World");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "printTwo", "()V", null, null);
            mv.visitCode();
            mv.visitMethodInsn(INVOKESTATIC, HOST_CLASS, "printOne", "()V", false);
            mv.visitMethodInsn(INVOKESTATIC, HOST_CLASS, "printOne", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        cw.toByteArray();

        try {
            outFile.getParentFile().mkdirs();
            new FileOutputStream(outFile).write(cw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
