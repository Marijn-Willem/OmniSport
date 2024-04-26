package com.sportservlet.ajax;

import com.sports.entity.PersonInstance;
import com.sports.entity.key.PersonInstanceKey;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sports.logic.factory.PersonInstanceFactory;

public class ProcessManagePersonInstance extends ProcessManageEntityInstance<PersonInstanceKey, PersonInstance> {
    @Override
    EntityInstanceFactory<PersonInstanceKey, PersonInstance> getEntityInstanceFactory() {
        return new PersonInstanceFactory();
    }
}
