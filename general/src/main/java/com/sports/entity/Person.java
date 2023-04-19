package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class Person extends NamedIntEntity {
    private String name;
    private int genderId;
    private Integer geoId;

    private int id;
    private boolean isNewlyCreated;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
            QueryUtil.convertStringToDbValue(name),
            "" + genderId,
            QueryUtil.convertIntegerToDbValue(geoId)
        };
    }

    public Person getCopy() {
        Person person = new Person();
        person.setName(name);
        person.setGenderId(genderId);
        person.setGeoId(geoId);

        return person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public Integer getGeoId() {
        return geoId;
    }

    public void setGeoId(Integer geoId) {
        this.geoId = geoId;
    }

    public boolean isNewlyCreated() {
        return isNewlyCreated;
    }

    public void setNewlyCreated(boolean newlyCreated) {
        isNewlyCreated = newlyCreated;
    }
}
