package com.jacky.tool.base;

import com.beust.jcommander.Parameter;

/**
 * Created by Jacky on 2020/5/28
 */
public class BaseJModel {
    @Parameter(names = {"-h", "-?", "--help"}, help = true, description = "How to use.", order = 1)
    public boolean help;
}
