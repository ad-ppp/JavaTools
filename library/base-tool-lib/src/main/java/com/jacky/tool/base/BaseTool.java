package com.jacky.tool.base;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class BaseTool {
    protected static final String PARAM_DIVIDER = ";";
    protected final String[] args;

    public BaseTool(String[] args) {
        this.args = args;
    }

    public void start() {
        if (checkHelper()) {
            return;
        }

        checkArgs();
        handlerArgs();
    }


    private boolean checkHelper() {
        if (args.length == 1 && "-h".equals(args[0]) || args.length == 0) {
            showUserGuide();
            return true;
        }
        return false;
    }

    public abstract void checkArgs();

    public abstract void handlerArgs();

    public abstract InputStream helpStream();

    private void showUserGuide() {
        final InputStream inputStream = helpStream();
        if (inputStream == null) {
            throw new IllegalStateException("you should provider stream for helper");
        }
        System.out.println(extraStreamToString(inputStream));
    }

    private String extraStreamToString(InputStream inputStream) {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            final StringBuilder strBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                strBuilder.append(line).append("\n");
            }
            strBuilder.deleteCharAt(strBuilder.length() - 1);
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    // tools
    protected final void closeQuite(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected final void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
