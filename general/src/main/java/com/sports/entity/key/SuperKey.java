package com.sports.entity.key;

public abstract class SuperKey {
    public abstract String getWhereClause();
    public abstract String getSepValues(String delim);

    public String getCommaSepValues() {
        return getSepValues(", ");
    }

    public String getWhiteSpaceSepValues() {
        return getSepValues("_");
    }

    public String getPipeSepValues() { return getSepValues("|"); }

    public SuperKey getSuperKey() {
        return null;
    }
}
