package com.sports.cache.data;

import com.sports.cache.util.*;
import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.comparator.CompSeasonStartDate;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonManager;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class EncounterListData extends OutputData {
    final int participant1Id;
    final int participant2Id;
    final Integer clientId;

    abstract CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getCompSeasonParticipantFactory();

    private final List<MatchListCompSeasonFragment> matchListCompSeasonFragmentList = new ArrayList<>();

    public EncounterListData(int participant1Id, int participant2Id, Integer clientId) {
        this.participant1Id = Math.min(participant1Id, participant2Id);
        this.participant2Id = Math.max(participant1Id, participant2Id);
        this.clientId = clientId;
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        Map<CompSeasonKey, List<H2HMatch>> encounterMap = new DbCalculation(stat).getEncountersBetweenParticipants(
                getCompSeasonParticipantFactory(), participant1Id, participant2Id);

        List<CompSeason> compSeasons = new CompSeasonManager(stat).getCompSeasonList(encounterMap.keySet());
        compSeasons.sort(new CompSeasonStartDate());

        Map<CompSeasonKey, List<CompSeasonPhase>> compSeasonPhaseMap = getCompSeasonPhaseMap(encounterMap, stat);

        MatchListCompSeasonFilter filter = new MatchListCompSeasonFilter(clientId, getCacheKey(), stat);

        compSeasons.forEach((x) -> {
            CompSeasonKey csKey = new CompSeasonKey(x.getCompetitionId(), x.getSeasonId());
            List<H2HMatch> h2HMatches = encounterMap.get(csKey);
            List<CompSeasonPhase> compSeasonPhases = compSeasonPhaseMap.get(csKey);
            LinkedHashMap<Integer, List<H2HMatch>> cspMatchMap = getCompSeasonPhaseMatchMap(h2HMatches, compSeasonPhases);

            MatchListCompSeasonFragment fragment = new MatchListCompSeasonFragment(x.getCompetitionId(), x.getSeasonId(),
                    clientId, cspMatchMap, DataFragmentUtil.getLevelForNestedList(nestingLevel));

            if (filter.isElementAllowed(fragment))
                matchListCompSeasonFragmentList.add(fragment);
        });

        DataFragmentUtil.fillDataFragments(matchListCompSeasonFragmentList, getCacheKey());
    }

    @Override
    public boolean isValidOutput() {
        return !matchListCompSeasonFragmentList.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("compSeasonList", "compSeason",
                matchListCompSeasonFragmentList);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("compSeasonList", matchListCompSeasonFragmentList) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("compSeasonList", matchListCompSeasonFragmentList);
    }

    private LinkedHashMap<Integer, List<H2HMatch>> getCompSeasonPhaseMatchMap(List<H2HMatch> matchesCompSeason,
                                                                              List<CompSeasonPhase> compSeasonPhases) {
        return new LinkedHashMap<>() {{
            compSeasonPhases.forEach(x -> put(x.getCompSeasonPhaseKey().getCompSeasonPhaseId(), new ArrayList<>()));
            matchesCompSeason.forEach(x -> get(x.getCompSeasonPhaseId()).add(x));
        }};
    }

    private Map<CompSeasonKey, List<CompSeasonPhase>> getCompSeasonPhaseMap(Map<CompSeasonKey, List<H2HMatch>> encounterMap,
                                                                            Statement stat) throws SQLException {
        List<CompSeasonPhaseKey> cspKeys = new ArrayList<>() {{
            encounterMap.values().forEach((v) -> addAll(v.stream().map(H2HMatch::getCompSeasonPhaseKey).toList()));
        }};

        List<CompSeasonPhase> compSeasonPhases = new CompSeasonPhaseManager(stat).getCompSeasonPhases(cspKeys);

        Map<CompSeasonKey, List<CompSeasonPhase>> cspMap = new HashMap<>() {{
            compSeasonPhases.forEach(x -> {
                CompSeasonKey csKey = x.getCompSeasonPhaseKey().getSuperKey();

                if (!containsKey(csKey))
                    put(csKey, new ArrayList<>());

                get(csKey).add(x);
            });
        }};

        Comparator<CompSeasonPhase> comparator = new CompSeasonPhaseRoundDescription();

        cspMap.values().forEach(x -> x.sort(comparator));

        return cspMap;
    }
}
