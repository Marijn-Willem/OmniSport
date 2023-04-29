package com.speedskating.servlet.dispatch;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.DisciplinePartPersonSport;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.comparator.EventDisciplinePartOrder;
import com.sports.entity.key.*;
import com.sports.entity.manager.DisciplinePartPersonSportManager;
import com.sports.entity.manager.EventDisciplinePartManager;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sports.logic.util.Util;
import com.sportservlet.dispatch.SuperDispatchServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AddHeat extends SuperDispatchServlet {
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        int compSeasonEventId = Integer.parseInt(req.getParameter("cseid"));
        int eventPartId = Integer.parseInt(req.getParameter("csepid"));
        int personSport1Id = Integer.parseInt(req.getParameter("p1id"));
        int personSport2Id = Integer.parseInt(req.getParameter("p2id"));

        dispatchURL = "TimeHeat?cid=" + competitionId + "&sid=" + seasonId +
            "&cseid=" + compSeasonEventId + "&csepid=" + eventPartId;

        CompSeasonEventPartKey csepk = getCompSeasonEventPartKey(req);

        DbCalculation dbCalc = new DbCalculation(stat);

        SportDisciplineKey sdk = dbCalc.getSportDisciplineKey(csepk);

        EventPartPersonSportManager eppm = new EventPartPersonSportManager(stat);
        int heat = eppm.getNewHeat(csepk);

        EventPartPersonSport epp1 = new EventPartPersonSport(), epp2 = new EventPartPersonSport();

        epp1.setHeat(heat);
        epp2.setHeat(heat);

        EventPartPersonSportKey eppk1 = new EventPartPersonSportKey(csepk, personSport1Id),
                eppk2 = new EventPartPersonSportKey(csepk, personSport2Id);

        List<EventDisciplinePart> eventDisciplineParts = new EventDisciplinePartManager(stat).getEventDisciplineList(csepk);
        dbCalc.fillDisciplinePartsForEventDisciplineParts(sdk, eventDisciplineParts);
        eventDisciplineParts.sort(new EventDisciplinePartOrder());
        DisciplinePartPersonSportManager dppm = new DisciplinePartPersonSportManager(stat);

        for (int i = 0; i < eventDisciplineParts.size(); i++) {
            EventDisciplinePart eventDisciplinePart = eventDisciplineParts.get(i);
            int edpId = eventDisciplinePart.getEventDisciplinePartId();

            int points1 = Util.getMillisFromTimeString(req.getParameter("p1_" + edpId));
            int points2 = Util.getMillisFromTimeString(req.getParameter("p2_" + edpId));

            if (i == eventDisciplineParts.size() - 1) {
                epp1.setPoints(points1);
                epp2.setPoints(points2);

                eppm.updateEventPartPersonSport(eppk1, epp1);
                eppm.updateEventPartPersonSport(eppk2, epp2);
            }

            DisciplinePartPersonSportKey dppk1 = new DisciplinePartPersonSportKey(eppk1, edpId);
            DisciplinePartPersonSportKey dppk2 = new DisciplinePartPersonSportKey(eppk2, edpId);

            DisciplinePartPersonSport dpp1 = new DisciplinePartPersonSport();
            dpp1.setPoints(points1);

            DisciplinePartPersonSport dpp2 = new DisciplinePartPersonSport();
            dpp2.setPoints(points2);

            dppm.addDisciplinePartPerson(dppk1, dpp1);
            dppm.addDisciplinePartPerson(dppk2, dpp2);
        }
    }
}
