//package com.scholar.dollar.android.dollarscholarbenlewis.model;
//
//import com.google.firebase.database.Exclude;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by benjaminlewis on 12/5/16.
// */
//
//public class College {
//
//    public int id;
//    public String name;
//    public String logoUrl;
//    public String city;
//    public String state;
//    public int ownership;
//    public int tuitionInState;
//    public int tuitionOutState;
//    public int earnings;
//    public Double graduationRate4yr;
//    public Double graduationRate6yr;
//    public int undergrads;
//    public Double admissionRate;
//    public int starCount = 0;
//    public Map<String, Boolean> stars = new HashMap<>();
//
//    public College(int id, String name, String logoUrl, String city, String state, int ownership,
//                       int tuitionInState, int tuitionOutState, int earnings, Double graduationRate4yr,
//                       Double graduationRate6yr, int undergrads, Double admissionRate) {
//        this.id = id;
//        this.name = name;
//        this.logoUrl = logoUrl;
//        this.city = city;
//        this.state = state;
//        this.ownership = ownership;
//        this.tuitionInState = tuitionInState;
//        this.tuitionOutState = tuitionOutState;
//        this.earnings = earnings;
//        this.graduationRate4yr = graduationRate4yr;
//        this.graduationRate6yr = graduationRate6yr;
//        this.undergrads = undergrads;
//        this.admissionRate = admissionRate;
//    }
//
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("id", id);
//        result.put("name", name);
//        result.put("logoUrl", logoUrl);
//        result.put("city", city);
//        result.put("state", state);
//        result.put("ownership", ownership );
//        result.put("tuitionInState", tuitionInState);
//        result.put("tuitionOutState", tuitionOutState);
//        result.put("earnings", earnings);
//        result.put("graduationRate4yr", graduationRate4yr);
//        result.put("graduationRate6yr", graduationRate6yr);
//        result.put("undergrads", undergrads);
//        result.put("admissionRate", admissionRate);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//
//        return result;
//    }
//
//    public Double getGraduationRate4yr() {
//        return graduationRate4yr;
//    }
//
//    public Double getGraduationRate6yr() {
//        return graduationRate6yr;
//    }
//}
