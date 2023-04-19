package com.management.servlet.ajax;

import com.sports.entity.Season;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.SeasonManager;
import com.sportservlet.ajax.ProcessManageIntEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ProcessManageSeason extends ProcessManageIntEntity<Season> {
    protected Season getNewEntity() {
        return new Season();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String nm = req.getParameter("nm");
        int o = Integer.parseInt(req.getParameter("o"));

        entity.setName(nm);
        entity.setOrder(o);
    }

    protected IntSuperManager<Season> getSuperManager(Statement stat) {
        return new SeasonManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return seasonId;
    }
}
