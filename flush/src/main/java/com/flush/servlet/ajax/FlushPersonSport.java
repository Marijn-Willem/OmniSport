package com.flush.servlet.ajax;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.PersonSportKey;
import com.sports.entity.key.CompSeasonPersonSportKey;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class FlushPersonSport extends Flush {
    @Override
    CacheFragmentKey getCacheKey(Statement stat, HttpServletRequest req) {
        int psid = getIntValuedParameterValue(req, "psid");

        CompSeasonPersonSportKey compSeasonPersonSportKey = new CompSeasonPersonSportKey(compSeasonKey, psid);

        return new PersonSportKey(compSeasonPersonSportKey);
    }
}
