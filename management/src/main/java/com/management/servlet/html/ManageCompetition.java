package com.management.servlet.html;

import com.sports.entity.Competition;
import com.sports.entity.Gender;
import com.sports.entity.Sport;
import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SportManager;
import com.sports.entity.manager.TeamManager;
import com.sports.logic.util.Util;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

public class ManageCompetition extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompetitionPortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("competition");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "cid";
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initManageCompetition();\">\n");
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Competition competition = null;
        LinkedHashMap<Integer, String> cupTeamInitMap = new LinkedHashMap<>();
        cupTeamInitMap.put(0, "-");
        List<Sport> sportList = new SportManager(stat).getFullSportList();

        if ("u".equals(mode)) {
            competition = new CompetitionManager(stat).getCompetition(competitionId);

            List<Team> cupTeamInitList = new TeamManager(stat).getTeamListForSport(competition.getSportId());
            cupTeamInitList.sort(new DescribedEntityDescription());
            for (Team cupTeam : cupTeamInitList)
                cupTeamInitMap.put(cupTeam.getId(), cupTeam.getDescription());
        }

        Writer w = res.getWriter();

        boolean isDomestic = competition != null && competition.isDomestic();

        writeTextFieldWithLabel("Name", "nm", competition != null ? competition.getName() : null, w);
        writeSportSelect(sportList, competition != null ? competition.getSportId() : null, w);
        writeSelectWithLabel("Gender", "gid", Gender.genderLinkedHashMap, competition != null ? competition.getGenderId() : null, w);
        writeCheckbox("H2H Double", "dbl", competition != null && competition.isH2hDouble(), w);
        writeSelectWithLabel("Initial cup team", "cti", cupTeamInitMap, competition != null ? competition.getCupTeamInitId() : null, w);
        writeDateTimeField("Initial cup date", "cdi", competition != null ? competition.getCupDateInit() : null, w);
        writeCheckbox("Domestic", "dm", isDomestic, false, "handleCheckDomestic()", w);
        writeSelectWithLabel("Geo", "geid", getGeoMapWithCountries(stat), competition != null ? competition.getGeoId() : null, !isDomestic, w);
    }

    private void writeSportSelect(List<Sport> sportList, Integer sportId, Writer w) throws IOException {
        w.append("<span>Sport: <select name=\"spid\" onchange=\"handleSelectSportId();\">\n");

        for (Sport sport : sportList) {
            w.append("<option value=\"");
            w.append(Integer.toString(sport.getId()));
            w.append("\"");
            if (!sport.isTeam() && !sport.isH2H())
                w.append(" class=\"alcifo\"");
            if (Util.compareIntegers(sportId, sport.getId()))
                w.append(" selected");
            w.append(">");
            w.append(sport.getName());
            w.append("</option>\n");
        }

        w.append("</select></span><br/>\n");
    }
}
