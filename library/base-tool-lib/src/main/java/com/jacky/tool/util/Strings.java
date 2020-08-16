package com.jacky.tool.util;

/**
 * Created by Jacky on 2020/8/14
 */
public final class Strings {
    public static <T extends CharSequence> String join(final T[] ts, final char separator) {
        if (ts == null || ts.length == 0) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (final T item : ts) {
            if (!first) {
                builder.append(separator);
            }
            builder.append(item);
            first = false;
        }
        return builder.toString();
    }

    public static boolean isNullOrEmpty(final CharSequence value) {
        return value == null || value.length() == 0;
    }

    public static boolean isNotBlank(CharSequence value) {
        return !isNullOrEmpty(value);
    }


    public static boolean equals(final String s1, final String s2) {
        return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
    }
}
