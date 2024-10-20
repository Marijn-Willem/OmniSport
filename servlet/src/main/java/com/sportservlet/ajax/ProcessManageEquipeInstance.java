package com.sportservlet.ajax;

import com.sports.entity.EquipeInstance;
import com.sports.entity.key.EquipeInstanceKey;
import com.sports.logic.factory.EquipeInstanceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class ProcessManageEquipeInstance extends ProcessManageEntityInstance<EquipeInstanceKey, EquipeInstance> {
    @Override
    EquipeInstanceFactory getEntityInstanceFactory() {
        return new EquipeInstanceFactory();
    }

    @Override
    void processSpecificFields(EquipeInstance entityInstance, HttpServletRequest req) { }
}
