package com.sports.logic.factory;

import com.sports.entity.PersonMatchPart;
import com.sports.entity.PersonMatchPartStat;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.key.PersonMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartManager;
import com.sports.entity.manager.PersonMatchPartManager;

import java.sql.Statement;

public class PersonMatchPartObjectFactory implements H2HPartObjectFactory<PersonMatchPartKey, PersonMatchPart, PersonMatchPartStatKey, PersonMatchPartStat> {
    public H2HMatchPartManager<PersonMatchPartKey, PersonMatchPart> getMatchPartManager(Statement stat) {
        return new PersonMatchPartManager(stat);
    }

    public PersonMatchPartKey getMatchPartKey(H2HMatchKey h2HMatchKey, int specifId) {
        return new PersonMatchPartKey((PersonMatchKey)h2HMatchKey, specifId);
    }

    public PersonMatchPart getMatchPart() {
        return new PersonMatchPart();
    }

    public PersonMatchPartStatObjectFactory getPartStatObjectFactory() {
        return new PersonMatchPartStatObjectFactory();
    }
}
