package com.sportservlet.html;

import com.sports.entity.PersonInstance;
import com.sports.entity.key.PersonInstanceKey;
import com.sports.logic.factory.PersonInstanceFactory;

import java.io.Writer;
import java.sql.Statement;

public abstract class ManagePersonInstance extends ManageEntityInstance<PersonInstanceKey, PersonInstance> {
    @Override
    PersonInstanceFactory getFactory() {
        return new PersonInstanceFactory();
    }

    @Override
    String getServletNameReturnPath() {
        return "EntityInstancePortalPerson";
    }

    @Override
    void writeSpecificFields(Statement stat, PersonInstance entityInstance, Writer w) { }
}
