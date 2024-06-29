package com.cyclingroad.servlet.html;

import com.sports.entity.PersonSport;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonTeamPersonSportKey;
import com.sports.entity.manager.CompSeasonPersonSportManager;
import com.sports.entity.manager.CompSeasonTeamPersonSportManager;
import com.sports.entity.manager.PersonSportManager;
import com.sports.logic.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ManageCompSeasonTeamPersonSports extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("compseasonteampersonsport");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("tid", getIntValuedParameterValue(req, "tid"), w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonTeamPortal?" + compSeasonUrlParameters + "&tid=" +
                getIntValuedParameterValue(req, "tid");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int tid = getIntValuedParameterValue(req, "tid");

        CompSeasonTeamPersonSportManager cstpsm = new CompSeasonTeamPersonSportManager(stat);
        List<CompSeasonTeamPersonSportKey> keysCompSeason = cstpsm.getKeysForCompSeason(compSeasonKey);
        List<Integer> existingPsIds = new ArrayList<>();
        boolean teamHasParticipants = false;

        for (CompSeasonTeamPersonSportKey key : keysCompSeason)
            if (key.getSuperKey().getSpecificId() == tid) {
                teamHasParticipants = true;
                break;
            }
            else
                existingPsIds.add(key.getPersonSportId());

        if (!teamHasParticipants) {
            List<Integer> allPsIds = new CompSeasonPersonSportManager(stat).getParticipantIdsCompSeason(compSeasonKey);
            List<Integer> psIdsToShow = Util.getElementsLeftNotInRight(allPsIds, existingPsIds);

            List<PersonSport> personSports = new PersonSportManager(stat).getParticipantList(psIdsToShow);
            personSports.sort(new DescribedEntityDescription());

            Writer w = res.getWriter();

            for (PersonSport personSport : personSports) {
                w.append("<input type=\"checkbox\" name=\"psid\" value=\"");
                w.append(Integer.toString(personSport.getId()));
                w.append("\" />");
                w.append(personSport.getDescription());
                w.append("<br/>\n");
            }

            w.append("<input type=\"button\" onclick=\"addCompSeasonTeamPersonSports();\" value=\"Add persons to team\" />\n");
            w.append("<div id=\"divRes\"></div>\n");
        }
        else
            dispatchToReturnPath(req, res);
    }
}
