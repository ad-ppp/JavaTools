package com.jacky.tool.task;

import java.util.concurrent.Callable;

/**
 * Created by Jacky on 2020/5/28
 */
public interface ITask extends Callable<TaskResult> {
    void init();
}
