package com.speedskating.servlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sportservlet.ajax.CompSeasonPortsAlcifoSport;

public class CompSeasonPorts extends CompSeasonPortsAlcifoSport {
    @Override
    public String getSpecificURLCells(CompSeasonEvent compSeasonEvent, boolean isTeam) {
        return getURLCell(compSeasonEvent.getCompSeasonEventId(), "EventHeats", "Heats");
    }
}
