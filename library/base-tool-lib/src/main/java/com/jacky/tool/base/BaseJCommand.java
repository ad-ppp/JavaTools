package com.jacky.tool.base;

import com.beust.jcommander.JCommander;

/**
 * Created by Jacky on 2020/5/28
 */
public abstract class BaseJCommand<T extends BaseJModel> {
    protected final JCommander jCommander;
    protected final T arguments;

    public BaseJCommand(JCommander jCommander, T arguments) {
        this.jCommander = jCommander;
        this.arguments = arguments;
    }

    protected void start() {
        if (jCommander.getParameters().isEmpty() || arguments.help) {
            jCommander.usage();
            return;
        }

        handle(jCommander, arguments);
    }

    public abstract void handle(JCommander jCommander, T arguments);

    public static String[] convertArgs(String[] args) {
        if (args == null || args.length == 0) {
            return new String[]{"-h"};
        }

        return args;
    }
}
