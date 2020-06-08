package com.jacky.tool.apk.main;

import com.beust.jcommander.Parameter;
import com.jacky.tool.base.BaseJModel;

/**
 * Created by Jacky on 2020/5/28
 */
public class Arguments extends BaseJModel {
    @Parameter(names = {"--arsc"}, description = "指定arsc文件路径")
    public String arsc;


}
