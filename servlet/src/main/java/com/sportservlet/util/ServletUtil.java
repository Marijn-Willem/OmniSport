package com.sportservlet.util;

import com.sports.logic.util.Util;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class ServletUtil {
    public static void writeOption(int value, String text, Writer w) throws IOException {
        w.append("<option value=\"");
        w.append(Integer.toString(value));
        w.append("\">");
        w.append(text);
        w.append("</option>\n");
    }

    public static void writeLink(String path, String href, String text, Writer w) throws IOException {
        w.append("<a href=\"");
        w.append(path);
        w.append("/");
        w.append(href);
        w.append("\">");
        w.append(text);
        w.append("</a><br/>\n");
    }

    public static void writeGenericGoToButton(String url, String parameters, String text, Writer w) throws IOException {
        w.append("<input type=\"button\" onclick=\"goToUrl('");
        w.append(url);
        w.append("', '");
        w.append(Util.convertNullStringToEmpty(parameters));
        w.append("');\" value=\"");
        w.append(text);
        w.append("\" /><br/>\n");
    }

    public static void writeNoCountResList(Map<Integer, String> noCountResultMap, Writer w) throws IOException {
        w.append("const ncrList = ['");
        w.append(Util.concatStrings(noCountResultMap.values(), "', '"));
        w.append("'];\n");
    }
}
