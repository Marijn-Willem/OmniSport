package com.management.servlet.ajax;

import com.sports.entity.Equipe;
import com.sports.entity.manager.EquipeManager;
import com.sports.entity.manager.IntSuperManager;
import com.sportservlet.ajax.ProcessManageIntEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageEquipe extends ProcessManageIntEntity<Equipe> {
    @Override
    protected IntSuperManager<Equipe> getSuperManager(Statement stat) {
        return new EquipeManager(stat);
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "eqid");
    }

    @Override
    protected Equipe getNewEntity() {
        return new Equipe();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");

        entity.setName(nm);
    }
}
