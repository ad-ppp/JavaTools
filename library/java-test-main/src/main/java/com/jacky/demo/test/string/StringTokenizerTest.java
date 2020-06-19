package com.jacky.demo.test.string;

import com.jacky.tool.util.Util;

import java.util.StringTokenizer;

/**
 * Created by Jacky on 2020/6/19
 */
public class StringTokenizerTest {
    private static final String sTokenizer = "1;2;3;hello world;";

    private void start() {
        Util.i("StringTokenizer \tclassLoader:%s", StringTokenizer.class.getClassLoader());
        Util.i("String \t\t\tclassLoader:%s", String.class.getClassLoader());

        final StringTokenizer stringTokenizer = new StringTokenizer(sTokenizer, ";");
        final int countTokens = stringTokenizer.countTokens();
        for (int i = 0; i < countTokens; i++) {
            Util.i("find token:%s", stringTokenizer.nextToken());
        }
    }

    public static void main(String[] args) {
        new StringTokenizerTest().start();
    }
}
