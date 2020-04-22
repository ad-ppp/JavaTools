package com.jacky.tools;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarSearcher {
    private static String dir;
    private static String path;
    private static String traceDir;
    private static final Set<String> targets = new HashSet<>();
    private static final Set<String> traces = new HashSet<>();
    private static final List<String> zipSuffix = Arrays.asList(".jar", ".zip");

    public static void main(String[] args) {
        if (checkHelp(args)) return;
        final File dir = checker(args);
        startSearch(dir);
        dumpResult();
        try {
            dumpTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkHelp(String[] args) {
        if (args.length == 1 && args[0].equals("-h")) {
            showUserGuide();
            return true;
        }
        return false;
    }

    private static void dumpResult() {
        if (targets.isEmpty()) {
            log("No Result for %s in Path[$s]", path, dir);
            return;
        }
        log("Found result for %s in Path[$s]", path, dir);
        final Iterator<String> iterator = targets.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            log("%2d. .%s", index, simplyPath(iterator.next()));
            index++;
        }
        log("================================end================================");
    }

    private static void dumpTrace() throws IOException {
        if (traceDir != null) {
            final Date date = new Date(System.currentTimeMillis());
            final String format = new SimpleDateFormat("MM-dd_HH:mm").format(date);
            final String fileName = format + "_trace.txt";
            final File file = new File(traceDir, fileName);

            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file, true);
                for (String trace : traces) {
                    final String content = new String(trace.getBytes(StandardCharsets.UTF_8)
                        , StandardCharsets.UTF_8);
                    fileWriter.write(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    fileWriter.flush();
                }
                closeQuite(fileWriter);
            }
        }
    }

    private static void startSearch(File file) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                if (listFile.isFile()) {
                    searchFile(listFile);
                } else if (listFile.isDirectory()) {
                    startSearch(listFile);
                }
            }
        } else {
            searchFile(file);
        }
    }

    private static void searchFile(File listFile) {
        if (isZipOrJar(listFile)) {
            if (!tryJarEntry(listFile)) {
                tryFile(listFile);
            }
        } else {
            tryFile(listFile);
        }
    }

    private static void tryFile(File listFile) {
        final String classPath = flatDirToClass(listFile.getAbsolutePath());
        traces.add(String.format("[file:%s]: \n%s\n"
            , simplyPath(listFile.getAbsolutePath())
            , simplyPath(classPath)));
        if (containsIgnoreCase(classPath)) {
            captureTarget(listFile.getAbsolutePath());
        }
    }

    private static boolean tryJarEntry(File listFile) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(listFile);
            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                final JarEntry jarEntry = entries.nextElement();
                final String classPath = flatDirToClass(jarEntry.getName());
                traces.add(String.format("[jar:%s]: \n%s\n"
                    , simplyPath(listFile.getAbsolutePath())
                    , simplyPath(classPath)));
                if (containsIgnoreCase(classPath)) {
                    captureTarget(listFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuite(jarFile);
        }

        return true;
    }

    private static boolean containsIgnoreCase(String classPath) {
        return classPath.toLowerCase().contains(JarSearcher.path);
    }

    private static void captureTarget(String targetPath) {
        targets.add(targetPath);
    }

    private static String flatDirToClass(String absolutePath) {
        return absolutePath
            .replace(JarSearcher.dir, "")
            .replace("/", ".");
    }

    private static String simplyPath(String fullPath) {
        return fullPath.replace(dir, "");
    }

    private static File checker(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("please input dir and fileName");
        }

        final String dirPath = args[0];
        final File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new IllegalStateException(String.format("file[%s] is not exists", dir));
        }

        JarSearcher.dir = dirPath;
        JarSearcher.path = args[1];

        if (args.length == 3) {
            final String traceDir = args[2];
            final File traceFile = new File(traceDir);
            if (!traceFile.exists() && traceFile.isFile()) {
                throw new IllegalArgumentException(
                    String.format("the dir[%s] for trace is not exists or is a file", traceDir)
                );
            }

            JarSearcher.traceDir = traceDir;
        }

        log("===============================Search===============================");
        log("search:\n[%s]", JarSearcher.dir);
        log("path:\n[%s]", JarSearcher.path);
        log("%s", "\n\n\n");
        return dir;
    }

    private static void showUserGuide() {
        log("===============================help===============================");
        log("%s", "The tool is intent to search file in dir or jar/zip file");
        log("%s", "GIT URL: git@github.com:ad-ppp/JavaTools.git");
        log("%s", "-h for helper");
        log("%s", "The arguments are as followers:");
        log("\t%s", "1) the path for dir or file ");
        log("\t%s", "2) the file name of searched");
        log("\t%s", "3) the path for output trace");
        log("%s", "\n");
    }

    private static boolean isZipOrJar(File listFile) {
        for (String suffix : zipSuffix) {
            if (listFile.getAbsolutePath().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    private static void closeQuite(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
