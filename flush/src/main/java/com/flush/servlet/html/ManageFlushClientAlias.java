package com.flush.servlet.html;

import com.sports.entity.AliasEntity;
import com.sports.entity.Client;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.AliasEntityManager;
import com.sports.entity.manager.ClientManager;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ManageFlushClientAlias extends ManageFlush {
    @Override
    void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        List<Client> clients = new ClientManager(stat).getClientList();
        List<AliasEntity> aliasEntities = new AliasEntityManager(stat).getAllAliasEntities();

        clients.sort(new NamedEntityName());
        aliasEntities.sort(new NamedEntityName());

        Writer w = res.getWriter();

        w.append("<select id=\"cnid\">\n");

        for (Client client : clients)
            ServletUtil.writeOption(client.getId(), client.getName(), w);

        w.append("</select><br/>\n");

        w.append("<select id=\"aeid\">\n");

        for (AliasEntity aliasEntity : aliasEntities)
            ServletUtil.writeOption(aliasEntity.getId(), aliasEntity.getName(), w);

        w.append("</select><br/>\n");

        w.append("<span>Entity id: <input id=\"eid\" type=\"text\" /></span><br/>\n");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("clientalias");
    }
}
