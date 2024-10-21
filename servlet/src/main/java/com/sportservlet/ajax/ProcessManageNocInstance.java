package com.sportservlet.ajax;

import com.sports.entity.NocInstance;
import com.sports.entity.key.NocInstanceKey;
import com.sports.logic.factory.NocInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ProcessManageNocInstance extends ProcessManageEntityInstance<NocInstanceKey, NocInstance> {
    @Override
    NocInstanceFactory getEntityInstanceFactory() {
        return new NocInstanceFactory();
    }

    @Override
    void processSpecificFields(Statement stat, NocInstance entityInstance, HttpServletRequest req) { }
}
