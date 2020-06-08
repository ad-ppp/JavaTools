package com.jacky.tool.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jacky on 2020/5/28
 */
public class TaskContainer {
    private static final int TIMEOUT_SECONDS = 600;
    private int timeoutSeconds = TIMEOUT_SECONDS;

    private ExecutorService executor;
    private final List<ITask> tasks = new ArrayList<>();

    public TaskContainer() {
        this(1);
    }

    public TaskContainer(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("num can be <=0");
        }

        this.executor = Executors.newFixedThreadPool(num);
    }

    public void addTask(ITask task) {
        if (tasks.contains(task)) {
            throw new IllegalStateException("the task has added to container");
        }

        tasks.add(task);
    }

    public List<Future<TaskResult>> executeTasks() throws Exception {
        if (executor.isShutdown()) {
            throw new IllegalStateException("can not execute tasks twice");
        }

        final List<Future<TaskResult>> futures = executor.invokeAll(tasks, timeoutSeconds, TimeUnit.SECONDS);
        executor.shutdown();
        return futures;
    }

}
