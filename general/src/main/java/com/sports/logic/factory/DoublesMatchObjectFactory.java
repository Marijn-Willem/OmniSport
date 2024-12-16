package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.Double;
import com.sports.entity.key.*;
import com.sports.entity.manager.DoublesMatchManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.logic.calculation.DoubleStandingProcessor;

import java.sql.Statement;

public class DoublesMatchObjectFactory extends H2HObjectFactory<CompSeasonDoubleKey,
        CompSeasonPhaseDoubleKey,
        Double,
        SuperKeyEntity,
        DoublesMatchKey,
        DoublesMatch,
        DoublesMatchPartKey,
        DoublesMatchPart,
        DoublesMatchPartStatKey,
        DoublesMatchPartStat> {
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
    public DoubleStandingProcessor getStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        return new DoubleStandingProcessor(stat, cspk);
    }

    @Override
    public DoublesMatchPartObjectFactory getPartObjectFactory() {
        return new DoublesMatchPartObjectFactory();
    }

    @Override
    public String getProcessManagePath() {
        return "ProcessManageDoublesMatch";
    }
}
