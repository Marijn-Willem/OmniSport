package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.H2HMatchKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.H2HMatch;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class H2HMatchFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int matchId;
    private final int clientId;
    private final int nestingLevelFragment;

    private final ParticipantFragment participant1Fragment;
    private final ParticipantFragment participant2Fragment;
    private final Integer score1;
    private final Integer score2;
    private final NoCountResultFragment noCountResult1Fragment;
    private final NoCountResultFragment noCountResult2Fragment;
    private final boolean finished;
    private final Integer knockoutOrder;
    private final LocalDateTime date;

    public H2HMatchFragment(H2HMatch h2HMatch, int clientId, int nestingLevel) {
        super(nestingLevel, true);

        competitionId = h2HMatch.getCompSeasonPhaseKey().getCompetitionId();
        seasonId = h2HMatch.getCompSeasonPhaseKey().getSeasonId();
        matchId = h2HMatch.getSpecificId();
        this.clientId = clientId;
        nestingLevelFragment = YamlUtil.getLevelForNestedFragment(nestingLevel);

        participant1Fragment = getParticipantFragment(h2HMatch.getParticipant1Id());
        participant2Fragment = getParticipantFragment(h2HMatch.getParticipant2Id());
        score1 = h2HMatch.getScore1_1();
        score2 = h2HMatch.getScore1_2();
        Integer ncr1 = h2HMatch.getParticipant1NcrId();
        Integer ncr2 = h2HMatch.getParticipant2NcrId();
        noCountResult1Fragment = ncr1 != null ? new NoCountResultFragment(ncr1, nestingLevelFragment) : null;
        noCountResult2Fragment = ncr2 != null ? new NoCountResultFragment(ncr2, nestingLevelFragment) : null;
        finished = h2HMatch.isFinished();
        knockoutOrder = h2HMatch.getKnockoutOrder();
        date = h2HMatch.getDate();
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new H2HMatchKey(competitionId, seasonId, matchId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        if (participant1Fragment != null)
            DataFragmentUtil.getFilledDataFragment(participant1Fragment, getCacheDataKey(), stat);

        if (participant2Fragment != null)
            DataFragmentUtil.getFilledDataFragment(participant2Fragment, getCacheDataKey(), stat);

        if (noCountResult1Fragment != null)
            DataFragmentUtil.getFilledDataFragment(noCountResult1Fragment, getCacheDataKey(), stat);

        if (noCountResult2Fragment != null)
            DataFragmentUtil.getFilledDataFragment(noCountResult2Fragment, getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("matchId", matchId) +
                XmlUtil.getNullableFragmentAsTag("participant1", participant1Fragment) +
                XmlUtil.getNullableFragmentAsTag("participant2", participant2Fragment) +
                XmlUtil.getTag("score1", score1) +
                XmlUtil.getTag("score2", score2) +
                XmlUtil.getNullableFragmentAsTag("noCountResult1", noCountResult1Fragment) +
                XmlUtil.getNullableFragmentAsTag("noCountResult2", noCountResult2Fragment) +
                XmlUtil.getTag("finished", finished) +
                XmlUtil.getTag("knockoutOrder", knockoutOrder) +
                XmlUtil.getTag("date", date);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("matchId", matchId) + "," +
                JsonUtil.getNullableFragmentAsEntry("participant1", participant1Fragment) + "," +
                JsonUtil.getNullableFragmentAsEntry("participant2", participant2Fragment) + "," +
                JsonUtil.getEntry("score1", score1) + "," +
                JsonUtil.getEntry("score2", score2) + "," +
                JsonUtil.getNullableFragmentAsEntry("noCountResult1", noCountResult1Fragment) + "," +
                JsonUtil.getNullableFragmentAsEntry("noCountResult2", noCountResult2Fragment) + "," +
                JsonUtil.getEntry("finished", finished) + "," +
                JsonUtil.getEntry("knockoutOrder", knockoutOrder) + "," +
                JsonUtil.getEntry("date", date);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("matchId", matchId, isInList) +
                yamlUtil.getNullableFragmentAsEntry("participant1", participant1Fragment) +
                yamlUtil.getNullableFragmentAsEntry("participant2", participant2Fragment) +
                yamlUtil.getEntry("score1", score1) +
                yamlUtil.getEntry("score2", score2) +
                yamlUtil.getNullableFragmentAsEntry("noCountResult1", noCountResult1Fragment) +
                yamlUtil.getNullableFragmentAsEntry("noCountResult2", noCountResult2Fragment) +
                yamlUtil.getEntry("finished", finished) +
                yamlUtil.getEntry("knockoutOrder", knockoutOrder) +
                yamlUtil.getEntry("date", date);
    }

    private ParticipantFragment getParticipantFragment(Integer participantId) {
        return participantId != null ?
                new ParticipantFragment(competitionId, seasonId, participantId, clientId, nestingLevelFragment, false) : null;
    }
}
