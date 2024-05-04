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
import com.sports.entity.manager.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AlcifoPartRankingData<PK extends SuperKey> extends OutputData {
    final int competitionId;
    final int seasonId;
    final int compSeasonEventId;
    final int compSeasonEventPartId;
    final Integer clientId;

    private final List<AlcifoParticipantFragment> fragments = new ArrayList<>();

    abstract AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends SuperKeyEntity,
            ? extends Participant,
            ? extends AlcifoParticipantKey,
            ? extends AlcifoParticipant,
            ? extends SuperKey,
            PK,
            ? extends AlcifoPartParticipant,
            ? extends SuperKey,
            ? extends AlcifoPartParticipant> getFactory(AlcifoParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends SuperKeyEntity,
                ? extends Participant,
                ? extends AlcifoParticipantKey,
                ? extends AlcifoParticipant,
                ? extends SuperKey,
                ? extends AlcifoPartParticipant> participantFactory);
    abstract PK getKey(CompSeasonEventPartKey compSeasonEventPartKey);

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
        CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(
                new CompSeasonEventKey(
                        new CompSeasonKey(competitionId, seasonId), compSeasonEventId), compSeasonEventPartId);

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepKey);

        DbCalculation dbCalculation = new DbCalculation(stat);

        if (compSeasonEventPart != null && dbCalculation.isAlcifo(competitionId)) {
            SportDiscipline sportDiscipline = new SportDisciplineManager(stat).getEntityFromSuperKey(
                    compSeasonEventPart.getSportDisciplineKey());

            CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(csepKey.getSuperKey());
            SportEvent se = new SportEventManager(stat).getEntityFromSuperKey(cse.getSportEventKey());

            AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
                    ? extends SuperKeyEntity,
                    ? extends Participant,
                    ? extends AlcifoParticipantKey,
                    ? extends AlcifoParticipant,
                    ? extends SuperKey,
                    PK,
                    ? extends AlcifoPartParticipant,
                    ? extends SuperKey,
                    ? extends AlcifoPartParticipant> factory = getFactory(Calculation.getAlcifoParticipantFactory(se));
            dbCalculation.getFullRankingInPart(factory, getKey(csepKey)).forEach(x ->
                    fragments.add(getFragment(x, sportDiscipline.getResultTypeId(),
                            sportDiscipline.getResultTypePrecisionId()))
            );

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
}
