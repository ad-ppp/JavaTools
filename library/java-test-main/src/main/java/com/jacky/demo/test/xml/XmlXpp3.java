package com.jacky.demo.test.xml;

import com.jacky.tool.util.Util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.Objects;

/**
 * Created by Jacky on 2020/5/29
 * <p>
 * xpp3 解析
 */
public class XmlXpp3 {
    private final int[] holderForStartAndLength = new int[2];

    public static void main(String[] args) {
        final InputStream resourceAsStream =
            Objects.requireNonNull(XmlXpp3.class.getClassLoader())
                .getResourceAsStream("AndroidManifest.xml");

        try {
            new XmlXpp3().start(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start(InputStream inputStream) throws Exception {
        final XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        xmlPullParserFactory.setNamespaceAware(true);
        final XmlPullParser parser = xmlPullParserFactory.newPullParser();
        parser.setInput(inputStream, "utf-8");

        // process
        int eventType = parser.getEventType();
        do {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                Util.i("visit document i[%s]\t[%s]", parser.getName(), parser.getNamespace());
            } else if (eventType == XmlPullParser.END_DOCUMENT) {
                Util.i("visit document o[%s]", parser.getName());
            } else if (eventType == XmlPullParser.START_TAG) {
                Util.i("visit tag,I[%s]", parser.getName());
            } else if (eventType == XmlPullParser.END_TAG) {
                Util.i("visit tag,O[%s]", parser.getName());
            } else if (eventType == XmlPullParser.TEXT) {
                handleContent(parser);
            }
            eventType = parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
    }

    private void handleContent(XmlPullParser parser) {
        final char[] ch = parser.getTextCharacters(holderForStartAndLength);
        int start = holderForStartAndLength[0];
        int length = holderForStartAndLength[1];

        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + length; i++) {
            sb.append(ch[i]);
//            switch (ch[i]) {
//                case '\\':
//                    System.out.print("\\\\");
//                    break;
//                case '"':
//                    System.out.print("\\\"");
//                    break;
//                case '\n':
//                    System.out.print("\\n");
//                    break;
//                case '\r':
//                    System.out.print("\\r");
//                    break;
//                case '\t':
//                    System.out.print("\\t");
//                    break;
//                default:
//                    System.out.print(ch[i]);
//                    break;
//            }
        }

        Util.i("visit text:[%d]\t[%d]\t[%s]", start, length, sb.toString());
    }
}
