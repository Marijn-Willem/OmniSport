package com.sports.entity;

import com.sports.db.type.Point;
import com.sports.db.util.QueryUtil;

public class Geo extends NamedIntEntity {
    public static final int geoIdUSA = 27;

    private String name;
    private int geoTypeId;
    private Integer parentGeoId;
    private Point coordinates;

    private int id;
    private String outputString;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                "" + geoTypeId,
                QueryUtil.convertIntegerToDbValue(parentGeoId),
                QueryUtil.convertPointToDbValue(coordinates)
            };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeoTypeId() {
        return geoTypeId;
    }

    public void setGeoTypeId(int geoTypeId) {
        this.geoTypeId = geoTypeId;
    }

    public Integer getParentGeoId() {
        return parentGeoId;
    }

    public void setParentGeoId(Integer parentGeoId) {
        this.parentGeoId = parentGeoId;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOutputString() {
        return outputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }
}
