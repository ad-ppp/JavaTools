package com.jacky.demo.test.pattern;

import com.jacky.tool.util.Util;

/**
 * Created by Jacky on 2020/6/3
 */
public class PatternMain {
    public static void main(String[] args) {
        final PatternMain patternMain = new PatternMain();
//        patternMain.testNumber();
        patternMain.testFace();
    }

    private void testFace() {
        final String regex = "R.drawable.face_[0-9]+$";
        Util.i("R.drawable.face_001".matches(regex));
        Util.i("R.drawable.face_001l".matches(regex));
        Util.i("R.drawable.face_1p".matches(regex));
        Util.i("R.drawable.face_2p".matches(regex));
        Util.i("R.drawable.face_3p".matches(regex));
        Util.i("12".matches(regex));
    }

    private void testNumber() {
        final String regex = "[0-9]+$";
        Util.i("003".matches(regex));
        Util.i("3".matches(regex));
        Util.i("123".matches(regex));
        Util.i("10p".matches(regex));
        Util.i("10p".matches(regex));
    }
}
