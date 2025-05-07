package com.sportservlet.html;

import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class SuperHtmlServlet extends SuperResponseServlet {
    private String returnPath;

    protected List<String> jsList;
    protected List<String> jsSpecificList;
    protected List<String> cssList;

    protected abstract void initProperties(HttpServletRequest req);
    protected abstract void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException;

    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body>\n");
    }

    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {}

    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException { return null; }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        jsList = new ArrayList<>() {{
            add("base");
        }};
        jsSpecificList = new ArrayList<>();
        cssList = new ArrayList<>();
        returnPath = getReturnPath(stat, req);

        initProperties(req);

        Writer w = resp.getWriter();
        w.append("<html>\n<head>\n");

        String line;

        for (String js : jsList) {
            line = "<script type=\"text/javascript\" src=\"" + resourcePath + "/js/" + js + ".js\"></script>\n";
            w.append(line);
        }

        for (String js : jsSpecificList) {
            line = "<script type=\"text/javascript\" src=\"" + resourcePath + "/js/specific/" + js + ".js\"></script>\n";
            w.append(line);
        }

        line = "<script type=\"text/javascript\">\nconst path = '" + path + "';\n";
        w.append(line);
        if (returnPath != null) {
            line = "let returnPath = '" + returnPath + "';\n";
            w.append(line);
        }
        processScriptTag(stat, req, w);
        w.append("</script>\n");

        for (String css : cssList) {
            line = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + resourcePath + "/css/" + css + ".css\" />\n";
            w.append(line);
        }

        line = "</head>\n";
        w.append(line);

        writeBodyTag(w);
        processHtmlBody(stat, req, resp);

        if (returnPath != null) {
            line = "<input type=\"button\" onclick=\"goToUrl(returnPath);\" value=\"Return\" />\n";
            w.append(line);
        }

        w.append("</body>\n</html>\n");
    }

    protected void dispatchToReturnPath(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            req.getRequestDispatcher(returnPath).forward(req, res);
        }
        catch (ServletException se) {
            se.printStackTrace();
        }
    }

    protected void writeHiddenFieldsCompSeason(Writer w) throws IOException {
        writeHiddenField(w, "cid", competitionId);
        writeHiddenField(w, "sid", seasonId);
    }

    protected void writeHiddenField(Writer w, String fieldName, int fieldValue) throws IOException {
        String line = "<input type=\"hidden\" name=\"" + fieldName + "\" value=\"" + fieldValue + "\" />\n";
        w.append(line);
    }

    protected void writeCompSeasonVarsInScriptTag(Writer w) throws IOException {
        writeVarInScriptTag("cid", competitionId, w);
        writeVarInScriptTag("sid", seasonId, w);
    }

    protected void writeCompSeasonPhaseVarsInScriptTag(HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        int phaseId = getIntValuedParameterValue(req, "pid");
        writeVarInScriptTag("pid", phaseId, w);
    }

    protected void writeCompSeasonEventVarsInScriptTag(HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        int compSeasonEventId = getIntValuedParameterValue(req, "cseid");
        writeVarInScriptTag("cseid", compSeasonEventId, w);
    }

    protected void writeCompSeasonEventPartVarsInScriptTag(HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonEventVarsInScriptTag(req, w);
        int compSeasonEventPartId = getIntValuedParameterValue(req, "csepid");
        writeVarInScriptTag("csepid", compSeasonEventPartId, w);
    }

    protected void writeVarInScriptTag(String name, int value, Writer w) throws IOException {
        writeVarNameAndValue(name, value, w);
    }

    protected void writeInitStateVarInScriptTag(String name, HttpServletRequest req, Writer w) throws IOException {
        String valAsString = req.getParameter(name);
        writeVarNameAndValue(name, Util.convertStringToNegativeInteger(valAsString), w);
    }

    protected void writeLink(String href, String text, Writer w) throws IOException {
        w.append("<a href=\"");
        w.append(path);
        w.append("/");
        w.append(href);
        w.append("\">");
        w.append(text);
        w.append("</a><br/>\n");
    }

    private void writeVarNameAndValue(String name, Integer value, Writer w) throws IOException {
        w.append("const ");
        w.append(name);
        w.append(" = ");
        w.append(Util.convertEmptyInteger(value, "null"));
        w.append(";\n");
    }
}
