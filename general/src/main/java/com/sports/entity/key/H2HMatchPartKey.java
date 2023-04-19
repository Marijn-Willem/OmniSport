package com.sports.entity.key;

public abstract class H2HMatchPartKey extends SuperKey implements EntityKeyWithParent {
    private H2HMatchKey h2HMatchKey;
    private int specificId;

    H2HMatchPartKey(H2HMatchKey h2HMatchKey, int specificId) {
        this.h2HMatchKey = h2HMatchKey;
        this.specificId = specificId;
    }

    @Override
    public int hashCode() {
        return 100 * getSuperKey().hashCode() + specificId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof H2HMatchPartKey &&
                ((H2HMatchPartKey)obj).h2HMatchKey.equals(h2HMatchKey) &&
                ((H2HMatchPartKey)obj).specificId == specificId;
    }

    @Override
    public String getSepValues(String delim) {
        return getSuperKey().getSepValues(delim) + delim + specificId;
    }

    @Override
    public String getWhereClause() {
        return getSuperKey().getWhereClause() + " AND " + getSpecificIdName() + " = " + specificId;
    }

    public String getWhereClauseParent() {
        return getSuperKey().getWhereClause() + " AND parentmatchpartid = " + specificId;
    }

    abstract String getSpecificIdName();

    public H2HMatchKey getSuperKey() {
        return h2HMatchKey;
    }

    public int getSpecificId() {
        return specificId;
    }
}
