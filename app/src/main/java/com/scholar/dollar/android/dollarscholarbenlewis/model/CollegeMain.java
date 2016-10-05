package com.scholar.dollar.android.dollarscholarbenlewis.model;

/**
 * Created by bplewis5 on 10/3/16.
 */

public class CollegeMain {
    private String id;
    private String name;
    private String logoUrl;
    private String city;
    private String state;
    private int ownership;
    private int tuitionInState;
    private int tuitionOutState;
    private int earnings;
    private double graduationRate;

    public CollegeMain(String id, String name, String logoUrl, String city, String state,
                       int ownership, int tuitionInState, int tuitionOutState, int earnings, double graduationRate) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.city = city;
        this.state = state;
        this.ownership = ownership;
        this.tuitionInState = tuitionInState;
        this.tuitionOutState = tuitionOutState;
        this.earnings = earnings;
        this.graduationRate = graduationRate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getOwnership() {
        return ownership;
    }

    public int getTuitionInState() {
        return tuitionInState;
    }

    public int getTuitionOutState() {
        return tuitionOutState;
    }

    public int getEarnings() {
        return earnings;
    }

    public double getGraduationRate() {
        return graduationRate;
    }
}
