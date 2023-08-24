package com.alias.servlet.dispatch;

import com.sports.entity.Equipe;
import com.sports.entity.manager.EquipeManager;
import com.sportservlet.dispatch.SuperDispatchServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class PrepareEquipeForEntityInstancePortal extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        String nm = req.getParameter("nm");
        Equipe equipe = new EquipeManager(stat).getEquipeByName(nm);

        if (equipe != null) {
            req.setAttribute("eid", equipe.getId());
            dispatchURL = "EntityInstancePortal";
        }
        else
            dispatchURL = "EquipePortal";
    }
}
