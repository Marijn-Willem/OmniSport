package com.sportservlet.ajax;

import com.sports.entity.PersonInstance;
import com.sports.entity.key.PersonInstanceKey;
import com.sports.logic.factory.PersonInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManagePersonInstance extends ProcessManageEntityInstance<PersonInstanceKey, PersonInstance> {
    @Override
    PersonInstanceFactory getEntityInstanceFactory() {
        return new PersonInstanceFactory();
    }

    @Override
    void processSpecificFields(PersonInstance entityInstance, HttpServletRequest req) { }
}
