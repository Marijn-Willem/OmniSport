package com.sports.cache.data;

import java.io.Serializable;

public abstract class WritableFragment extends DataFragment implements Serializable {
    protected final int nestingLevel;
    protected final boolean isInList;

    public WritableFragment(int nestingLevel, boolean isInList) {
        this.nestingLevel = nestingLevel;
        this.isInList = isInList;
    }

    public abstract String toXML();
    public abstract String toJson();
    public abstract String toYaml();
}
