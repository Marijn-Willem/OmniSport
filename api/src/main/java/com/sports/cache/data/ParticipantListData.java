package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.*;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.*;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class ParticipantListData extends OutputData {
    final int competitionId;
    final int seasonId;
    final Integer clientId;

    private final List<ParticipantFragment> participantFragments = new ArrayList<>();

    abstract List<Integer> getParticipantIds(Statement stat, CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory) throws SQLException;

    public ParticipantListData(int competitionId, int seasonId, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.clientId = clientId;
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        if (!new com.sports.calc.alcifo.DbCalculation(stat).isAlcifo(competitionId)) {
            CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                    ? extends CompSeasonPhaseParticipantKey,
                    ? extends Participant,
                    ? extends SuperKeyEntity,
                    ? extends H2HMatchKey,
                    ? extends H2HMatch,
                    ? extends H2HMatchPartKey,
                    ? extends H2HMatchPart,
                    ? extends H2HMatchPartStatKey,
                    ? extends H2HMatchPartStat> factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);
            List<Integer> participantIds = getParticipantIds(stat, factory);
            List<? extends Participant> participants = factory.getParticipantManager(stat).getParticipantList(participantIds);
            participants.sort(new DescribedEntityDescription());

            int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevel);

            participantFragments.addAll(participants.stream().map(x ->
                    new ParticipantFragment(competitionId, seasonId, x.getId(), clientId, nestingLevelList, true)).toList());

            DataFragmentUtil.fillDataFragments(participantFragments, getCacheDataKey());
        }
    }

    @Override
    public boolean isValidOutput() {
        return !participantFragments.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("participantList",
                "participant", participantFragments);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("participantList", participantFragments) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("participantList", participantFragments);
    }
}
