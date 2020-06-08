package com.jacky.tool.apk.main;

import com.beust.jcommander.JCommander;
import com.google.common.base.Strings;
import com.jacky.tool.apk.tasks.AsrcTask;
import com.jacky.tool.base.BaseJCommand;
import com.jacky.tool.task.TaskContainer;
import com.jacky.tool.task.TaskResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Jacky on 2020/5/27
 */
public class ApkTools extends BaseJCommand<Arguments> {

    private ApkTools(JCommander jCommander, Arguments arguments) {
        super(jCommander, arguments);
    }

    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        final JCommander jCommander = JCommander.newBuilder()
            .addObject(arguments)
            .build();
        jCommander.setProgramName("ApkTools");
        jCommander.parse(args);

        final ApkTools apkTools = new ApkTools(jCommander, arguments);
        apkTools.start();
    }

    @Override
    public void handle(JCommander jCommander, Arguments arguments) {
        final String arsc = arguments.arsc;
        final TaskContainer taskContainer = new TaskContainer(1);
        if (!Strings.isNullOrEmpty(arsc)) {
            final File file = new File(arsc);
            taskContainer.addTask(new AsrcTask(file));
        }

        try {
            final List<Future<TaskResult>> futures = taskContainer.executeTasks();
            doResults(futures);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doResults(List<Future<TaskResult>> futures) {
        for (Future<TaskResult> future : futures) {
            try {
                final TaskResult taskResult = future.get();
                doResult(taskResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doResult(TaskResult taskResult) {

    }
}
