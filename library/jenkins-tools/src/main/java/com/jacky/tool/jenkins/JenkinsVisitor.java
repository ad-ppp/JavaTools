package com.jacky.tool.jenkins;

import com.beust.jcommander.JCommander;
import com.jacky.tool.base.BaseJCommand;
import com.jacky.tool.jenkins.model.JobRequest;
import com.jacky.tool.util.Strings;
import com.jacky.tool.util.Util;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Executable;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueItem;
import com.offbytwo.jenkins.model.QueueReference;

import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ---------------------- show available jobs begin ----------------------
 * android.blackboard.develop
 * android.blackboard.release
 * android.blackboard.release.multijob
 * android.playground
 * android.pre.yt_app_phone.release
 * android.test.blackboard.release
 * android.test.blackboard.release-onlybuild
 * android.test.blackboard.release.multijob
 * android.test.blackboard.release.multijob-onlybuild
 * android.test.yt_app_phone.release
 * devops.xiao
 * Mars
 * Mars.test
 * ---------------------- show available jobs end ----------------------
 * <p>
 * <p>
 * TODO: add action to scan log of build
 */
public class JenkinsVisitor extends BaseJCommand<RequestModule> {
    private final JenkinsServer jenkinsServer;
    private final static int MAX_TRY_COUNT = 30;
    public final static int LOOK_JOB_DETAIL_INTERVAL = 400; //ms

    public static void main(String[] args) {
        JCommander jCommander = null;
        try {
            final RequestModule requestModule = new RequestModule();
            jCommander = JCommander.newBuilder()
                .addObject(requestModule)
                .build();
            jCommander.setProgramName("Jenkins-Tool");
            jCommander.parse(BaseJCommand.convertArgs(args));

            if (Strings.isNotBlank(requestModule.config)) {
                jCommander.parse(ConfigParser.parse(requestModule.config));
            }

            // check local config
            final LocalConfig delegate = requestModule.delegate;
            if (Strings.isNullOrEmpty(delegate.user)) {
                throw new IllegalStateException("no user found");
            }

            if (Strings.isNullOrEmpty(delegate.token)) {
                throw new IllegalStateException("no private access token found");
            }

            if (Strings.isNullOrEmpty(delegate.url)) {
                throw new IllegalStateException("no jenkins url found");
            }

            final URI uri = new URI(delegate.url);
            final HttpClientBuilder builder = HttpClientBuilder.create();
            final JenkinsHttpClient client = new JenkinsHttpClient(uri, builder, delegate.user, delegate.token);
            final JenkinsServer jenkinsServer = new JenkinsServer(client);


            new JenkinsVisitor(jenkinsServer, jCommander, requestModule).start();
        } catch (Exception e) {
            e.printStackTrace();

            if (jCommander != null) {
                jCommander.usage();
            }
        }
    }

    public JenkinsVisitor(JenkinsServer jenkinsServer, JCommander jCommander, RequestModule arguments) {
        super(jCommander, arguments);
        this.jenkinsServer = jenkinsServer;
    }

    @Override
    public void handle(JCommander jCommander, RequestModule arguments) {
        if (arguments.showAvailableJobs) {
            showAvailableJobs();
            return;
        }

        try {
            handleSafe();
            Util.r("\n---------------------- ALL FINISHED!!! ----------------------\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void handleSafe() throws Exception {
        if (Strings.isNullOrEmpty(arguments.jobName)) {
            throw new IllegalArgumentException("no job name init");
        }
        final AtomicInteger atomicInteger = new AtomicInteger(1);
        Util.startThread(waitConnectRunnable(atomicInteger));
        final JobWithDetails entryJob = jenkinsServer.getJob(arguments.jobName);
        atomicInteger.decrementAndGet();

        if (entryJob == null) {
            final String format = String.format("Can not find job:%s", arguments.jobName);
            throw new IllegalStateException(format);
        }

        Map<String, String> params = arguments.params;
        if (entryJob.isInQueue()) {
            // todo 取消 任务
        }

        Util.r("%s is in queue? %b", entryJob.getName(), entryJob.isInQueue());

        final List<JobRequest> jobRequests = new ArrayList<>();
        // main job
        jobRequests.add(new JobRequest(entryJob, entryJob.getNextBuildNumber()));
        final List<Job> downstreamProjects = entryJob.getDownstreamProjects();
        JobWithDetails jobDetail;
        if (downstreamProjects != null && !downstreamProjects.isEmpty()) {
            for (Job subJob : downstreamProjects) {
                jobDetail = subJob.details();
                final int nextBuildNumber = jobDetail.getNextBuildNumber();
                jobRequests.add(new JobRequest(subJob, nextBuildNumber));
            }
        }
        executeJob(entryJob, params);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // 开启线程轮训每个job状态
        Util.startThread(new LookJobTask(jobRequests, countDownLatch));
        countDownLatch.await();

    }

    private Runnable waitConnectRunnable(AtomicInteger atomicInteger) {
        return () -> {
            int i = 1;
            while (atomicInteger.get() == 1) {
                Util.r("wait to connect to jenkins server... %d s ", i++);
                Util.sleepSafely(1000);
            }
        };
    }

    private void executeJob(Job job, Map<String, String> params) throws Exception {
        final QueueReference queueReference = params == null ? job.build() : job.build(params);
        Executable executable;
        QueueItem queueItem;

        int tryCount = 1;
        do {
            Util.r("[%s] wait to execute: %d times...", job.getName(), tryCount++);
            Util.sleepSafely(1000);
            queueItem = jenkinsServer.getQueueItem(queueReference);
            executable = queueItem.getExecutable();
        } while (executable == null && tryCount < MAX_TRY_COUNT);
    }

    private void showAvailableJobs() {
        try {
            Util.showDivider("show available jobs begin");
            final Map<String, Job> jobs = jenkinsServer.getJobs();
            final Set<String> origins = jobs.keySet();
            final String[] ts = origins.toArray(new String[0]);
            final String join = Strings.join(ts, '\n');
            Util.r(join);
            Util.showDivider("show available jobs end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}