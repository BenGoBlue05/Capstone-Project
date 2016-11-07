package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.scholar.dollar.android.dollarscholarbenlewis.BuildConfig;
import com.scholar.dollar.android.dollarscholarbenlewis.activities.DetailActivity;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class PlacesService extends IntentService {

    private static final String LOG_TAG = PlacesService.class.getSimpleName();
    private int mCollegeId;
    private String mName;
    private double mLat;
    private double mLon;

    public static final String KEYWORD_PARAM = "keyword=";
    public static final String LOCATION_PARAM = "location=";
    public static final String TYPE_PARAM = "type=";
    public static final String API_KEY_PARAM = "key=";
    public static final String AND = "&";

    public static final String NEARBY_API_KEY = BuildConfig.NEARBY_API_KEY;

    public static final String PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public PlacesService(String name) {
        super(name);
    }

    public PlacesService() {
        super(PlacesService.class.getSimpleName());
    }


    private void addPlace() throws IOException {
        String url = buildPlaceStringUrl();
        Log.i(LOG_TAG, url);
        String json = Utility.fetch(url);
        Log.i(LOG_TAG, json);
        insertPlace(json);
    }

    private String buildPlaceStringUrl(){
        return PLACES_BASE_URL + LOCATION_PARAM + mLat + "," + mLon + AND + KEYWORD_PARAM +
                mName + AND + TYPE_PARAM + "university" + AND + API_KEY_PARAM + NEARBY_API_KEY;
    }

    public void insertPlace(String json) {
        try {
            JSONObject college = new JSONObject(json)
                    .getJSONArray("results")
                    .getJSONObject(0);
            String placeId = college.getString("place_id");
            String name = college.getString("name");
            Log.i(LOG_TAG, "PLACES ID: " + placeId);
            ContentValues values = new ContentValues();
            values.put(CollegeContract.PlaceEntry.PLACE_ID, placeId);
            values.put(CollegeContract.PlaceEntry.PLACE_NAME, name);
            getContentResolver().update(CollegeContract.PlaceEntry.buildPlaceWithCollegeId(mCollegeId),
                    values, null, null);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON EXCEPTION: " + e);
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mCollegeId = intent.getIntExtra(DetailActivity.COLLEGE_ID_KEY, -1);
        Log.i(LOG_TAG, "PLACES_SERVICES STARTED");
        if (mCollegeId != -1) {
            mName = intent.getStringExtra(DetailActivity.NAME_KEY);
            Log.i(LOG_TAG, mName);
            mLat = intent.getDoubleExtra(DetailActivity.LAT_KEY, -1);
            mLon = intent.getDoubleExtra(DetailActivity.LON_KEY, -1);
            try {
                addPlace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(LOG_TAG, "COLLEGE ID NOT FOUND IN INTENT EXTRA");
        }
    }
}
