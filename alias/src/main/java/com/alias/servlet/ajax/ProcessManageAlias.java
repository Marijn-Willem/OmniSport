package com.alias.servlet.ajax;

import com.sports.entity.Alias;
import com.sports.entity.key.AliasKey;
import com.sports.entity.manager.AliasManager;
import com.sports.entity.manager.SuperKeySuperManager;
import com.sportservlet.ajax.ProcessManageSuperKeyEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageAlias extends ProcessManageSuperKeyEntity<AliasKey, Alias> {
    @Override
    protected SuperKeySuperManager<AliasKey, Alias> getSuperManager(Statement stat) {
        return new AliasManager(stat);
    }

    @Override
    protected AliasKey getNewSuperKey(SuperKeySuperManager<AliasKey, Alias> superManager, HttpServletRequest req)
            throws SQLException {
        int aeid = getIntValuedParameterValue(req, "aeid");
        int aid = ((AliasManager)superManager).getNewAliasId(aeid);

        return new AliasKey(aeid, aid);
    }

    @Override
    protected String getUpdateIdStr(AliasKey superKey) {
        return Integer.toString(superKey.getAliasId());
    }

    @Override
    protected AliasKey getSuperKeyFromRequest(HttpServletRequest req) {
        int aeid = getIntValuedParameterValue(req, "aeid");
        int aid = getIntValuedParameterValue(req, "aid");

        return new AliasKey(aeid, aid);
    }

    @Override
    protected Alias getNewEntity() {
        return new Alias();
    }

    @Override
    protected void processEntityFromRequest(Statement stat, HttpServletRequest req) {
        String eid = req.getParameter("eid");
        Integer lid = convertRequestParamToIdInteger(req, "lid");
        Integer cnid = convertRequestParamToIdInteger(req, "cnid");
        String al = req.getParameter("al");

        entity.setEntityId(eid);
        entity.setLanguageId(lid);
        entity.setClientId(cnid);
        entity.setAlias(al);
    }
}
