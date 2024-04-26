package com.sportservlet.ajax;

import com.sports.entity.EquipeInstance;
import com.sports.entity.key.EquipeInstanceKey;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sports.logic.factory.EquipeInstanceFactory;

public class ProcessManageEquipeInstance extends ProcessManageEntityInstance<EquipeInstanceKey, EquipeInstance> {
    @Override
    EntityInstanceFactory<EquipeInstanceKey, EquipeInstance> getEntityInstanceFactory() {
        return new EquipeInstanceFactory();
    }
}
