package com.sports.logic.factory;

import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.TeamMatchPart;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.key.TeamMatchKey;
import com.sports.entity.key.TeamMatchPartKey;
import com.sports.entity.manager.H2HMatchPartManager;
import com.sports.entity.manager.TeamMatchPartManager;

import java.sql.Statement;

public class TeamMatchPartObjectFactory implements H2HPartObjectFactory<TeamMatchPartKey, TeamMatchPart> {
    public H2HMatchPartManager<TeamMatchPartKey, TeamMatchPart> getMatchPartManager(Statement stat) {
        return new TeamMatchPartManager(stat);
    }

    public TeamMatchPartKey getMatchPartKey(H2HMatchKey h2HMatchKey, int specifId) {
        return new TeamMatchPartKey((TeamMatchKey)h2HMatchKey, specifId);
    }

    public TeamMatchPart getMatchPart() {
        return new TeamMatchPart();
    }

    public H2HPartStatObjectFactory<? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getPartStatObjectFactory() {
        return null;
    }
}
