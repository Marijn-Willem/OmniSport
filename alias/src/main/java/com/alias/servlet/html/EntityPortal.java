package com.alias.servlet.html;

import com.alias.servlet.util.ServletUtil;
import com.sports.entity.AliasEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EntityPortal extends SuperHtmlServlet {
    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        writeLink("Action type", AliasEntity.aliasEntityIdActionType, w);
        writeLink("Club", AliasEntity.aliasEntityIdClubInstance, w);
        writeLink("Competition", AliasEntity.aliasEntityIdCompetition, w);
        writeLink("Discipline part", AliasEntity.aliasEntityIdDisciplinePart, w);
        writeLink("Geo", AliasEntity.aliasEntityIdGeoInstance, w);
        writeLink("Location role", AliasEntity.aliasEntityIdLocationRole, w);
        writeLink("Noc", AliasEntity.aliasEntityIdNocInstance, w);
        writeLink("Person", AliasEntity.aliasEntityIdPersonInstance, w);
        writeLink("PhaseType", AliasEntity.aliasEntityIdPhaseType, w);
        writeLink("Sport", AliasEntity.aliasEntityIdSport, w);
        writeLink("Sport discipline", AliasEntity.aliasEntityIdSportDiscipline, w);
        writeLink("Sport event", AliasEntity.aliasEntityIdSportEvent, w);
        writeLink("Sport event part", AliasEntity.aliasEntityIdSportEventPart, w);
        writeLink("Stat type", AliasEntity.aliasEntityIdStatType, w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return null;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    private void writeLink(String entity, int aliasEntityId, Writer w) throws IOException {
        String href = ServletUtil.entityMetaDataMap.get(aliasEntityId).getEntityName() +
                "Portal?aeid=" + aliasEntityId;
        writeLink(href, entity, w);
    }
}
