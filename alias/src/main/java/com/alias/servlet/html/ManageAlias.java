package com.alias.servlet.html;

import com.alias.servlet.util.ServletUtil;
import com.sports.entity.Alias;
import com.sports.entity.Client;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.key.AliasKey;
import com.sports.entity.manager.AliasManager;
import com.sports.entity.manager.ClientManager;
import com.sportservlet.html.ManageEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

public class ManageAlias extends ManageEntity {
    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    protected void preProcessSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        ServletUtil.writeEntityList(getIntValuedParameterValue(req, "aeid"), res.getWriter());
    }

    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int aeid = getIntValuedParameterValue(req, "aeid");
        String eid = req.getParameter("eid");

        return "AliasPortal?aeid=" + aeid + "&eid=" + eid;
    }

    @Override
    protected String getEntityIdName() {
        return "aid";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("alias");
        cssList.add("styling");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeVarInScriptTag("aeid", getIntValuedParameterValue(req, "aeid"), w);
        w.append("const eid = '");
        w.append(req.getParameter("eid"));
        w.append("';\n");
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {

    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        LinkedHashMap<Integer, String> languageMap = getLanguageMap(stat);
        LinkedHashMap<Integer, String> clientMap = new LinkedHashMap<>();
        clientMap.put(0, "-");

        List<Client> clientList = new ClientManager(stat).getClientList();
        clientList.sort(new NamedEntityName());

        for (Client client : clientList)
            clientMap.put(client.getId(), client.getName());

        Alias alias = null;
        boolean isInsert = "i".equals(mode);

        if (!isInsert) {
            int aeid = getIntValuedParameterValue(req, "aeid");
            int aid = getIntValuedParameterValue(req, "aid");
            alias = new AliasManager(stat).getAlias(new AliasKey(aeid, aid));
        }

        Writer w = res.getWriter();

        writeSelectWithLabel("Language", "lid", languageMap, alias != null ? alias.getLanguageId() : null, !isInsert, w);
        writeSelectWithLabel("Client", "cnid", clientMap, alias != null ? alias.getClientId() : null, !isInsert, w);
        writeTextFieldWithLabel("Alias", "al", alias != null ? alias.getAlias() : null, w);
    }
}
