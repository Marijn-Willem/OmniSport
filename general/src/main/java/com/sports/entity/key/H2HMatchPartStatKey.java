package com.sports.entity.key;

public abstract class H2HMatchPartStatKey extends SuperKey {
    private H2HMatchPartKey h2HMatchPartKey;
    private int specificId;

    H2HMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specificId) {
        this.h2HMatchPartKey = h2HMatchPartKey;
        this.specificId = specificId;
    }

    @Override
    public int hashCode() {
        return 100 * getSuperKey().hashCode() + specificId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof H2HMatchPartStatKey &&
                ((H2HMatchPartStatKey)obj).h2HMatchPartKey.equals(h2HMatchPartKey) &&
                ((H2HMatchPartStatKey)obj).specificId == specificId;
    }

    @Override
    public String getSepValues(String delim) {
        return getSuperKey().getSepValues(delim) + delim + specificId;
    }

    @Override
    public String getWhereClause() {
        return getSuperKey().getWhereClause() + " AND " + getSpecificIdName() + " = " + specificId;
    }

    @Override
    public H2HMatchPartKey getSuperKey() {
        return h2HMatchPartKey;
    }

    abstract String getSpecificIdName();

    public int getSpecificId() {
        return specificId;
    }
}
