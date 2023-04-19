package com.sports.logic.factory;

import com.sports.entity.DoublesMatch;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.DoublesMatchKey;
import com.sports.entity.manager.DoublesMatchManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.calculation.DoubleStandingProcessor;
import com.sports.logic.calculation.StandingProcessor;

import java.sql.Statement;

public class DoublesMatchObjectFactory implements H2HObjectFactory<DoublesMatchKey, DoublesMatch> {
    @Override
    public H2HMatchManager<DoublesMatchKey, DoublesMatch> getManager(Statement stat) {
        return new DoublesMatchManager(stat);
    }

    @Override
    public DoublesMatchKey getKey(CompSeasonKey compSeasonKey, int specifId) {
        return new DoublesMatchKey(compSeasonKey, specifId);
    }

    @Override
    public DoublesMatch getMatch() {
        return new DoublesMatch();
    }

    @Override
    public StandingProcessor getStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        return new DoubleStandingProcessor(stat, cspk);
    }

    @Override
    public DoublesMatchPartObjectFactory getPartObjectFactory() {
        return new DoublesMatchPartObjectFactory();
    }

    @Override
    public boolean showMatchListForPhase() {
        return true;
    }
}
