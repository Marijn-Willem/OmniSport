package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public abstract class CopyCompSeason extends NonLinkedSeasonList {
    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    protected void initAbstractProperties() {
        jsList.add("copycompseason");
    }

    protected String getOnClick() {
        return "copyCompSeasonLoader.loadElement();";
    }

    protected String getButtonValue() {
        return "Copy CompSeason";
    }
}
