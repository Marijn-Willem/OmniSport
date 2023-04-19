package com.sports.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CacheKeyGenerateUtil {
    public static void createCacheKeyFiles(List<List<String>> lineGroups, String superClass)
            throws IOException {
        for (List<String> lineGroup : lineGroups)
            createCacheKeyFile(lineGroup, superClass);
    }

    private static void createCacheKeyFile(List<String> lineGroup, String superClass) throws IOException {
        String className = lineGroup.get(0) + "Key";
        File file = GenerateUtil.createFile("CacheManagement", "cache\\key\\" + className + ".java");

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        GenerateUtil.appendWithNewLine("package com.sports.cache.key;", bw);
        if (lineGroup.size() > 2) {
            bw.newLine();
            GenerateUtil.appendWithNewLine("import com.sports.logic.util.Util;", bw);
        }
        bw.newLine();

        GenerateUtil.appendWithNewLine("public class " + className + " extends " + superClass + " {", bw);
        for (int i = 1; i < lineGroup.size(); i++)
            GenerateUtil.appendWithNewLine("\tprivate final int " + lineGroup.get(i) + ";", bw);
        bw.newLine();

        bw.append("\tpublic ");
        bw.append(className);
        bw.append("(");
        for (int i = 1; i < lineGroup.size(); i++) {
            bw.append("int ");
            bw.append(lineGroup.get(i));
            if (i < lineGroup.size() - 1)
                bw.append(", ");
        }
        GenerateUtil.appendWithNewLine(") {", bw);
        GenerateUtil.appendConstructorAssignments(lineGroup, bw);
        GenerateUtil.appendWithNewLine("\t}", bw);
        bw.newLine();

        GenerateUtil.appendWithNewLine("\t@Override", bw);
        GenerateUtil.appendWithNewLine("\tString getSpecificKeyPart() {", bw);

        if (lineGroup.size() == 2)
            writeOneParameterSpecificKeyPart(lineGroup, bw);
        else if (lineGroup.size() == 3)
            writeTwoParameterSpecificKeyPart(lineGroup, bw);
        else if (lineGroup.size() > 3)
            writeMultiParameterSpecificKeyPart(lineGroup, bw);

        GenerateUtil.appendWithNewLine("\t}", bw);
        GenerateUtil.appendWithNewLine("}", bw);

        bw.flush();
        bw.close();
    }

    private static void writeOneParameterSpecificKeyPart(List<String> lines, BufferedWriter bw) throws IOException {
        GenerateUtil.appendWithNewLine("\t\treturn " + getIntegerToString(lines.get(1)) + ";", bw);
    }

    private static void writeTwoParameterSpecificKeyPart(List<String> lines, BufferedWriter bw) throws IOException {
        GenerateUtil.appendWithNewLine("\t\treturn Util.concatStringsWithDelimiter(" +
                getIntegerToString(lines.get(1)) + ", " + getIntegerToString(lines.get(2)) + ", \"|\");", bw);
    }

    private static void writeMultiParameterSpecificKeyPart(List<String> lines, BufferedWriter bw) throws IOException {
        int whiteSpaces = "return U".length();
        GenerateUtil.appendWithNewLine("\t\treturn Util.concatStrings(new String[] {", bw);
        for (int i = 1; i < lines.size(); i += 2) {
            bw.append("\t\t");
            GenerateUtil.appendWhitespaces(whiteSpaces, bw);
            bw.append(getIntegerToString(lines.get(i)));
            if (i < lines.size() - 1) {
                bw.append(", ");
                bw.append(getIntegerToString(lines.get(i + 1)));
            }
            GenerateUtil.appendWithNewLine(i < lines.size() - 2 ? "," : "", bw);
        }
        GenerateUtil.appendWithNewLine("\t\t}, \"|\");", bw);
    }

    private static String getIntegerToString(String line) {
        return "Integer.toString(" + line + ")";
    }
}
