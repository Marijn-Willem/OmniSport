package com.sports.cache.data;

import java.io.Serializable;

public abstract class WritableFragment extends DataFragment implements Serializable {
    public abstract String toXML();
    public abstract String toJson();
}
