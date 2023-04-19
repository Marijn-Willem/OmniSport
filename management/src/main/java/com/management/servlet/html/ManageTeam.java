package com.management.servlet.html;

import com.sports.entity.Gender;
import com.sports.entity.Sport;
import com.sports.entity.Team;
import com.sports.entity.manager.ClubManager;
import com.sports.entity.manager.EquipeManager;
import com.sports.entity.manager.SportManager;
import com.sports.entity.manager.TeamManager;
import com.sportservlet.html.ManageEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

public class ManageTeam extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "TeamPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("team");
        cssList.add("styling");
    }

    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "tid";
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Team team = null;

        if (!"i".equals(mode)) {
            int tid = getIntValuedParameterValue(req, "tid");
            team = new TeamManager(stat).getEntityFromId(tid);
        }

        int spid = team != null ? team.getSportId() : getIntValuedParameterValue(req, "spid");
        Sport sport = new SportManager(stat).getSport(spid);
        LinkedHashMap<Integer, String> sportMap = new LinkedHashMap<>();
        sportMap.put(spid, sport.getName());

        int gid = team != null ? team.getGenderId() : getIntValuedParameterValue(req, "gid");

        String cn = team != null && team.getClubId() != null ?
                new ClubManager(stat).getEntityFromId(team.getClubId()).getName() : null;

        String en = team != null && team.getEquipeId() != null ?
                new EquipeManager(stat).getEntityFromId(team.getEquipeId()).getName() : null;

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Description", "ds", team != null ? team.getDescription() : null, w);
        writeTextFieldWithOnKeypress("Club", "cn", cn, "loadClubList()", w);
        w.append("<table id=\"tblCn\" border=\"1\">\n</table>\n");
        writeTextFieldWithOnKeypress("Equipe", "en", en, "loadEquipeList()", w);
        w.append("<table id=\"tblEn\" border=\"1\">\n</table>\n");
        writeSelectWithLabel("Noc", "nid", getFullNocMap(stat), team != null ? team.getNocId() : null, w);
        writeSelectWithLabel("Sport", "spid", sportMap, spid, true, w);
        writeSelectWithLabel("Gender", "gid", Gender.getGenderLinkedHashMap(), gid, true, w);
        writeSpanWithLabel("Elo", team != null ? Integer.toString(team.getElo()) : "", w);
        w.append("<input type=\"button\" onclick=\"setTeamDescription();\" value=\"Set team description\" /><br/>\n");
    }
}
