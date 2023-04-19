package com.management.servlet.ajax;

import com.sports.entity.key.ClientCompSeasonKey;
import com.sports.entity.manager.ClientCompSeasonManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.flush.ClientCompSeasonFlusher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessManageClientCompSeasons extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Integer> givenClientIds = new ArrayList<>() {{
            String[] cnIds = req.getParameterValues("cnid");
            if (cnIds != null)
                Arrays.stream(cnIds).forEach(cnId -> add(Integer.parseInt(cnId)));
        }};

        ClientCompSeasonManager ccsm = new ClientCompSeasonManager(stat);

        List<Integer> existingClientIds = ccsm.getClientIdsForCompSeason(compSeasonKey);

        List<Integer> idsToDelete = Util.getElementsLeftNotInRight(existingClientIds, givenClientIds);
        List<Integer> idsToInsert = Util.getElementsLeftNotInRight(givenClientIds, existingClientIds);

        ccsm.deleteClientCompSeasons(getKeys(idsToDelete));
        ccsm.insertClientCompSeasons(getKeys(idsToInsert));

        List<Integer> idsToFlush = new ArrayList<>() {{
            addAll(idsToDelete);
            addAll(idsToInsert);
        }};

        cacheFlusher = new ClientCompSeasonFlusher(idsToFlush);

        resp.getWriter().append("Clients in competition season successfully processed");
    }

    private List<ClientCompSeasonKey> getKeys(List<Integer> idsLeft) {
        return idsLeft.stream().map(id -> new ClientCompSeasonKey(id, competitionId, seasonId))
                .collect(Collectors.toList());
    }
}
