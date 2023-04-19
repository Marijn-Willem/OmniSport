package com.flush.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class ManageFlushSport extends ManageFlush {
    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadSports();\">\n");
    }

    @Override
    void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"spid\">\n</select><br/>\n");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sport");
    }
}
