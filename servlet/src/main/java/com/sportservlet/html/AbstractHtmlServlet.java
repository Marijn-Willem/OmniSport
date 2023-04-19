package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public interface AbstractHtmlServlet {
    void initSpecificProperties(HttpServletRequest req);
}
