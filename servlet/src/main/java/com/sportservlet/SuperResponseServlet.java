package com.sportservlet;

import com.sports.entity.manager.NoCountResultManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public abstract class SuperResponseServlet extends SuperServlet {
    protected String resourcePath;
    protected String path;

    private Map<Integer, String> noCountResultMap;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resourcePath = req.getContextPath();
        path = resourcePath + "/servlet";
        noCountResultMap = null;

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().append("<!DOCTYPE html>\n");

        super.doGet(req, resp);

        resp.getWriter().flush();
    }

    protected Map<Integer, String> getNoCountResultMap(Statement stat) throws SQLException {
        if (noCountResultMap == null)
            noCountResultMap = new NoCountResultManager(stat).getIdNameMap();

        return noCountResultMap;
    }
}
