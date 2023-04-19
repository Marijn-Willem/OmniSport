package com.sportservlet.html;

import com.sports.logic.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class EntityImportFile extends SuperEntityImport {
    protected abstract String getImportDir();

    protected String getOnClick(HttpServletRequest req) {
        return "processImport(" + competitionId + ", " + seasonId + ")";
    }

    protected boolean getNameFilter(String name) {
        return true;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        String fileDir = System.getenv("OMNISPORT_FILELOC") + Util.fileSeparator +
                getServletContext().getInitParameter("fileDir");

        String[] fileList = new File(fileDir + Util.fileSeparator + getImportDir()).list();

        Writer w = res.getWriter();

        w.append("<div>\n<select id=\"fl\">\n");

        if (fileList != null)
            for (String fileName : fileList)
                if (".txt".equals(fileName.substring(fileName.length() - 4))) {
                    String name = fileName.substring(0, fileName.length() - 4);

                    if (getNameFilter(name)) {
                        String line = "<option value=\"" + name + "\">" + name + "</option>\n";
                        w.append(line);
                    }
                }

        w.append("</select>\n</div>\n");

        super.processHtmlBody(stat, req, res);
    }
}
