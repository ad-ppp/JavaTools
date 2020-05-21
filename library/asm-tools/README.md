# 工具列表
#### Copy 程序命令行运行步骤
- `cd ~/myWidget/android/ad-ppp/JavaTools/library/asm-tools/src/main/java`
- `javac -cp $asmjar com/xhb/tools/asm/util/ClassCopy.java`
- `java -cp .:$asmjar com.xhb.tools.asm.util.ClassCopy $class1 $class2`

#### VisitTrace
通过 ASM-TraceClassVisitor ,将class字节码显示到 System.out

#### JarOrClassTracer
通过 ASM-TraceClassVisitor，将jar or class文件visitor过程输出到 PrintWriter
- `java -cp $asm-tools.jar com.xhb.tools.asm.util.JarOrClassTracer`

---

## 几个工具对比
#### javap 对代码进行反汇编:
javap -c ${HelloWorld-class}

```
Compiled from "HelloWorld.java"
public class com.xhb.tools.asm.test.HelloWorld {
  public com.xhb.tools.asm.test.HelloWorld();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: new           #2                  // class com/xhb/tools/asm/test/HelloWorld
       3: dup
       4: invokespecial #3                  // Method "<init>":()V
       7: astore_1
       8: aload_1
       9: new           #4                  // class java/lang/Object
      12: dup
      13: invokespecial #1                  // Method java/lang/Object."<init>":()V
      16: invokespecial #5                  // Method tell:(Ljava/lang/Object;)V
      19: return
}

```

#### 本项目中的 VisitTrace 工具:
`java -cp tools/lib/asm-tools.jar com.xhb.tools.asm.util.VisitTrace $classPath`

展示的是对class对visit过程
```
// class version 52.0 (52)
// access flags 0x21
public class com/xhb/tools/asm/test/HelloWorld {

  // compiled from: HelloWorld.java

  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 6 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x9
  public static main([Ljava/lang/String;)V
   L0
    LINENUMBER 8 L0
    NEW com/xhb/tools/asm/test/HelloWorld
    DUP
    INVOKESPECIAL com/xhb/tools/asm/test/HelloWorld.<init> ()V
    ASTORE 1
   L1
    LINENUMBER 9 L1
    ALOAD 1
    NEW java/lang/Object
    DUP
    INVOKESPECIAL java/lang/Object.<init> ()V
    INVOKESPECIAL com/xhb/tools/asm/test/HelloWorld.tell (Ljava/lang/Object;)V
   L2
    LINENUMBER 10 L2
    RETURN
    MAXSTACK = 3
    MAXLOCALS = 2

  // access flags 0x2
  private tell(Ljava/lang/Object;)V
   L0
    LINENUMBER 13 L0
    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    LDC "tell..."
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L1
    LINENUMBER 14 L1
    RETURN
    MAXSTACK = 2
    MAXLOCALS = 2
}
```

#### Asmifier 工具:
`java -cp ${asmJar} org.objectweb.asm.util.ASMifier $classPath`

展示的是如何用asm框架生成指定class文件
```
package asm.com.xhb.tools.asm.test;
import java.util.*;
import org.objectweb.asm.*;
public class HelloWorldDump implements Opcodes {

public static byte[] dump () throws Exception {

ClassWriter cw = new ClassWriter(0);
FieldVisitor fv;
MethodVisitor mv;
AnnotationVisitor av0;

cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/xhb/tools/asm/test/HelloWorld", null, "java/lang/Object", null);

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
mv.visitTypeInsn(NEW, "com/xhb/tools/asm/test/HelloWorld");
mv.visitInsn(DUP);
mv.visitMethodInsn(INVOKESPECIAL, "com/xhb/tools/asm/test/HelloWorld", "<init>", "()V", false);
mv.visitVarInsn(ASTORE, 1);
mv.visitVarInsn(ALOAD, 1);
mv.visitTypeInsn(NEW, "java/lang/Object");
mv.visitInsn(DUP);
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
mv.visitMethodInsn(INVOKESPECIAL, "com/xhb/tools/asm/test/HelloWorld", "tell", "(Ljava/lang/Object;)V", false);
mv.visitInsn(RETURN);
mv.visitMaxs(3, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PRIVATE, "tell", "(Ljava/lang/Object;)V", null, null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
mv.visitLdcInsn("tell...");
mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
mv.visitInsn(RETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
cw.visitEnd();

return cw.toByteArray();
}
}
```

