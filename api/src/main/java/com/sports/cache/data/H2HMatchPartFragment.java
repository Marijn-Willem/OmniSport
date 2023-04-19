package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.H2HMatchPartKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.H2HMatchPart;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.H2HMatchPartManager;
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

    public H2HMatchPartFragment(int competitionId, int seasonId, int matchId, int specificId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.matchId = matchId;
        this.specificId = specificId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new H2HMatchPartKey(competitionId, seasonId, matchId, specificId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        H2HObjectFactory factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId)
                .getH2HObjectFactory();
        H2HPartObjectFactory partFactory = factory.getPartObjectFactory();

        com.sports.entity.key.H2HMatchKey matchKey = factory.getKey(new CompSeasonKey(competitionId, seasonId), matchId);
        H2HMatchPartManager manager = partFactory.getMatchPartManager(stat);
        com.sports.entity.key.H2HMatchPartKey matchPartKey = partFactory.getMatchPartKey(matchKey, specificId);
        H2HMatchPart matchPart = manager.getH2HMatchPart(matchPartKey);

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
}
