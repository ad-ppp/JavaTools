package com.jacky.tool.jenkins;

import com.alibaba.fastjson.JSON;
import com.jacky.tool.util.FileUtils;
import com.jacky.tool.util.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2020/8/15
 */
public class ConfigParser {

    public static final String PARSER_RULE =
        "{\"name\":\"android.test.blackboard.release.multijob\",\"params\":[{\"key\":\"x_branch\",\"value\":\"v566\"},{\"key\":\"m_branch\",\"value\":\"v566\"}],\"local\":{\"user\":\"user\",\"token\":\"token\",\"url\":\"jenkins_url\"},\"showAvailableJobs\":false}";

    /**
     * @param configPath
     * @return
     */
    public static String[] parse(String configPath) {
        final File file = new File(configPath);
        if (!file.exists()) {
            final String format = String.format("the config file:%s is not exists", configPath);
            throw new IllegalArgumentException(format);
        }

        final String json = FileUtils.readFile(configPath);
        final Configuration configuration = JSON.parseObject(json, Configuration.class);
        if (configuration == null) {
            throw new IllegalStateException("json parser exception");
        }

        final List<String> arguments = new ArrayList<>();
        if (Strings.isNotBlank(configuration.getName())) {
            arguments.add(RequestModule.PARAMETER_NAME);
            arguments.add(configuration.getName());
        }

        final List<Configuration.ParamsBean> params = configuration.getParams();
        if (params != null && !params.isEmpty()) {
            for (Configuration.ParamsBean param : params) {
                arguments.add(String.format("-D%s=%s", param.getKey(), param.getValue()));
            }
        }

        if (configuration.isShowAvailableJobs()) {
            arguments.add(RequestModule.PARAMETER_SHOW_AVAILABLE_JOBS);
        }

        final Configuration.LocalBean local = configuration.getLocal();
        if (Strings.isNotBlank(local.getUser())) {
            arguments.add(LocalConfig.PARAMETER_USERNAME);
            arguments.add(local.getUser());
        }

        if (Strings.isNotBlank(local.getToken())) {
            arguments.add(LocalConfig.PARAMETER_TOKEN);
            arguments.add(local.getToken());
        }

        if (Strings.isNotBlank(local.getUrl())) {
            arguments.add(LocalConfig.PARAMETER_URL);
            arguments.add(local.getUrl());
        }

        return arguments.toArray(new String[1]);
    }


}
