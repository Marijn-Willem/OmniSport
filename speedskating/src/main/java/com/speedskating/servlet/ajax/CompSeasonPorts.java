package com.speedskating.servlet.ajax;

import com.sportservlet.ajax.CompSeasonPortsAlcifoSport;

public class CompSeasonPorts extends CompSeasonPortsAlcifoSport {
    @Override
    public String getSpecificURLCells(int eventId) {
        return getURLCell(eventId, "EventHeats", "Heats");
    }
}
