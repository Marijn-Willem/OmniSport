package com.sports.entity.manager;

public interface AbstractSuperKeyManager {
    String getIdColumn();

    String[] getGenericColumns();

    String[] getSpecificValueColumns();
}
