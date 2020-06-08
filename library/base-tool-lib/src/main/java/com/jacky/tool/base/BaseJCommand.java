package com.jacky.tool.base;

import com.beust.jcommander.JCommander;

/**
 * Created by Jacky on 2020/5/28
 */
public abstract class BaseJCommand<T extends BaseJModel> {
    private final JCommander jCommander;
    private final T arguments;

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
}
