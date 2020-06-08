package com.jacky.demo.test.string.splite;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/6/4
 */
public class SplitMain {
    public static void main(String[] args) {
        final String s = "5.4.4.24";
        Util.i(s.split(".").length );
        Util.i(s.split("\\.").length );
    }
}
