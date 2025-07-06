package com.flush.servlet.ajax;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.EntityInstanceNonCompSeasonKey;
import com.sports.entity.*;
import com.sports.entity.manager.ClubManager;
import com.sports.entity.manager.EquipeManager;
import com.sports.entity.manager.PersonManager;
import com.sports.logic.calculation.DbCalculation;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class FlushEntityInstanceNonCompSeason extends Flush {
    @Override
    CacheFragmentKey getCacheKey(Statement stat, HttpServletRequest req) throws SQLException {
        String en = req.getParameter("en");
        String eas = req.getParameter("eas");
        Integer entityId = getEntityId(stat, en, eas);

        return entityId != null ? new EntityInstanceNonCompSeasonKey(en, entityId) : null;
    }

    Integer getEntityId(Statement stat, String en, String eas) throws SQLException {
        NamedIntEntity entity = null;
        Integer entityId = null;

        if ("Club".equals(en))
            entity = new ClubManager(stat).getClubByName(eas);
        else if ("Geo".equals(en))
            entity = new DbCalculation(stat).getGeoFromOutputString(eas);
        else if ("Noc".equals(en))
            entityId = Integer.valueOf(eas);
        else if ("Person".equals(en))
            entity = new PersonManager(stat).getPersonByName(eas);
        else if ("Equipe".equals(en))
            entity = new EquipeManager(stat).getEquipeByName(eas);

        entityId = entity != null ? Integer.valueOf(entity.getId()) : entityId;

        return entityId;
    }
}
