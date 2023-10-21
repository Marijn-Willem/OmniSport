package com.flush.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class ManageFlushEntityInstanceNonCompSeason extends ManageFlush {
    void writeSpecificFields(Writer w) throws IOException { }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"init();\">\n");
    }

    @Override
    void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        writeSpecificFields(w);

        w.append("<select id=\"en\" onchange=\"setVisibility();\">\n");
        writeOption("Club", w);
        writeOption("Equipe", w);
        writeOption("Geo", w);
        writeOption("Noc", w);
        writeOption("Person", w);
        w.append("</select><br/>\n");

        w.append("<input id=\"nm\" type=\"text\" onkeypress=\"loadTableName();\" /><br/>\n");
        w.append("<table id=\"tblNm\" border=\"1\"></table>\n");
        w.append("<input id=\"gn\" type=\"text\" onkeypress=\"handleChangeGn();\" /><br/>\n");
        w.append("<table id=\"tblGn\" border=\"1\"></table>\n");
        w.append("<select id=\"nid\"></select><br/>\n");
        w.append("<input id=\"inp_pls\" type=\"text\" onkeypress=\"personSearchListLoader.loadElement();\" /><br/>\n");
        w.append("<table id=\"tbl_pls\" border=\"1\"></table>\n");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("person");
        jsList.add("geo");
        jsList.add("club");
        jsList.add("equipe");
        jsSpecificList.add("entityinstance");
    }

    private void writeOption(String entityName, Writer w) throws IOException {
        w.append("<option value=\"");
        w.append(entityName);
        w.append("\">");
        w.append(entityName);
        w.append("</option>\n");
    }
}
