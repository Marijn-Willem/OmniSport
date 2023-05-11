package com.speedskating.servlet.html;

import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.DisciplinePart;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.PersonSport;
import com.sports.entity.comparator.DisciplinePartOrder;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimeHeat extends SuperHtmlServlet {
    @Override
    void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("timeheat");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        int cseid = getIntValuedParameterValue(req, "cseid");
        return "EventHeats?" + compSeasonUrlParameters + "&cseid=" + cseid;
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonEventPartVarsInScriptTag(req, w);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        CompSeasonEventPartKey csepk = getCompSeasonEventPartKey(req);

        EventPartPersonSportManager eppm = new EventPartPersonSportManager(stat);

        List<Integer> personSportIds = eppm.getPersonSportIdsCompSeasonEventPartNoHeat(csepk);

        PersonSportManager psm = new PersonSportManager(stat);
        List<PersonSport> personList = psm.getParticipantList(personSportIds);

        Writer w = res.getWriter();

        String line = "<table border=\"1\">\n";

        w.append(line);

        String select1 = getPersonSelect(personList, "1");
        String select2 = getPersonSelect(personList, "2");

        line = "<tr><td>" + select1 + "</td>" +
                "<td><input id=\"tmr\" type=\"text\" /></td>" +
                "<td>" + select2 + "</td></tr>\n";

        w.append(line);

        line = "<tr><td><input id=\"btnLap1\" type=\"button\" value=\"Lap\" " +
                "style=\"display:none\" onclick=\"newLap(1);\" /></td>" +
                "<td><input type=\"button\" value=\"Start\" onclick=\"startTimer();\" /></td>" +
                "<td><input id=\"btnLap2\" type=\"button\" value=\"Lap\" " +
                "style=\"display:none\" onclick=\"newLap(2);\" /></td></tr>\n";

        w.append(line);

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getEntityFromSuperKey(csepk);
        SportDisciplineKey sdk = compSeasonEventPart.getSportDisciplineKey();
        List<DisciplinePart> disciplinePartList = new ArrayList<>() {{
            addAll(new DisciplinePartManager(stat).getDisciplinePartList(sdk));
        }};

        disciplinePartList.sort(new DisciplinePartOrder());

        List<EventDisciplinePart> eventDisciplineParts = new EventDisciplinePartManager(stat).getEventDisciplineList(csepk);

        int order = 0;

        for (int i = 0; i < disciplinePartList.size(); i++) {
            DisciplinePart disciplinePart = disciplinePartList.get(i);
            String cssClass = i == disciplinePartList.size() - 1 ? "fin" : "interm";

            int eventDisciplinePartId = getEventDisciplinePartId(eventDisciplineParts,
                    disciplinePart.getDisciplinePartId());

            line = "<tr class=\"" + cssClass + "\"><td>" + getInput("p1", order, eventDisciplinePartId) +
                    "</td><td>" + disciplinePart.getName() + "</td><td>" +
                    getInput("p2", order++, eventDisciplinePartId) + "</td></tr>\n";

            w.append(line);
        }

        w.append("</table>\n");

        w.append("<input id=\"inp_sbt\" style=\"display:none\" type=\"submit\" onclick=\"submit();\" value=\"Submit\" /><br/>\n");
        w.append("""
                <input type="submit" onclick="submitFinishTimes();" value="Submit only finish times" />
                <br/>
                """);
    }

    private String getPersonSelect(List<PersonSport> personSportList, String plNr) {
        StringBuilder personSelect = new StringBuilder("<select name=\"p" + plNr + "id\" " +
                "onchange=\"processPersonSelect(" + plNr + ");\">\n");

        personSelect.append("<option value=\"0\">-</option>\n");

        for (PersonSport personSport : personSportList) {
            personSelect.append("<option value=\"");
            personSelect.append(personSport.getId());
            personSelect.append("\">");
            personSelect.append(personSport.getDescription());
            personSelect.append("</option>\n");
        }

        personSelect.append("</select>\n");

        return personSelect.toString();
    }

    private String getInput(String personStr, int order, int disciplinePartId) {
        return "<input id=\"" + personStr + "_" + order + "\" type=\"text\" class=\"time\" " +
                "name=\"" + personStr + "_" + disciplinePartId + "\" />";
    }

    private int getEventDisciplinePartId(List<EventDisciplinePart> eventDisciplineParts, int disciplinePartId) {
        for (EventDisciplinePart eventDisciplinePart : eventDisciplineParts)
            if (eventDisciplinePart.getDisciplinePartId() == disciplinePartId)
                return eventDisciplinePart.getEventDisciplinePartId();

        return 0;
    }
}
