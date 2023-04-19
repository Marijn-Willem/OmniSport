package com.sports.entity.key;

import com.sports.db.util.QueryUtil;
import com.sports.logic.util.Util;

public class TeamDescriptionSportIdGenderIdKey extends SuperKey {
    private final String description;
    private final int sportId;
    private final int genderId;

    public TeamDescriptionSportIdGenderIdKey(String description, int sportId, int genderId) {
        this.description = description;
        this.sportId = sportId;
        this.genderId = genderId;
    }

    @Override
    public int hashCode() {
        return 100 * description.hashCode() + 10 * sportId + genderId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TeamDescriptionSportIdGenderIdKey &&
                ((TeamDescriptionSportIdGenderIdKey)obj).description.equals(description) &&
                ((TeamDescriptionSportIdGenderIdKey)obj).sportId == sportId &&
                ((TeamDescriptionSportIdGenderIdKey)obj).genderId == genderId;
    }

    public String getWhereClause() {
        return "description = " + QueryUtil.convertStringToDbValue(description) + " AND sportid = " + sportId +
                " AND genderid = " + genderId;
    }

    public String getSepValues(String delim) {
        return Util.concatStrings(new String[] {description, "" + sportId, "" + genderId}, delim);
    }
}
