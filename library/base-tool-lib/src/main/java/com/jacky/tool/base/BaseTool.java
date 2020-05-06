package com.jacky.tool.base;

import java.io.Closeable;
import java.io.IOException;

public abstract class BaseTool {
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

    public abstract void showUserGuide();

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
