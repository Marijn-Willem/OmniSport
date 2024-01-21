package com.management.servlet.ajax;

import com.sports.entity.Language;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.LanguageManager;
import com.sportservlet.ajax.ProcessManageIntEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ProcessManageLanguage extends ProcessManageIntEntity<Language> {
    @Override
    protected IntSuperManager<Language> getSuperManager(Statement stat) {
        return new LanguageManager(stat);
    }

    @Override
    protected Language getNewEntity() {
        return new Language();
    }

    @Override
    protected Integer getIdFromRequest(HttpServletRequest req) {
        return convertRequestParamToIdInteger(req, "lid");
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        Integer fbid = convertRequestParamToIdInteger(req, "fbid");

        entity.setName(nm);
        entity.setFallbackLanguageId(fbid);
    }
}
