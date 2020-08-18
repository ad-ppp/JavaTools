package com.jacky.tool.jenkins;

import com.beust.jcommander.Parameter;

/**
 * Created by Jacky on 2020/8/18
 * 本地配置
 */
public class LocalConfig {
    public final static String PARAMETER_USERNAME = "-u";
    public final static String PARAMETER_TOKEN = "-t";
    public final static String PARAMETER_URL = "--url";

    @Parameter(names = {"--user", PARAMETER_USERNAME}, description = "USERNAME")
    public String user;

    @Parameter(names = {"--token", PARAMETER_TOKEN}, description = "private token to access jenkins server")
    public String token;

    @Parameter(names = {PARAMETER_URL}, description = "jenkins url")
    public String url;

    @Override
    public String toString() {
        return "LocalConfig{" +
            "user='" + user + '\'' +
            ", token='" + token + '\'' +
            ", url='" + url + '\'' +
            '}';
    }
}
