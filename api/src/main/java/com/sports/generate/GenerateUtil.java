package com.sports.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GenerateUtil {
    public static List<List<String>> getLineGroups(String templateFile) throws IOException {
        List<String> allLines = Files.readAllLines(getPath(null,
                "generate\\template\\" + templateFile + ".txt"));

        List<List<String>> lineGroups = new ArrayList<>();

        List<String> currentGroup = new ArrayList<>();

        for (String line : allLines)
            if ("".equals(line)) {
                lineGroups.add(currentGroup);
                currentGroup = new ArrayList<>();
            }
            else
                currentGroup.add(line);

        if (!currentGroup.isEmpty())
            lineGroups.add(currentGroup);

        return lineGroups;
    }

    public static File createFile(String module, String path) throws IOException {
        return Files.createFile(getPath(module, path)).toFile();
    }

    public static void appendWithNewLine(String line, BufferedWriter bw) throws IOException {
        bw.append(line);
        bw.newLine();
    }

    public static void appendWhitespaces(int whiteSpaces, Writer w) throws IOException {
        for (int i = 0; i < whiteSpaces; i++)
            w.append(" ");
    }

    public static void appendConstructorAssignments(List<String> lineGroup, BufferedWriter bw) throws IOException {
        for (int i = 1; i < lineGroup.size(); i++) {
            String fieldName = lineGroup.get(i);
            appendWithNewLine("\t\tthis." + fieldName + " = " + fieldName + ";", bw);
        }
    }

    private static Path getPath(String module, String path) {
        String basePath = (module != null ? "..\\" + module + "\\" : "") +
                "src\\main\\java\\com\\sports";
        return FileSystems.getDefault().getPath(basePath + "\\" + path);
    }
}
