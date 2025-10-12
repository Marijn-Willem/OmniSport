package com.speedskating.servlet.ajax;

import com.sports.entity.CompSeasonEvent;
import com.sportservlet.ajax.CompSeasonPortsAlcifoSport;

import java.io.IOException;
import java.io.Writer;

public class CompSeasonPorts extends CompSeasonPortsAlcifoSport {
    @Override
    public String getSpecificURLCells(CompSeasonEvent compSeasonEvent, boolean isTeam) {
        return getURLCell(compSeasonEvent.getCompSeasonEventId(), "EventHeats", "Heats") +
                getCalculateEventPersonSportRankingCell(compSeasonEvent.getCompSeasonEventId(), isTeam);
    }

    @Override
    protected void processSpecific(Writer w) throws IOException {
        w.append("<div id=\"divCalc\"></div>\n");
    }

    private String getCalculateEventPersonSportRankingCell(int compSeasonEventId, boolean isTeam) {
        return !isTeam ?
                "<td onclick=\"calculateEventPersonSportRanks(" + compSeasonEventId +
                        ");\">Calculate EventPersonSport Ranks</td>\n" : "<td/>\n";
    }
}
