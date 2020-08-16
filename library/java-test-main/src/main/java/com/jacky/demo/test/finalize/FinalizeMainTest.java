package com.jacky.demo.test.finalize;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/7/8
 */
public class FinalizeMainTest {

    private void fireTimeout() {
        final GhostObject ghostObject = new GhostObject();
    }

    void start() {
        fireTimeout();
        Runtime.getRuntime().gc();
        System.runFinalization();

        synchronized (this) {
            try {
                Util.i("start wait");
                wait(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Util.i("finish wait");
    }

    public static void main(String[] args) {
        new FinalizeMainTest().start();
    }
}
