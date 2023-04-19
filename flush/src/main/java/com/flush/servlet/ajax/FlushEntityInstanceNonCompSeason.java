package com.flush.servlet.ajax;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EntityInstanceNonCompSeasonKey;
import com.sports.entity.Club;
import com.sports.entity.Geo;
import com.sports.entity.Person;
import com.sports.entity.manager.ClubManager;
import com.sports.entity.manager.PersonManager;
import com.sports.logic.calculation.DbCalculation;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class FlushEntityInstanceNonCompSeason extends Flush {
    @Override
    CacheKey getCacheKey(Statement stat, HttpServletRequest req) throws SQLException {
        String en = req.getParameter("en");
        String eas = req.getParameter("eas");
        Integer entityId = getEntityId(stat, en, eas);

        return entityId != null ? new EntityInstanceNonCompSeasonKey(en, entityId) : null;
    }

    Integer getEntityId(Statement stat, String en, String eas) throws SQLException {
        if ("Club".equals(en)) {
            Club club = new ClubManager(stat).getClubByName(eas);
            return club != null ? club.getId() : null;
        }
        else if ("Geo".equals(en)) {
            Geo geo = new DbCalculation(stat).getGeoFromOutputString(eas);
            return geo != null ? geo.getId() : null;
        }
        else if ("Noc".equals(en))
            return Integer.valueOf(eas);
        else if ("Person".equals(en)) {
            Person person = new PersonManager(stat).getPersonByName(eas);
            return person != null ? person.getId() : null;
        }

        return null;
    }
}
