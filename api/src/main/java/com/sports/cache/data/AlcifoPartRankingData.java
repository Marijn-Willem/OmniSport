package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.AlcifoParticipantFactory;
import com.sports.calc.alcifo.Calculation;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SportDisciplineManager;
import com.sports.entity.manager.SportEventManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AlcifoPartRankingData extends OutputData {
    final int competitionId;
    final int seasonId;
    final int compSeasonEventId;
    final int compSeasonEventPartId;
    final Integer clientId;

    private final List<AlcifoParticipantFragment> fragments = new ArrayList<>();

    abstract AlcifoPartParticipantFactory getFactory(AlcifoParticipantFactory participantFactory);
    abstract SuperKey getKey(CompSeasonEventPartKey compSeasonEventPartKey);

    public AlcifoPartRankingData(int competitionId, int seasonId, int compSeasonEventId,
                                 int compSeasonEventPartId, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.clientId = clientId;
    }

    @Override
    public boolean isValidOutput() {
        return !fragments.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("ranking", "participant", fragments);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("ranking", fragments) + "}";
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

        if (competition != null) {
            int sportId = competition.getSportId();

            CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(
                    new CompSeasonEventKey(
                            new CompSeasonKey(competitionId, seasonId), compSeasonEventId), compSeasonEventPartId);
            DbCalculation dbCalculation = new DbCalculation(stat);
            SportDiscipline sportDiscipline = getSportDiscipline(csepKey, dbCalculation, stat);

            if (sportDiscipline != null && dbCalculation.isAlcifo(competitionId)) {
                SportEventKey sek = new SportEventKey(sportId, compSeasonEventId);
                SportEvent se = new SportEventManager(stat).getEntityFromSuperKey(sek); // Guaranteed to exist due to sportDiscipline

                AlcifoPartParticipantFactory factory = getFactory(Calculation.getAlcifoParticipantFactory(se));
                dbCalculation.getFullRankingInPart(factory, getKey(csepKey)).forEach(x ->
                        fragments.add(getFragment(x, sportDiscipline.getResultTypeId(),
                                sportDiscipline.getResultTypePrecisionId()))
                );
            }

            DataFragmentUtil.fillDataFragments(fragments, getCacheKey());
        }
    }

    private AlcifoParticipantFragment getFragment(Participant participant, int resultTypeId,
                                                  Integer resultTypePrecisionId) {
        if (participant instanceof PersonSport)
            return new EventPartPersonSportFragment(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId,
                    (PersonSport) participant, resultTypeId, resultTypePrecisionId, clientId);
        else
            return new EventPartTeamFragment(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId,
                    (Team) participant, resultTypeId, resultTypePrecisionId, clientId);
    }

    private SportDiscipline getSportDiscipline(CompSeasonEventPartKey csepKey, DbCalculation dbCalc, Statement stat)
        throws SQLException {
        SportDisciplineKey sdKey = dbCalc.getSportDisciplineKey(csepKey);

        return sdKey != null ? new SportDisciplineManager(stat).getEntityFromSuperKey(sdKey) : null;
    }
}
