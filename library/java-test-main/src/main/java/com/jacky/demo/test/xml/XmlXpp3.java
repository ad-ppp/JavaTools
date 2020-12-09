package com.jacky.demo.test.xml;

import com.jacky.tool.util.Util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.builder.XmlNamespace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    /**
     * xmlParser 解析
     * 1. <abc>123<abc/>
     * 通过 parser.getText()得到的内容为： 123
     * 2. <abc android:name="123"></abc>
     * 这种方式通过 parser.getText()得到的内容为空
     */
    private void start(InputStream inputStream) throws Exception {
        final XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        xmlPullParserFactory.setNamespaceAware(true);
        final XmlPullParser parser = xmlPullParserFactory.newPullParser();

        // 写入文件
        testForXmlSerializer(xmlPullParserFactory);

        parser.setInput(inputStream, "utf-8");
        // process
        int type = parser.getEventType();
        do {
            final String namespace = parser.getNamespace();
            final String name = parser.getName();

            Util.i("do:[%s][%s]", namespace, name);
            if (type == XmlPullParser.START_DOCUMENT) {
                Util.i("visit document i[%s]", name);
            } else if (type == XmlPullParser.END_DOCUMENT) {
                Util.i("visit document o[%s]", name);
            } else if (type == XmlPullParser.START_TAG) {

//                if (name.equals("TextView")) {
//                    parser.set("contentDescription", UUID.randomUUID());
//                }

                final StringBuilder sb = new StringBuilder(name);
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    sb
                        .append("[")
                        .append(parser.getAttributeName(i))
                        .append(":")
                        .append(parser.getAttributeValue(i))
                        .append("]");
                }

                Util.i("tag=>%s", sb.append("\n").toString());
            } else if (type == XmlPullParser.END_TAG) {
                Util.i("visit tag,O[%s]", name);
            }
            type = parser.next();
        } while (type != XmlPullParser.END_DOCUMENT);
    }

    private void testForXmlSerializer(XmlPullParserFactory xmlPullParserFactory) throws XmlPullParserException, IOException {
        final XmlSerializer serializer = xmlPullParserFactory.newSerializer();
        final File file = new File("/Users/yangjianfei/myWidget/android/ad-ppp/JavaTools/library/view-node-client/gen",
            "xml-serializer.xml");
        serializer.setOutput(new FileWriter(file));
        serializer.startDocument("utf-8", false);
        serializer.startTag("xmlns:android=\"http://schemas.android.com/apk/res/android", "manifest")
            .attribute("android", "compileSdkVersion", "28");

            serializer.comment("A TextView ");
            serializer.startTag("android", "TextView")
                .attribute("android", "layout_width", "match_parent")
                .attribute("android", "layout_height", "match_parent")
                .endTag("android", "TextView")
        .endTag("xmlns:android=\"http://schemas.android.com/apk/res/android", "manifest");

        serializer.flush();
        serializer.endDocument();
    }

//    private void handleContent(XmlPullParser parser, String tag) {
//        final char[] ch = parser.getTextCharacters(holderForStartAndLength);
//        int start = holderForStartAndLength[0];
//        int length = holderForStartAndLength[1];
//
//        final StringBuilder sb = new StringBuilder();
//        for (int i = start; i < start + length; i++) {
//            sb.append(ch[i]);
////            switch (ch[i]) {
////                case '\\':
////                    System.out.print("\\\\");
////                    break;
////                case '"':
////                    System.out.print("\\\"");
////                    break;
////                case '\n':
////                    System.out.print("\\n");
////                    break;
////                case '\r':
////                    System.out.print("\\r");
////                    break;
////                case '\t':
////                    System.out.print("\\t");
////                    break;
////                default:
////                    System.out.print(ch[i]);
////                    break;
////            }
//        }
//
//        Util.i("visit text:[%d]\t[%d]\t[%s]", start, length, sb.toString());
//    }
}
