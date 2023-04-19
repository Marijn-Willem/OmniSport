package com.cyclingroad.servlet.ajax;

import com.sports.entity.SportEvent;
import com.sportservlet.ajax.CompSeasonPortsAlcifoSport;
import com.sportservlet.util.ServletUtil;

import java.io.IOException;
import java.io.Writer;

public class CompSeasonPorts extends CompSeasonPortsAlcifoSport {
    @Override
    public String getSpecificURLCells(int eventId) {
        boolean isTeam = false;

        for (SportEvent sportEvent : sportEventList)
            if (sportEvent.getSportEventId() == eventId) {
                isTeam = sportEvent.isTeam();
                break;
            }

        String importTitle = isTeam ? "Import teams" : "Import persons";

        return getURLCell(eventId, "CompSeasonEventPartPortal", "Manage Event Parts") +
                getURLCell(eventId, "EventParticipantFromCompSeasonImport", importTitle + " (comp season)");
    }

    @Override
    protected void processSpecific(Writer w) throws IOException {
        ServletUtil.writeGenericGoToButton("CompSeasonTeamPortal", compSeasonUrlParameters, "Manage teams", w);
    }
}
