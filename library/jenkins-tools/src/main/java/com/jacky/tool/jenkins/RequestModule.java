package com.jacky.tool.jenkins;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.jacky.tool.base.BaseJModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacky on 2020/8/14
 */

@Parameters(commandDescription = "create by xhb-jacky", commandNames = {"Jenkins helper"})
public class RequestModule extends BaseJModel {
    public static final String PARAMETER_SHOW_AVAILABLE_JOBS = "-a";
    public static final String PARAMETER_CONFIG = "-c";
    public static final String PARAMETER_D = "-D";
    public static final String PARAMETER_NAME = "-n";

    @Parameter(names = {"--all", PARAMETER_SHOW_AVAILABLE_JOBS}, description = "show all available jobs", help = true)
    public boolean showAvailableJobs;

    @Parameter(names = {"--config", PARAMETER_CONFIG}, description = "Config of file , must be the type of json " +
        ", like: " + ConfigParser.PARSER_RULE, order = 2)
    public String config;

    @DynamicParameter(names = {PARAMETER_D}, description = "Dynamic parameters go here. ")
    public Map<String, String> params = new HashMap<>();

    @Parameter(names = {"--name", PARAMETER_NAME}, description = "The name of job you build")
    public String jobName;

    @ParametersDelegate
    public LocalConfig delegate = new LocalConfig();

    @Override
    public String toString() {
        return "RequestModule{" +
            "showAvailableJobs=" + showAvailableJobs +
            ", config='" + config + '\'' +
            ", params=" + params +
            ", jobName='" + jobName + '\'' +
            ", delegate=" + delegate +
            ", help=" + help +
            '}';
    }
}
