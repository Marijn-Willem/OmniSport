package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.DisciplinePartKey;
import com.sports.cache.util.AliasUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.DisciplinePart;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.DisciplinePartManager;

import java.sql.SQLException;
import java.sql.Statement;

public class DisciplinePartFragment extends WritableFragment {
    private final int sportId;
    private final int sportDisciplineId;
    private final int disciplinePartId;
    private final int clientId;

    private String name;

    public DisciplinePartFragment(int sportId, int sportDisciplineId, int disciplinePartId, int clientId) {
        this.sportId = sportId;
        this.sportDisciplineId = sportDisciplineId;
        this.disciplinePartId = disciplinePartId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new DisciplinePartKey(sportId, sportDisciplineId, disciplinePartId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        com.sports.entity.key.DisciplinePartKey dpKey = new com.sports.entity.key.DisciplinePartKey(
                new SportDisciplineKey(sportId, sportDisciplineId), disciplinePartId
        );

        DisciplinePart dp = new DisciplinePartManager(stat).getDisciplinePart(dpKey);
        name = new AliasUtil(clientId, getCacheDataKey(), stat).getAliasableAsClientSpecificString(dp, dpKey);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("name", name);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("name", name);
    }
}
