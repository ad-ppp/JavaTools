package com.jacky.tool.jenkins.model;

import com.offbytwo.jenkins.model.Job;

/**
 * Created by Jacky on 2020/8/15
 */
public  class JobRequest {
    private Job job;
    private int nextBuildId;

    public JobRequest(Job job, int nextBuildId) {
        this.job = job;
        this.nextBuildId = nextBuildId;
    }

    public Job getJob() {
        return job;
    }

    public int getNextBuildId() {
        return nextBuildId;
    }
}
