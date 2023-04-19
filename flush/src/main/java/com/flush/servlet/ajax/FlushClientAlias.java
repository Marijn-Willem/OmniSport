package com.flush.servlet.ajax;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.ClientAliasKey;
import com.sports.entity.key.AliasEntityIdKey;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class FlushClientAlias extends Flush {
    @Override
    CacheKey getCacheKey(Statement stat, HttpServletRequest req) {
        int cnid = getIntValuedParameterValue(req, "cnid");
        int aeid = getIntValuedParameterValue(req, "aeid");
        String eid = req.getParameter("eid");

        return new ClientAliasKey(cnid, new AliasEntityIdKey(aeid, eid));
    }
}
