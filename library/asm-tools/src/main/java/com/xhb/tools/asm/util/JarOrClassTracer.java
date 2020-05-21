package com.xhb.tools.asm.util;

import com.jacky.tool.base.BaseTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Jacky on 2020/5/20
 */
public class JarOrClassTracer extends BaseTool {
    private List<String> classPaths = new ArrayList<>();
    private List<String> jarPaths = new ArrayList<>();
    private String tracePath;
    private PrintWriter printWriter;

    private JarOrClassTracer(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        new JarOrClassTracer(args).start();
    }

    @Override
    public void checkArgs() {
        try {
            doCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlerArgs() {
        final OutputStream outputStream;
        try {
            File file = null;
            if (tracePath != null) {
                file = new File(tracePath);
                if (file.exists()) {
                    file.delete();
                }
            }
            outputStream = tracePath == null ?
                System.out : new FileOutputStream(file, true);
            printWriter = new PrintWriter(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (printWriter == null) {
            return;
        }

        for (String classPath : classPaths) {
            try {
                traceClass(null, classPath, new FileInputStream(classPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        for (String jarPath : jarPaths) {
            try {
                final ZipFile zipFile = new ZipFile(jarPath);
                final Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    final ZipEntry zipEntry = entries.nextElement();
                    if (!zipEntry.getName().endsWith(".class")) {
                        continue;
                    }

                    final InputStream inputStream = zipFile.getInputStream(zipEntry);
                    traceClass(jarPath, zipEntry.getName(), inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public InputStream helpStream() {
        return getClass().getClassLoader().getResourceAsStream("JarOrClassTrace.helper");
    }

    private void traceClass(final String jarPath, String name, InputStream inputStream) {
        try {
            if (jarPath != null) {
                printWriter
                    .append("start trace jar:").append(jarPath)
                    .append("\nname：").append(name.replace("/","."))
                    .append("\n");
            }

            final VisitTrace visitTrace = new VisitTrace();
            visitTrace.start(inputStream, printWriter);
            printWriter.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doCheck() {
        for (int i = 0; i < args.length; i++) {
            if ("-c".equals(args[i])) {
                final String[] split = args[i + 1].split(PARAM_DIVIDER);
                for (String s : split) {
                    classPaths.add(toAbsPath(s));
                }
            }

            if ("-j".equals(args[i])) {
                final String[] split = args[i + 1].split(PARAM_DIVIDER);
                for (String s : split) {
                    jarPaths.add(toAbsPath(s));
                }
            }

            if ("-t".equals(args[i])) {
                final String trace = args[i + 1];
                new File(trace).getAbsoluteFile().mkdirs();
                this.tracePath = trace;
            }
        }
    }

    private String toAbsPath(String path) {
        final File file = new File(path);
        if (!file.exists()) {
            throw new IllegalStateException(String.format("The file[%s] is not exist", path));
        }

        return file.getAbsolutePath();
    }
}
