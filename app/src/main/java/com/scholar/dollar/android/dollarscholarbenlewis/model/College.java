package com.scholar.dollar.android.dollarscholarbenlewis.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bplewis5 on 10/3/16.
 */
@IgnoreExtraProperties
public class College {
    public String id;
    public String name;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public College() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

}
