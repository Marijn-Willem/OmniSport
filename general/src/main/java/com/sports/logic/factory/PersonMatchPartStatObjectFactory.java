package com.sports.logic.factory;

import com.sports.entity.PersonMatchPartStat;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.key.PersonMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartStatManager;
import com.sports.entity.manager.PersonMatchPartStatManager;

import java.sql.Statement;

public class PersonMatchPartStatObjectFactory implements H2HPartStatObjectFactory<PersonMatchPartStatKey, PersonMatchPartStat> {
    public H2HMatchPartStatManager<PersonMatchPartStatKey, PersonMatchPartStat> getStatManager(Statement stat) {
        return new PersonMatchPartStatManager(stat);
    }

    public PersonMatchPartStatKey getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId) {
        return new PersonMatchPartStatKey((PersonMatchPartKey)h2HMatchPartKey, specifId);
    }

    public PersonMatchPartStat getMatchPartStat() {
        return new PersonMatchPartStat();
    }
}
