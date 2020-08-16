package com.jacky.demo.test.finalize;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/7/8
 */
public class GhostObject {

    @Override
    protected void finalize() throws Throwable {
        Util.i("start finalize");
        super.finalize();
        Util.sleepSafely(80000);
        Util.i("end finalize");
    }
}
