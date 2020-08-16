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
        "{\"name\":\"123\",\"params\":[{\"key\":\"123\",\"value\":\"222\"}],\"showAvailableJobs\":false}";

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
        final Config config = JSON.parseObject(json, Config.class);
        if (config == null) {
            throw new IllegalStateException("json parser exception");
        }

        final List<String> arguments = new ArrayList<>();
        if (Strings.isNotBlank(config.getName())) {
            arguments.add(RequestModule.PARAMETER_NAME);
            arguments.add(config.getName());
        }

        final List<Config.ParamsBean> params = config.getParams();
        if (params != null && !params.isEmpty()) {
            for (Config.ParamsBean param : params) {
                arguments.add(String.format("-D%s=%s", param.getKey(), param.getValue()));
            }
        }

        if (config.isShowAvailableJobs()) {
            arguments.add(RequestModule.PARAMETER_SHOW_AVAILABLE_JOBS);
        }

        return arguments.toArray(new String[1]);
    }


}
