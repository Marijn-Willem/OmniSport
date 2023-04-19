package com.management.servlet.ajax;

import com.sports.entity.Client;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.ClientManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ClientList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Client> clients = new ClientManager(stat).getClientList();
        clients.sort(new NamedEntityName());

        for (Client client : clients)
            ServletUtil.writeOption(client.getId(), client.getName(), resp.getWriter());
    }
}
