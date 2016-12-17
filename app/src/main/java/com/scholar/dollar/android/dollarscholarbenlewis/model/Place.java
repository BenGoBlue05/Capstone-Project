package com.scholar.dollar.android.dollarscholarbenlewis.model;

/**
 * Created by benjaminlewis on 12/17/16.
 */

public class Place {
    private int collegeId;
    private double lat;
    private double lon;
    private String name;

    public Place(int collegeId, String name, double lat, double lon) {
        this.collegeId = collegeId;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }
}
