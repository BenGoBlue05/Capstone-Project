package com.scholar.dollar.android.dollarscholarbenlewis.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bplewis5 on 10/3/16.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String photoUrl;
    public int favoritesCount = 0;
    public Map<String, Boolean> favorites = new HashMap<>();

    public User() {
    }

    public User(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("photoUrl", photoUrl);
        result.put("favorites_count", favoritesCount);
        result.put("favorites", favorites);

        return result;
    }

}
