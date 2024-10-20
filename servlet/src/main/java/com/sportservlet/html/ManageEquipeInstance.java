package com.sportservlet.html;

import com.sports.entity.EquipeInstance;
import com.sports.entity.key.EquipeInstanceKey;
import com.sports.logic.factory.EquipeInstanceFactory;

import java.io.Writer;
import java.sql.Statement;

public abstract class ManageEquipeInstance extends ManageEntityInstance<EquipeInstanceKey, EquipeInstance> {
    @Override
    EquipeInstanceFactory getFactory() {
        return new EquipeInstanceFactory();
    }

    @Override
    String getServletNameReturnPath() {
        return "EntityInstancePortal";
    }

    @Override
    void writeSpecificFields(Statement stat, EquipeInstance entityInstance, Writer w) { }
}
