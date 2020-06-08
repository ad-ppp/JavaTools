package com.jacky.tool.base;

import com.beust.jcommander.Parameter;

import java.util.Locale;

/**
 * Created by Jacky on 2020/5/28
 */
public class BaseJModel {
    @Parameter(names = {"-h", "-?", "--help"}, help = true, description = "Show usage information")
    public boolean help;
}
