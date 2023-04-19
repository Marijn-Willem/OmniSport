package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Participant;
import com.sports.entity.comparator.DescribedEntityDescription;
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

    abstract List<Integer> getParticipantIds(Statement stat, CompSeasonParticipantFactory factory)
            throws SQLException;

    public ParticipantListData(int competitionId, int seasonId, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.clientId = clientId;
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        if (!new com.sports.calc.alcifo.DbCalculation(stat).isAlcifo(competitionId)) {
            CompSeasonParticipantFactory factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);
            List<Integer> participantIds = getParticipantIds(stat, factory);
            List<Participant> participants = factory.getParticipantManager(stat).getParticipantList(participantIds);
            participants.sort(new DescribedEntityDescription());

            participantFragments.addAll(participants.stream().map(x ->
                    new ParticipantFragment(competitionId, seasonId, x.getId(), clientId)).toList());

            DataFragmentUtil.fillDataFragments(participantFragments, getCacheKey());
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
}
