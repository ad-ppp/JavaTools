package com.jacky.tools;

import com.jacky.tool.base.BaseTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarSearcher extends BaseTool {
    private String dir;
    private String path;
    private String traceDir;
    private final Set<String> targets = new HashSet<>();
    private final Set<String> traces = new HashSet<>();
    private final List<String> zipSuffix = Arrays.asList(".jar", ".zip");

    public JarSearcher(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        new JarSearcher(args).start();
    }

    @Override
    public void handlerArgs() {
        log("===============================Params===============================");
        log("search:\n[%s]", this.dir);
        log("path:\n[%s]", this.path);
        log("%s", "\n\n\n");

        final File fileDir = new File(dir);
        startSearch(fileDir);
        dumpResult();
        try {
            dumpTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkArgs() {
        if (args.length < 2) {
            throw new IllegalArgumentException("please input dir and fileName");
        }

        final String dirPath = args[0];
        final File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new IllegalStateException(String.format("file[%s] is not exists", dir));
        }

        this.dir = dir.getAbsolutePath();
        this.path = args[1];

        if (args.length == 3) {
            final String traceDir = args[2];
            final File traceFile = new File(traceDir);
            if (!traceFile.exists() && traceFile.isFile()) {
                throw new IllegalArgumentException(
                    String.format("the dir[%s] for trace is not exists or is a file", traceDir)
                );
            }

            this.traceDir = traceDir;
        }
    }

    @Override
    public InputStream helpStream() {
        return getClass().getClassLoader().getResourceAsStream("jar-searcher.helper");
    }

    private void dumpResult() {
        if (targets.isEmpty()) {
            log("No Result for %s in Path:\n[%s]\n", path, dir);
            return;
        }
        log("Found result for %s in Path\n[%s]\n", path, dir);
        final Iterator<String> iterator = targets.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            log("%2d. .%s", index, simplyPath(iterator.next()));
            index++;
        }
        log("================================end================================");
    }

    private void dumpTrace() throws IOException {
        if (traceDir != null) {
            final Date date = new Date(System.currentTimeMillis());
            final String format = new SimpleDateFormat("MM_dd_HH_mm", Locale.ENGLISH).format(date);
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

    private void startSearch(File file) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                for (File listFile : files) {
                    if (listFile.isFile()) {
                        searchFile(listFile);
                    } else if (listFile.isDirectory()) {
                        startSearch(listFile);
                    }
                }
            }
        } else {
            searchFile(file);
        }
    }

    private void searchFile(File listFile) {
        if (isZipOrJar(listFile)) {
            if (!tryJarEntry(listFile)) {
                tryFile(listFile);
            }
        } else {
            tryFile(listFile);
        }
    }

    private void tryFile(File listFile) {
        final String classPath = flatDirToClass(listFile.getAbsolutePath());
        traces.add(String.format("[file:%s]: \n%s\n"
            , simplyPath(listFile.getAbsolutePath())
            , simplyPath(classPath)));
        if (containsIgnoreCase(classPath)) {
            captureTarget(listFile.getAbsolutePath());
        }
    }

    private boolean tryJarEntry(File listFile) {
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

    private boolean containsIgnoreCase(String classPath) {
        return classPath.toLowerCase().contains(path.toLowerCase());
    }

    private void captureTarget(String targetPath) {
        targets.add(targetPath);
    }

    private String flatDirToClass(String absolutePath) {
        return absolutePath
            .replace(dir, "")
            .replace("/", ".");
    }

    private String simplyPath(String fullPath) {
        return fullPath.replace(dir, "");
    }

    private boolean isZipOrJar(File listFile) {
        for (String suffix : zipSuffix) {
            if (listFile.getAbsolutePath().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }
}
