package com.sports.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutputDataGenerator {
    public static void main(String[] args) throws IOException {
        List<List<String>> lineGroups = GenerateUtil.getLineGroups("OutputDataTemplate");
        List<List<String>> lineGroupsExpanded = lineGroups.stream().map(x -> new ArrayList<String>() {{
            addAll(x);
            add("clientId");
        }}).collect(Collectors.toList());
        CacheKeyGenerateUtil.createCacheKeyFiles(lineGroupsExpanded, "CacheDataKey");

        for (List<String> lineGroup : lineGroups) {
            createOutputData(lineGroup);
            createRestXml(lineGroup);
            createRestJson(lineGroup);
            createRestYaml(lineGroup);
        }
    }

    private static void createRestXml(List<String> lineGroup) throws IOException {
        createRestFile(lineGroup, "xml", "APPLICATION_XML");
    }

    private static void createRestJson(List<String> lineGroup) throws IOException {
        createRestFile(lineGroup, "json", "APPLICATION_JSON + \";charset=\\\"UTF-8\\\"\"");
    }

    private static void createRestYaml(List<String> lineGroup) throws IOException {
        createRestFile(lineGroup, "yaml", "TEXT_PLAIN + \";charset=\\\"UTF-8\\\"\"");
    }

    private static void createOutputData(List<String> lineGroup) throws IOException {
        String className = lineGroup.get(0) + "Data";
        String keyName = lineGroup.get(0) + "Key";
        File file = GenerateUtil.createFile("api", "cache\\data\\" + className + ".java");

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        GenerateUtil.appendWithNewLine("package com.sports.cache.data;", bw);
        bw.newLine();
        GenerateUtil.appendWithNewLine("import com.sports.cache.key.CacheDataKey;", bw);
        GenerateUtil.appendWithNewLine("import com.sports.cache.key." + keyName + ";", bw);
        bw.newLine();
        GenerateUtil.appendWithNewLine("import java.sql.SQLException;", bw);
        GenerateUtil.appendWithNewLine("import java.sql.Statement;", bw);
        bw.newLine();

        GenerateUtil.appendWithNewLine("public class " + className + " extends OutputData {", bw);
        for (int i = 1; i < lineGroup.size(); i++)
            GenerateUtil.appendWithNewLine("\tprivate final int " + lineGroup.get(i) + ";", bw);
        GenerateUtil.appendWithNewLine("\tprivate final Integer clientId;", bw);
        bw.newLine();

        bw.append("\tpublic ");
        bw.append(className);
        bw.append("(");
        for (int i = 1; i < lineGroup.size(); i++) {
            bw.append("int ");
            bw.append(lineGroup.get(i));
            bw.append(", ");
        }
        GenerateUtil.appendWithNewLine("Integer clientId) {", bw);
        GenerateUtil.appendConstructorAssignments(lineGroup, bw);
        GenerateUtil.appendWithNewLine("\t\tthis.clientId = clientId;", bw);
        GenerateUtil.appendWithNewLine("\t}", bw);
        bw.newLine();

        GenerateUtil.appendWithNewLine("\t@Override", bw);
        GenerateUtil.appendWithNewLine("\tpublic CacheDataKey getCacheKey() {", bw);
        bw.append("\t\treturn new ");
        bw.append(keyName);
        bw.append("(");
        for (int i = 1; i < lineGroup.size(); i++) {
            bw.append(lineGroup.get(i));
            bw.append(", ");
        }
        GenerateUtil.appendWithNewLine("clientId);", bw);
        GenerateUtil.appendWithNewLine("\t}", bw);
        bw.newLine();

        GenerateUtil.appendWithNewLine("\t@Override", bw);
        GenerateUtil.appendWithNewLine("\tpublic void fill(Statement stat) throws SQLException {", bw);
        bw.newLine();
        GenerateUtil.appendWithNewLine("\t}", bw);
        bw.newLine();

        GenerateUtil.appendWithNewLine("\t@Override", bw);
        GenerateUtil.appendWithNewLine("\tpublic boolean isValidOutput() {", bw);
        GenerateUtil.appendWithNewLine("\t\treturn true;", bw);
        GenerateUtil.appendWithNewLine("\t}", bw);
        bw.newLine();

        writeOutputMethod("toXML", bw);
        bw.newLine();
        writeOutputMethod("toJson", bw);
        bw.newLine();
        writeOutputMethod("toYaml", bw);
        GenerateUtil.appendWithNewLine("}", bw);

        bw.flush();
        bw.close();
    }

    private static void createRestFile(List<String> lineGroup, String dir, String mediaType)
            throws IOException {
        String outputName = lineGroup.get(0);

        File file = GenerateUtil.createFile("api", "rest\\" + dir + "\\" + outputName + ".java");

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        GenerateUtil.appendWithNewLine("package com.sports.rest." + dir + ";", bw);
        bw.newLine();
        GenerateUtil.appendWithNewLine("import com.sports.cache.data." + outputName + "Data;", bw);
        GenerateUtil.appendWithNewLine("import com.sports.entity.key.CompSeasonKey;", bw);
        GenerateUtil.appendWithNewLine("import com.sports.rest.RestClientUtil;", bw);
        bw.newLine();
        GenerateUtil.appendWithNewLine("import jakarta.servlet.http.HttpServletResponse;", bw);
        GenerateUtil.appendWithNewLine("import jakarta.ws.rs.GET;", bw);
        GenerateUtil.appendWithNewLine("import jakarta.ws.rs.Path;", bw);
        GenerateUtil.appendWithNewLine("import jakarta.ws.rs.PathParam;", bw);
        GenerateUtil.appendWithNewLine("import jakarta.ws.rs.Produces;", bw);
        GenerateUtil.appendWithNewLine("import jakarta.ws.rs.core.Context;", bw);
        GenerateUtil.appendWithNewLine("import jakarta.ws.rs.core.MediaType;", bw);
        bw.newLine();
        GenerateUtil.appendWithNewLine("import java.io.IOException;", bw);
        bw.newLine();
        bw.append("@Path(\"");
        bw.append(dir);
        bw.append("/");
        bw.append(lineGroup.get(0).toLowerCase());

        for (int i = 1; i < lineGroup.size(); i++) {
            bw.append("/{");
            bw.append(lineGroup.get(i));
            bw.append("}");
        }

        GenerateUtil.appendWithNewLine("/{clientName}/{password}\")", bw);
        GenerateUtil.appendWithNewLine("public class " + outputName + " {", bw);
        GenerateUtil.appendWithNewLine("\t@GET", bw);
        GenerateUtil.appendWithNewLine("\t@Produces(MediaType." + mediaType + ")", bw);

        String declaration = "\tpublic String get" + outputName + "(";
        bw.append(declaration);

        if (lineGroup.size() > 1)
            writeParameter(0, lineGroup.get(1), bw);

        int whiteSpaces = declaration.length() - 1;

        for (int i = 2; i < lineGroup.size(); i++)
            writeParameter(whiteSpaces, lineGroup.get(i), bw);

        int whiteSpaces1 = lineGroup.size() == 1 ? 0 : whiteSpaces;
        writeAdditionalParameters(whiteSpaces1, whiteSpaces, bw);

        String dirWithCapital = dir.substring(0, 1).toUpperCase() + dir.substring(1);

        GenerateUtil.appendWithNewLine("\t\tCompSeasonKey compSeasonKey = " +
                "new CompSeasonKey(competitionId, seasonId);", bw);
        GenerateUtil.appendWithNewLine("\t\tInteger clientId = " +
                "RestClientUtil.getValidatedClientId(clientName, password);", bw);
        bw.newLine();
        GenerateUtil.appendWithNewLine("\t\treturn new " + dirWithCapital +
                "OutputCreator(clientId, response, compSeasonKey).createOutput(new " + outputName + "Data(", bw);
        bw.append("\t\t");
        GenerateUtil.appendWhitespaces("return n".length(), bw);
        for (int i = 1; i < lineGroup.size(); i++) {
            bw.append(lineGroup.get(i));
            bw.append(", ");
        }
        GenerateUtil.appendWithNewLine("clientId));", bw);

        GenerateUtil.appendWithNewLine("\t}", bw);
        GenerateUtil.appendWithNewLine("}", bw);

        bw.flush();
        bw.close();
    }

    private static void writeOutputMethod(String methodName, BufferedWriter bw) throws IOException {
        GenerateUtil.appendWithNewLine("\t@Override", bw);
        bw.append("\tpublic String ");
        bw.append(methodName);
        GenerateUtil.appendWithNewLine("() {", bw);
        GenerateUtil.appendWithNewLine("\t\treturn null;", bw);
        GenerateUtil.appendWithNewLine("\t}", bw);
    }

    private static void writeParameter(int whiteSpaces, String parameter, BufferedWriter bw) throws IOException {
        GenerateUtil.appendWhitespaces(whiteSpaces, bw);
        bw.append("@PathParam(\"");
        bw.append(parameter);
        bw.append("\") int ");
        bw.append(parameter);
        GenerateUtil.appendWithNewLine(",", bw);
        bw.append("\t");
    }

    private static void writeAdditionalParameters(int whiteSpaces1, int whiteSpaces2, BufferedWriter bw)
            throws IOException {
        GenerateUtil.appendWhitespaces(whiteSpaces1, bw);
        GenerateUtil.appendWithNewLine("@PathParam(\"clientName\") String clientName,", bw);
        bw.append("\t");
        GenerateUtil.appendWhitespaces(whiteSpaces2, bw);
        GenerateUtil.appendWithNewLine("@PathParam(\"password\") String password,", bw);
        bw.append("\t");
        GenerateUtil.appendWhitespaces(whiteSpaces2, bw);
        GenerateUtil.appendWithNewLine("@Context HttpServletResponse response) throws IOException {", bw);
    }
}
