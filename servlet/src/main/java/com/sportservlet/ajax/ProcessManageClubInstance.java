package com.sportservlet.ajax;

import com.sports.entity.ClubInstance;
import com.sports.entity.key.ClubInstanceKey;
import com.sports.logic.factory.ClubInstanceFactory;
import com.sports.logic.factory.EntityInstanceFactory;

public class ProcessManageClubInstance extends ProcessManageEntityInstance<ClubInstanceKey, ClubInstance> {
    @Override
    EntityInstanceFactory<ClubInstanceKey, ClubInstance> getEntityInstanceFactory() {
        return new ClubInstanceFactory();
    }
}
