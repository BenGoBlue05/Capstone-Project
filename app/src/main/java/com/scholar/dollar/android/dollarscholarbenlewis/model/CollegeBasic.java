package com.scholar.dollar.android.dollarscholarbenlewis.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bplewis5 on 10/3/16.
 */
@IgnoreExtraProperties
public class CollegeBasic {
    public String name;
    public String placeId = "na";
    public String nickname;
    public String primaryColor = "na";
    public String secondaryColor = "na";
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public CollegeBasic() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public CollegeBasic(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("placeId", placeId);
        result.put("nickname", nickname);
        result.put("primaryColor", primaryColor);
        result.put("secondaryColor", secondaryColor);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

}
