package com.xhb.tools.asm.test;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jacky on 2020-05-16
 * <p>
 * 在方法： tell(Object object):void class文件中插入代码
 * System.out.println("tell intercept")
 */
public class InjectByteCode implements Opcodes {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("no class path");
        }

        try {
            final InputStream inputStream = new FileInputStream(args[0]);
            ClassReader classReader = new ClassReader(inputStream);
            final ClassWriter classWriter = new ClassWriter(ASM5);
            classReader.accept(new MyClassVisitor(classWriter), ClassReader.EXPAND_FRAMES);

            final File out = new File(args[1]);
            new FileOutputStream(out).write(classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MyClassVisitor extends ClassVisitor implements Opcodes {
        MyClassVisitor(ClassVisitor classVisitor) {
            super(ASM5, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                         String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            return mv == null ? null : new MethodAdapter(mv);
        }
    }

    private static class MethodAdapter extends MethodVisitor implements Opcodes {

        MethodAdapter(final MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            /* TODO: System.err.println("CALL" + name); */
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("CALL println");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            /* do call */
            mv.visitMethodInsn(opcode, owner, name, desc, itf);

            /* TODO: System.err.println("RETURN" + name);  */
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("RETURN " + name);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }
}
