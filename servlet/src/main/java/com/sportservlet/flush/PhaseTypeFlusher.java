package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.PhaseTypeAsParentKey;
import com.sports.cache.key.PhaseTypeKey;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhaseTypeFlusher extends CacheFlusher {
    private final int phaseTypeId;

    public PhaseTypeFlusher(int phaseTypeId) {
        this.phaseTypeId = phaseTypeId;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) {
        return new ArrayList<>() {{
            add(new PhaseTypeKey(phaseTypeId));
            add(new PhaseTypeAsParentKey(phaseTypeId));
        }};
    }
}
