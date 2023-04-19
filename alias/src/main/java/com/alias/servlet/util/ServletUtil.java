package com.alias.servlet.util;

import com.sports.entity.AliasEntity;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ServletUtil {
    public static final Map<Integer, EntityMetaData> entityMetaDataMap = new HashMap<Integer, EntityMetaData>() {{
        put(AliasEntity.aliasEntityIdActionType, new EntityMetaData("ActionType", false));
        put(AliasEntity.aliasEntityIdClubInstance, new EntityMetaData("Club", true));
        put(AliasEntity.aliasEntityIdCompetition, new EntityMetaData("Competition", false));
        put(AliasEntity.aliasEntityIdDisciplinePart, new EntityMetaData("DisciplinePart", false));
        put(AliasEntity.aliasEntityIdGeoInstance, new EntityMetaData("Geo", true));
        put(AliasEntity.aliasEntityIdLocationRole, new EntityMetaData("LocationRole", false));
        put(AliasEntity.aliasEntityIdNocInstance, new EntityMetaData("Noc", true));
        put(AliasEntity.aliasEntityIdPersonInstance, new EntityMetaData("Person", true));
        put(AliasEntity.aliasEntityIdPhaseType, new EntityMetaData("PhaseType", false));
        put(AliasEntity.aliasEntityIdSport, new EntityMetaData("Sport", false));
        put(AliasEntity.aliasEntityIdSportDiscipline, new EntityMetaData("SportDiscipline", false));
        put(AliasEntity.aliasEntityIdSportEvent, new EntityMetaData("SportEvent", false));
        put(AliasEntity.aliasEntityIdSportEventPart, new EntityMetaData("SportEventPart", false));
        put(AliasEntity.aliasEntityIdStatType, new EntityMetaData("StatType", false));
    }};

    public static void writeEntityList(Integer aeid, Writer w) throws IOException {
        w.append("<ul>\n");
        writeEntityListItem("Action type", AliasEntity.aliasEntityIdActionType, aeid, w);
        writeEntityListItem("Club", AliasEntity.aliasEntityIdClubInstance, aeid, w);
        writeEntityListItem("Competition", AliasEntity.aliasEntityIdCompetition, aeid, w);
        writeEntityListItem("Discipline part", AliasEntity.aliasEntityIdDisciplinePart, aeid, w);
        writeEntityListItem("Geo", AliasEntity.aliasEntityIdGeoInstance, aeid, w);
        writeEntityListItem("Location role", AliasEntity.aliasEntityIdLocationRole, aeid, w);
        writeEntityListItem("Noc", AliasEntity.aliasEntityIdNocInstance, aeid, w);
        writeEntityListItem("Person", AliasEntity.aliasEntityIdPersonInstance, aeid, w);
        writeEntityListItem("PhaseType", AliasEntity.aliasEntityIdPhaseType, aeid, w);
        writeEntityListItem("Sport", AliasEntity.aliasEntityIdSport, aeid, w);
        writeEntityListItem("Sport discipline", AliasEntity.aliasEntityIdSportDiscipline, aeid, w);
        writeEntityListItem("Sport event", AliasEntity.aliasEntityIdSportEvent, aeid, w);
        writeEntityListItem("Sport event part", AliasEntity.aliasEntityIdSportEventPart, aeid, w);
        writeEntityListItem("Stat type", AliasEntity.aliasEntityIdStatType, aeid, w);
        w.append("</ul><br/>\n");
    }

    private static void writeEntityListItem(String text, int aliasEntityId, Integer curId, Writer w) throws IOException{
        w.append("<li");
        if (Integer.valueOf(aliasEntityId).equals(curId))
            w.append(" class=\"ci\"");
        w.append(">");
        w.append(text);
        w.append("</li>\n");
    }
}
