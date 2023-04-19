package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientManager extends IntSuperManager<Client> {
    public ClientManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "client";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name", "password", "languageid", "isadmin" };
    }

    @Override
    Client getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();

        client.setId(rs.getInt("id"));
        client.setName(rs.getString("name"));
        client.setPassWord(rs.getString("password"));
        client.setLanguageId(QueryUtil.getIntegerFromResultSet(rs, "languageid"));
        client.setAdmin(rs.getBoolean("isadmin"));

        return client;
    }

    public List<Client> getClientList() throws SQLException {
        return getEntityList(null);
    }

    public List<Client> getAdminClients() throws SQLException {
        return getEntityList("isadmin");
    }

    public Client getClient(String name) throws SQLException {
        return getEntity("name = \"" + name + "\"");
    }

    public List<Client> getClientsFromLanguageIds(List<Integer> languageIds) throws SQLException {
        List<Client> clients = new ArrayList<>();

        if (languageIds.size() > 0)
            clients = getEntityList("languageid IN (" + getCommaSepIntList(languageIds) + ")");

        return clients;
    }
}
