package com.management.servlet.html;

import com.sports.entity.Client;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.ClientCompSeasonManager;
import com.sports.entity.manager.ClientManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageClientCompSeasons extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("clientcompseason");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonManagementPortal?cid=" + competitionId;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Set<Integer> existingClientIds = new HashSet<>(new ClientCompSeasonManager(stat).getClientIdsForCompSeason(compSeasonKey));

        List<Client> clientList = new ClientManager(stat).getClientList();
        clientList.sort(new NamedEntityName());

        Writer w = res.getWriter();

        for (Client client : clientList) {
            w.append("<input type=\"checkbox\" name=\"cnid\" value=\"");
            w.append(Integer.toString(client.getId()));
            w.append("\"");
            if (existingClientIds.contains(client.getId()))
                w.append(" checked");
            w.append(" />");
            w.append(client.getName());
            w.append("<br/>\n");
        }

        w.append("<input type=\"button\" onclick=\"processClientCompSeasons();\" value=\"Update clients\" /><br/>\n");
        w.append("<div id=\"divRes\"></div>\n");
    }
}
