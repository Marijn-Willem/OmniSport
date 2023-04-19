package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public abstract class ManageTeams extends TeamSelectServlet {
    @Override
    protected void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<input type=\"button\" id=\"inp_sel\" value=\"Add to selection\" onclick=\"addToSelection();\" /><br/>\n");
        w.append("<input type=\"button\" id=\"inp_mem\" value=\"Add to memory\" onclick=\"addToMemory();\" /><br/>\n");
        w.append("<table id=\"tbl_sel\" border=\"1\">\n</table>");
        w.append("<div id=\"div_mem\"></div>\n");
    }

    @Override
    protected void initAbstractProperties() {
        jsList.add("name");
    }
}
