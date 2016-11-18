package com.scholar.dollar.android.dollarscholarbenlewis.model;

/**
 * Created by bplewis5 on 10/3/16.
 */

public class CollegeMain {
    private int id;
    private String name;
    private String logoUrl;
    private String city;
    private String state;
    private int ownership;
    private int tuitionInState;
    private int tuitionOutState;
    private int earnings;
    private double graduationRate4yr;
    private double graduationRate6yr;
    private int undergrads;
    private double latitude;
    private double longitude;
    private int localeCode;
    private double admissionRate;

    public CollegeMain(int id, String name, String logoUrl, String city, String state,
                       int ownership, int tuitionInState, int tuitionOutState, int earnings,
                       double graduationRate4yr, double graduationRate6yr, int undergrads,
                       double latitude, double longitude, int localeCode, double admissionRate) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.city = city;
        this.state = state;
        this.ownership = ownership;
        this.tuitionInState = tuitionInState;
        this.tuitionOutState = tuitionOutState;
        this.earnings = earnings;
        this.graduationRate4yr = graduationRate4yr;
        this.graduationRate6yr = graduationRate6yr;
        this.undergrads = undergrads;
        this.latitude = latitude;
        this.longitude = longitude;
        this.localeCode = localeCode;
        this.admissionRate = admissionRate;

    }

    public int getId() {
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

    public double getGraduationRate6yr() {
        return graduationRate6yr;
    }

    public double getGraduationRate4yr() {
        return graduationRate4yr;
    }

    public int getUndergradSize() {
        return undergrads;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getLocaleCode() {
        return localeCode;
    }

    public double getAdmissionRate() {
        return admissionRate;
    }
}
