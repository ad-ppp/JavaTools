package com.jacky.tool.jenkins.model;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/8/15
 */
public class JobResponse {
    private final JobRequest jobRequest;
    private JobStatus jobStatus = JobStatus.init;
    private long startTime;
    private int costTime = -1;
    private int showLength = 0;
    private boolean hasPrintParams;

    public boolean isHasPrintParams() {
        return hasPrintParams;
    }

    public void setHasPrintParams(boolean hasPrintParams) {
        this.hasPrintParams = hasPrintParams;
    }

    public int getShowLength() {
        return showLength;
    }

    public void setShowLength(int showLength) {
        this.showLength = showLength;
    }

    public JobResponse(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    public JobRequest getJobRequest() {
        return jobRequest;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        Util.checkNoNull(jobStatus);

        if (jobStatus == JobStatus.onGoing) {
            startTime = System.currentTimeMillis();
        }

        if (jobStatus == JobStatus.success || jobStatus == JobStatus.error) {
            costTime = (int) (System.currentTimeMillis() - startTime);
        }
        this.jobStatus = jobStatus;
    }

    public String dump() {
        return "{job:" + jobRequest.getJob().getName() +
            ", jobStatus=" + jobStatus +
            ", startTime=" + startTime +
            ", costTime=" + costTime +
            '}';
    }
}
