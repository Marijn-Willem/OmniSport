package com.management.servlet.ajax;

import com.sports.entity.Client;
import com.sports.entity.manager.ClientManager;
import com.sports.entity.manager.IntSuperManager;
import com.sportservlet.ajax.ProcessManageIntEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.ClientFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageClient extends ProcessManageIntEntity<Client> {
    @Override
    protected IntSuperManager<Client> getSuperManager(Statement stat) {
        return new ClientManager(stat);
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "cnid");
    }

    @Override
    protected Client getNewEntity() {
        return new Client();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        Integer lid = convertRequestParamToIdInteger(req, "lid");
        String pwd = req.getParameter("pwd");
        boolean ia = Boolean.parseBoolean(req.getParameter("ia"));

        entity.setName(nm);
        entity.setLanguageId(lid);
        entity.setPassWord(pwd);
        entity.setAdmin(ia);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new ClientFlusher(id);
    }
}
