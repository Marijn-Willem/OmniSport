package com.cyclingroad.servlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sportservlet.ajax.CompSeasonPortsAlcifoSport;
import com.sportservlet.util.ServletUtil;

import java.io.IOException;
import java.io.Writer;

public class CompSeasonPorts extends CompSeasonPortsAlcifoSport {
    @Override
    public String getSpecificURLCells(CompSeasonEvent compSeasonEvent, boolean isTeam) {
        String importTitle = isTeam ? "Import teams" : "Import persons";

        return getURLCell(compSeasonEvent.getCompSeasonEventId(), "CompSeasonEventPartPortal", "Manage Event Parts") +
                getURLCell(compSeasonEvent.getCompSeasonEventId(), "EventParticipantFromCompSeasonImport",
                        importTitle + " (comp season)");
    }

    @Override
    protected void processSpecific(Writer w) throws IOException {
        ServletUtil.writeGenericGoToButton("CompSeasonTeamPortal", compSeasonUrlParameters, "Manage teams", w);
    }
}
