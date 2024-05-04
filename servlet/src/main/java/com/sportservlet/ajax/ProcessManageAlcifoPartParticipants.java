package com.sportservlet.ajax;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.AlcifoParticipantKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.NoCountResultManager;
import com.sports.entity.manager.SportDisciplineManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ProcessManageAlcifoPartParticipants<PK extends SuperKey, APP extends AlcifoPartParticipant> extends SuperResponseServlet {
    abstract PK getPartKey(HttpServletRequest req);
    abstract AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends SuperKeyEntity,
            ? extends Participant,
            ? extends AlcifoParticipantKey,
            ? extends AlcifoParticipant,
            ? extends SuperKey,
            PK,
            APP,
            ? extends SuperKey,
            ? extends AlcifoPartParticipant> getFactory();

    protected void postProcessInsertOrUpdate(Statement stat, SuperKey partKey) throws SQLException { }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        PK partKey = getPartKey(req);
        String[] personSportData = req.getParameterValues("pt");

        AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends SuperKeyEntity,
                ? extends Participant,
                ? extends AlcifoParticipantKey,
                ? extends AlcifoParticipant,
                ? extends SuperKey,
                PK,
                APP,
                ? extends SuperKey,
                ? extends AlcifoPartParticipant> factory = getFactory();

        CompSeasonEventPartKey csepKey = getCompSeasonEventPartKey(req);
        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getEntityFromSuperKey(csepKey);

        SportDiscipline sd = new SportDisciplineManager(stat).getEntityFromSuperKey(
                compSeasonEventPart.getSportDisciplineKey());

        Map<String, Integer> ncrMap = new NoCountResultManager(stat).getNameIdMap();

        List<APP> partParticipants = Arrays.stream(personSportData)
                .map(pd -> getParticipant(pd, sd.getResultTypeId(), ncrMap, factory))
                .collect(Collectors.toList());

        new DbCalculation(stat).updatePartParticipants(partKey, partParticipants, factory);
        postProcessInsertOrUpdate(stat, partKey);

        resp.getWriter().append("Participants updated");
    }

    private APP getParticipant(String data, int resultTypeId, Map<String, Integer> ncrMap,
                                                 AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
                                                         ? extends SuperKeyEntity,
                                                         ? extends Participant,
                                                         ? extends AlcifoParticipantKey,
                                                         ? extends AlcifoParticipant,
                                                         ? extends SuperKey,
                                                         PK,
                                                         APP,
                                                         ? extends SuperKey,
                                                         ? extends AlcifoPartParticipant> factory) {
        String[] dataSplit = data.split("\\|");
        APP partParticipant = factory.getInstance();
        partParticipant.setParticipantId(Integer.parseInt(dataSplit[0]));

        partParticipant.setRank(dataSplit.length > 1 ? Util.convertStringToInteger(dataSplit[1]) : null);
        partParticipant.setPoints(dataSplit.length > 2 ? getPoints(dataSplit[2], resultTypeId) : null);
        partParticipant.setNoCountResultId(dataSplit.length > 3 ? ncrMap.get(dataSplit[3]) : null);

        return partParticipant;
    }

    private Integer getPoints(String pointsData, int resultTypeId) {
        if (!Util.isEmptyString(pointsData) && resultTypeId == ResultType.resultTypeIdTime)
            return Util.getMillisFromHMSString(pointsData);

        return Util.convertStringToInteger(pointsData);
    }
}
