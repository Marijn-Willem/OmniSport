package com.alias.servlet.ajax;

import com.sports.entity.Alias;
import com.sports.entity.Client;
import com.sports.entity.Language;
import com.sports.entity.comparator.AliasListDisplayText;
import com.sports.entity.key.AliasEntityIdKey;
import com.sports.entity.manager.AliasManager;
import com.sports.entity.manager.ClientManager;
import com.sports.entity.manager.LanguageManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliasList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        Map<Integer, Language> languageMap = new LanguageManager(stat).getLanguageMap();
        List<Client> clientList = new ClientManager(stat).getClientList();

        Map<Integer, Client> clientMap = new HashMap<>();
        for (Client client : clientList)
            clientMap.put(client.getId(), client);

        int aeid = Integer.parseInt(req.getParameter("aeid"));
        String eid = req.getParameter("eid");

        List<Alias> aliasList = new AliasManager(stat).getAliasListFromEntityId(new AliasEntityIdKey(aeid, eid));

        for (Alias alias : aliasList) {
            String displayPrefix;
            String displayPostfix;

            if (alias.getLanguageId() != null) {
                displayPrefix = "Language";
                displayPostfix = languageMap.get(alias.getLanguageId()).getName();
            }
            else {
                displayPrefix = "Client";
                displayPostfix = clientMap.get(alias.getClientId()).getName();
            }

            alias.setListDisplayText(Util.concatStringsWithDelimiter(displayPrefix, displayPostfix, " - "));
        }

        aliasList.sort(new AliasListDisplayText());

        for (Alias alias : aliasList)
            ServletUtil.writeOption(alias.getAliasId(), alias.getListDisplayText(), resp.getWriter());
    }
}
