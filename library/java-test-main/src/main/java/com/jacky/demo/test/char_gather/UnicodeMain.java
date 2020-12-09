package com.jacky.demo.test.char_gather;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/8/27
 */
public class UnicodeMain {
    public static void main(String[] args) {
        final String s = "ǐǎňèňīǚǜ";
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);

            Util.i("%s:%d", c, (int) c);

        }

    }
}
