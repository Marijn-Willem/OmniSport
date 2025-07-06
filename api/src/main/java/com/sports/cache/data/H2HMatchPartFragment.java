package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.H2HMatchPartKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.H2HObjectFactory;
import com.sports.logic.factory.H2HPartObjectFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class H2HMatchPartFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int matchId;
    private final int specificId;

    private String name;
    private Integer parentMatchPartId;
    private boolean finished;

    public H2HMatchPartFragment(int competitionId, int seasonId, int matchId, int specificId, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.matchId = matchId;
        this.specificId = specificId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new H2HMatchPartKey(competitionId, seasonId, matchId, specificId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        H2HObjectFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends com.sports.entity.key.H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId)
                .getH2HObjectFactory();
        H2HPartObjectFactory<? extends com.sports.entity.key.H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> partFactory = factory.getPartObjectFactory();

        com.sports.entity.key.H2HMatchKey matchKey = factory.getKey(new CompSeasonKey(competitionId, seasonId), matchId);
        H2HMatchPart matchPart = partFactory.getEntity(stat, matchKey, specificId);

        name = matchPart.getName();
        parentMatchPartId = matchPart.getParentMatchPartId();
        finished = matchPart.isFinished();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("name", name) +
                XmlUtil.getTag("parentMatchPartId", parentMatchPartId) +
                XmlUtil.getTag("finished", finished);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getEntry("parentMatchPartId", parentMatchPartId) + "," +
                JsonUtil.getEntry("finished", finished);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("name", name, isInList) +
                yamlUtil.getEntry("parentMatchPartId", parentMatchPartId) +
                yamlUtil.getEntry("finished", finished);
    }
}
