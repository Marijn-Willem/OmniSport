package com.sportservlet.ajax;

import com.sports.entity.NocInstance;
import com.sports.entity.key.NocInstanceKey;
import com.sports.logic.factory.EntityInstanceFactory;
import com.sports.logic.factory.NocInstanceFactory;

public class ProcessManageNocInstance extends ProcessManageEntityInstance<NocInstanceKey, NocInstance> {
    @Override
    EntityInstanceFactory<NocInstanceKey, NocInstance> getEntityInstanceFactory() {
        return new NocInstanceFactory();
    }
}
