package com.management.servlet.ajax;

import com.sports.entity.Club;
import com.sports.entity.Equipe;
import com.sports.entity.Team;
import com.sports.entity.manager.ClubManager;
import com.sports.entity.manager.EquipeManager;
import com.sports.entity.manager.IntSuperManager;
import com.sports.entity.manager.TeamManager;
import com.sports.logic.util.Util;
import com.sportservlet.ajax.ProcessManageIntEntity;
import com.sportservlet.flush.CacheFlusher;
import com.sportservlet.flush.TeamFlusher;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageTeam extends ProcessManageIntEntity<Team> {
    protected IntSuperManager<Team> getSuperManager(Statement stat) {
        return new TeamManager(stat);
    }

    protected Integer getIdFromRequest(HttpServletRequest req) {
        return Util.convertStringToInteger(req.getParameter("tid"));
    }

    protected Team getNewEntity() {
        return new Team();
    }

    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) throws SQLException {
        String ds = req.getParameter("ds");
        String cn = req.getParameter("cn");
        String en = req.getParameter("en");
        Integer nid = convertRequestParamToIdInteger(req, "nid");
        int spid = getIntValuedParameterValue(req, "spid");
        int gid = getIntValuedParameterValue(req, "gid");

        Club club = !Util.isEmptyString(cn) ? new ClubManager(stat).getClubByName(cn) : null;
        Integer clid = club != null ? club.getId() : null;

        Equipe equipe = !Util.isEmptyString(en) ? new EquipeManager(stat).getEquipeByName(en) : null;
        Integer eqid = equipe != null ? equipe.getId() : null;

        entity.setDescription(ds);
        entity.setClubId(clid);
        entity.setEquipeId(eqid);
        entity.setNocId(nid);
        entity.setSportId(spid);
        entity.setGenderId(gid);
    }

    @Override
    protected CacheFlusher getCacheFlusher() {
        return new TeamFlusher(id);
    }
}
