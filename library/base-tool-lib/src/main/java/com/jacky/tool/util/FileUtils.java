package com.jacky.tool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Jacky on 2020/8/15
 */
public final class FileUtils {

    public static String readFile(String path) {
        final File file = new File(path);
        if (!file.exists()) {
            final String format = String.format("file:%s is not exists", path);
            throw new IllegalArgumentException(format);
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        FileReader in = null;
        try {
            in = new FileReader(file);
            bufferedReader = new BufferedReader(in);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        } finally {
            Util.closeQuietly(in);
            Util.closeQuietly(bufferedReader);
        }
    }
}
