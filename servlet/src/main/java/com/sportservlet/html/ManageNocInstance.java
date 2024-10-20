package com.sportservlet.html;

import com.sports.entity.NocInstance;
import com.sports.entity.key.NocInstanceKey;
import com.sports.logic.factory.NocInstanceFactory;

import java.io.Writer;
import java.sql.Statement;

public abstract class ManageNocInstance extends ManageEntityInstance<NocInstanceKey, NocInstance> {
    @Override
    NocInstanceFactory getFactory() {
        return new NocInstanceFactory();
    }

    @Override
    String getServletNameReturnPath() {
        return "EntityInstancePortal";
    }

    @Override
    void writeSpecificFields(Statement stat, NocInstance entityInstance, Writer w) { }
}
