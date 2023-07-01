package com.management.servlet.html;

import com.sports.entity.Equipe;
import com.sports.entity.manager.EquipeManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageEquipe extends ManageEntity {
    private Equipe equipe;

    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "EquipePortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("equipe");
        jsSpecificList.add("equipe");
        cssList.add("styling");
    }

    @Override
    protected String getEntityIdName() {
        return null;
    }

    @Override
    protected void preProcessSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException {
        if (!"i".equals(mode)) {
            String nm = req.getParameter("nm");
            equipe = new EquipeManager(stat).getEquipeByName(nm);
        }
        else
            equipe = null;
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (equipe != null || "i".equals(mode)) {
            Writer w = res.getWriter();

            writeTextFieldWithLabel("Name", "nm", equipe != null ? equipe.getName() : null, w);
        }
        else
            dispatchToReturnPath(req, res);
    }

    @Override
    protected String getUpdateId(HttpServletRequest req) {
        return equipe != null ? Integer.toString(equipe.getId()) : "";
    }
}
