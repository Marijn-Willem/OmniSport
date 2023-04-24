package com.sports.cache.util;

import com.sports.cache.key.CacheDataKey;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record DescribedEntityUtil(int clientId, CacheDataKey cacheDataKey, Statement stat) {
    public String getPersonSportString(PersonSport personSport, CompSeasonKey compSeasonKey)
            throws SQLException {
        return new EntityInstanceUtil(clientId, cacheDataKey, stat).getPersonString(personSport.getPersonId(), compSeasonKey);
    }

    public String getTeamString(Team team) throws SQLException {
        if (team.getClubId() != null)
            return new EntityInstanceUtil(clientId, cacheDataKey, stat).getClubString(team.getClubId());

        if (team.getNocId() != null)
            return new EntityInstanceUtil(clientId, cacheDataKey, stat).getNocString(team.getNocId());

        return team.getDescription();
    }

    public String getTeamString(Team team, CompSeasonKey compSeasonKey) throws SQLException {
        if (team.getClubId() != null)
            return new EntityInstanceUtil(clientId, cacheDataKey, stat).getClubString(team.getClubId(), compSeasonKey);

        if (team.getNocId() != null)
            return new EntityInstanceUtil(clientId, cacheDataKey, stat).getNocString(team.getNocId(), compSeasonKey);

        if (team.getEquipeId() != null)
            return new EntityInstanceUtil(clientId, cacheDataKey, stat).getEquipeString(team.getEquipeId(), compSeasonKey);

        return team.getDescription();
    }

    public String getDoubleString(com.sports.entity.Double dbl, CompSeasonKey compSeasonKey)
            throws SQLException {
        Map<Integer, PersonSport> personSportMap = new PersonSportManager(stat).getPersonSportMap(
                new ArrayList<>() {{
                    add(dbl.getPersonSport1Id());
                    add(dbl.getPersonSport2Id());
                }}
        );

        String personSport1String = getPersonSportString(personSportMap.get(dbl.getPersonSport1Id()), compSeasonKey);
        String personSport2String = getPersonSportString(personSportMap.get(dbl.getPersonSport2Id()), compSeasonKey);

        return Util.concatStringsWithDelimiter(personSport1String, personSport2String, " / ");
    }

    public String getCompSeasonEventPartString(CompSeasonEventPart compSeasonEventPart) throws SQLException {
        AliasUtil aliasUtil = new AliasUtil(clientId, cacheDataKey, stat);
        EntityInstanceUtil entityInstanceUtil = new EntityInstanceUtil(clientId, cacheDataKey, stat);

        if (compSeasonEventPart.getSportEventPartId() != null) {
            CompSeasonEvent compSeasonEvent = getCompSeasonEvent(compSeasonEventPart);
            SportEventPartKey sportEventPartKey = new SportEventPartKey(compSeasonEvent.getSportEventKey(),
                    compSeasonEventPart.getSportEventPartId());

            SportEventPart sportEventPart = new SportEventPartManager(stat).getSportEventPart(sportEventPartKey);

            return aliasUtil.getAliasableAsClientSpecificString(sportEventPart, sportEventPartKey);
        }

        if (compSeasonEventPart.getEventPartNameId() != null) {
            CompSeasonEvent compSeasonEvent = getCompSeasonEvent(compSeasonEventPart);
            EventPartNameKey eventPartNameKey = new EventPartNameKey(compSeasonEvent.getSportEventKey(),
                    compSeasonEventPart.getEventPartNameId());

            EventPartName eventPartName = new EventPartNameManager(stat).getEntityFromSuperKey(eventPartNameKey);

            return aliasUtil.getAliasableAsClientSpecificString(eventPartName, eventPartNameKey);
        }

        CompSeasonEventPartKey compSeasonEventPartKey = compSeasonEventPart.getCompSeasonEventPartKey();

        List<EventPartLocation> eventPartLocations = new EventPartLocationManager(stat)
                .getEventPartLocations(compSeasonEventPartKey);

        if (eventPartLocations.size() == 1 && eventPartLocations.get(0).getGeoId() != null) {
            int geoId = eventPartLocations.get(0).getGeoId();
            CompSeasonKey compSeasonKey = compSeasonEventPartKey.getSuperKey().getSuperKey();

            return entityInstanceUtil.getGeoString(geoId, compSeasonKey);
        }

        if (compSeasonEventPart.getSportDisciplineId() != null) {
            CompSeasonEvent compSeasonEvent = getCompSeasonEvent(compSeasonEventPart);
            SportDisciplineKey sportDisciplineKey = new SportDisciplineKey(
                    compSeasonEvent.getSportEventKey().getSportId(), compSeasonEventPart.getSportDisciplineId());

            SportDiscipline sportDiscipline = new SportDisciplineManager(stat)
                    .getEntityFromSuperKey(sportDisciplineKey);

            return aliasUtil.getAliasableAsClientSpecificString(sportDiscipline, sportDisciplineKey);
        }

        return null;
    }

    private CompSeasonEvent getCompSeasonEvent(CompSeasonEventPart compSeasonEventPart) throws SQLException {
        CompSeasonEventKey cseKey = compSeasonEventPart.getCompSeasonEventPartKey().getSuperKey();
        return new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);
    }
}
