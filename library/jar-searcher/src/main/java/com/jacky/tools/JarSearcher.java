package com.jacky.tools;

import com.jacky.tool.base.BaseTool;
import com.jacky.tool.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private String text;
    private String traceDir;
    private final Set<String> targets = new HashSet<>();
    private final Set<String> traces = new HashSet<>();
    private final List<String> zipSuffix = Arrays.asList(".jar", ".zip", ".aar");


    private static final String FOR_TEXT = "-t";

    private final int SEARCH_FILE = 1;
    private final int SEARCH_TEXT = 2;
    private int searchMode = SEARCH_FILE;

    public JarSearcher(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        new JarSearcher(args).start();
    }

    private boolean isSearchFile() {
        return searchMode == SEARCH_FILE;
    }

    private boolean isSearchText() {
        return searchMode == SEARCH_TEXT;
    }

    @Override
    public void handlerArgs() {
        log("===============================Params===============================");
        log("search:\n[%s]", this.dir);
        log("path:\n[%s]", this.text);
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

        int index;
        if (args[1].trim().equals(FOR_TEXT)) {
            searchMode = SEARCH_TEXT;
            index = 2;
        } else {
            index = 1;
        }
        text = args[index];

        if (text == null || text.equals("")) {
            throw new IllegalArgumentException();
        }

        if (args.length == (index + 1) + 1) {
            final String traceDir = args[index + 1];
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
            log("No Result for %s in Path:\n[%s]\n", text, dir);
            return;
        }
        log("Found result for %s in Path\n[%s]\n", text, dir);
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

        if (isSearchText()) {
            try {
                tryHitStream(new FileInputStream(listFile), listFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isSearchFile()) {
            if (containsIgnoreCase(classPath)) {
                captureTarget(listFile.getAbsolutePath());
            }
        }
    }

    private void tryHitStream(InputStream inputStream, String path) throws Exception {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(text)) {
                    // hit
                    captureTarget(path);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(bufferedReader);
            Util.closeQuietly(inputStream);
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

                if (isSearchText()) {
                    try {
                        tryHitStream(jarFile.getInputStream(jarEntry), jarEntry.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (isSearchFile()) {
                    if (containsIgnoreCase(classPath)) {
                        captureTarget(listFile.getAbsolutePath());
                    }
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
        return classPath.toLowerCase().contains(text.toLowerCase());
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
