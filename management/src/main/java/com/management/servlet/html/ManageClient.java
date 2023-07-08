package com.management.servlet.html;

import com.sports.entity.Client;
import com.sports.entity.manager.ClientManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

public class ManageClient extends ManageEntity {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "ClientPortal";
    }

    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("client");
        cssList.add("styling");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected String getEntityIdName() {
        return "cnid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        LinkedHashMap<Integer, String> languageMap = getLanguageMap(stat);

        ClientManager cm = new ClientManager(stat);

        Client client = null;

        if (!"i".equals(mode)) {
            int cnid = getIntValuedParameterValue(req, "cnid");
            client = cm.getEntityFromId(cnid);
        }

        Writer w = res.getWriter();

        writeTextFieldWithLabel("Name", "nm", client != null ? client.getName() : null, w);
        writeSelectWithLabel("Language", "lid", languageMap, client != null ? client.getLanguageId() : null, w);
        writeCheckbox("Admin", "ia", client != null && client.isAdmin(), w);
        writePasswordField(client != null ? client.getPassWord() : null, w);
    }
}
