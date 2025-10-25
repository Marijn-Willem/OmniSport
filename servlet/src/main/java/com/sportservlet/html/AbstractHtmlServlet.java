package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;

public interface AbstractHtmlServlet {
    void initSpecificProperties(HttpServletRequest req);
}
