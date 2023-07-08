package com.sportservlet.html;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.Gender;
import com.sports.entity.SportEvent;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SportEventManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class ManageCompSeasonEvent extends ManageEntity {
    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("compseasonevent");
    }

    @Override
    protected String getEntityIdName() {
        return "cseid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        CompSeasonEvent cse = null;

        if (!"i".equals(mode)) {
            int cseid = getIntValuedParameterValue(req, "cseid");
            CompSeasonEventKey cseKey = new CompSeasonEventKey(compSeasonKey, cseid);
            cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);
        }

        int sportId = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
        List<SportEvent> sportsEvents = new SportEventManager(stat).getSportEventList(
                Collections.singletonList(sportId));
        sportsEvents.sort(new AliasableName());

        LinkedHashMap<Integer, String> sportsEventMap = getLinkedHashMapFromNamedEntities(sportsEvents, false,
                SportEvent::getSportEventId);

        Writer w = res.getWriter();

        writeSelectWithLabel("Sport event", "seid", sportsEventMap, cse != null ? cse.getSportEventKey().getSportEventId() : null, w);
        writeSelectWithLabel("Gender", "gid", Gender.getGenderLinkedHashMap(), cse != null ? cse.getGenderId() : null, w);
        writeTextFieldWithLabel("External source", "es", cse != null ? cse.getExternalSource() : null, w);
    }
}
