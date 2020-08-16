package com.jacky.tool.jenkins;

import com.jacky.tool.jenkins.model.JobRequest;
import com.jacky.tool.jenkins.model.JobResponse;
import com.jacky.tool.jenkins.model.JobStatus;
import com.jacky.tool.util.Util;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Jacky on 2020/8/15
 */
public final class LookJobTask implements Runnable {
    final List<JobResponse> jobResponses;
    final List<JobResponse> copy;
    final CountDownLatch countDownLatch;
    private int index = 0;

    private JobResponse lastJobResponse;

    public LookJobTask(List<JobRequest> jobRequests, CountDownLatch countDownLatch) {
        this.jobResponses = Util.convertList(jobRequests, JobResponse::new);
        this.copy = new ArrayList<>(jobResponses);
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        while (!copy.isEmpty()) {
            final JobResponse jobResponse = copy.get(index++ % (copy.size()));
            final JobStatus jobStatus = jobResponse.getJobStatus();
            if (jobStatus == JobStatus.error) {
                throw new IllegalStateException();
            }

            if (jobStatus == JobStatus.init || jobStatus == JobStatus.onGoing) {
                try {
                    look(jobResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    jobResponse.setJobStatus(JobStatus.error);
                    copy.remove(jobResponse);
                }
            }
            Util.sleepSafely(JenkinsVisitor.LOOK_JOB_DETAIL_INTERVAL);
        }

        Util.r("---------------------- DUMP JOB STATUS ----------------------");
        for (JobResponse response : jobResponses) {
            Util.r(response.dump());
        }

        countDownLatch.countDown();
    }

    void look(JobResponse jobResponse) throws Exception {
        final JobRequest jobRequest = jobResponse.getJobRequest();
        final Job job = jobRequest.getJob();

        JobWithDetails details = job.details();
        Build build = details.getBuildByNumber(jobRequest.getNextBuildId());
        if (build != null) {
            if (jobResponse.getJobStatus() != JobStatus.onGoing) {
                jobResponse.setJobStatus(JobStatus.onGoing);
            }
            dumpBuild(jobResponse, build);
        }
    }

    private void dumpBuild(JobResponse jobResponse, Build build) throws Exception {
        BuildWithDetails details = build.details();

        final Map<String, String> parameters = details.getParameters();
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sb.append("[")
                .append(entry.getKey())
                .append(":")
                .append(entry.getValue())
                .append("]");
        }
        final Job job = jobResponse.getJobRequest().getJob();
        if (!jobResponse.isHasPrintParams()) {
            jobResponse.setHasPrintParams(true);
            Util.r("%s BUILD PARAMETERS: %s", job.getName(), sb.toString());
        }

        int showLength = jobResponse.getShowLength();
        if (details.isBuilding()) {
            details = build.details();
            final String consoleOutputText = details.getConsoleOutputText();
            final int length = consoleOutputText.length();
            if (length > showLength) {
                if (lastJobResponse != jobResponse) {
                    Util.r("\n-------------------------- [%s] --------------------------\n", job.getName());
                    lastJobResponse = jobResponse;
                }

                Util.r(consoleOutputText.substring(showLength, length));
                jobResponse.setShowLength(length);
            }
        } else {
            if (jobResponse.getJobStatus() != JobStatus.success) {
                jobResponse.setJobStatus(JobStatus.success);
                copy.remove(jobResponse);
            }
        }
    }
}
