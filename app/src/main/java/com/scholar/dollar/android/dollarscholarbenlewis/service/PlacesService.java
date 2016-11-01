package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.activities.DetailActivity;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class PlacesService extends IntentService {

    private static final String LOG_TAG = PlacesService.class.getSimpleName();
    private int mCollegeId;
    private String mName;
    private double mLat;
    private double mLon;

    public static final String KEYWORD_PARAM = "keyword";
    public static final String LOCATION_PARAM = "location";
    public static final String TYPE_PARAM = "type";
    public static final String API_KEY_PARAM = "key";

    public static final String PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public PlacesService(String name) {
        super(name);
    }

    public PlacesService() {
        super(PlacesService.class.getSimpleName());
    }


    private void addPlace() {
        Uri uri = buildPlaceUri();
        if (uri != null) {
            try {
                String json = CollegeService.fetch(uri.toString());
                insertPlace(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Uri buildPlaceUri() {
        try {
            String keyword = URLEncoder.encode(mName, "utf-8");
            String apiKey = getApplicationContext().getString(R.string.places_api_key);
            return Uri.parse(PLACES_BASE_URL).buildUpon()
                    .appendQueryParameter(LOCATION_PARAM, "" + mLat + "," + mLon)
                    .appendQueryParameter(KEYWORD_PARAM, keyword)
                    .appendQueryParameter(TYPE_PARAM, "university")
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "BUILD_PLACE_URL RETURNED NULL");
        return null;
    }

    public void insertPlace(String json) {
        try {
            String placeId = new JSONObject(json)
                    .getJSONArray("results")
                    .getJSONObject(0)
                    .getString("place_id");
            ContentValues values = new ContentValues();
            values.put(CollegeContract.PlaceEntry.PLACE_ID, placeId);
            getContentResolver().update(CollegeContract.PlaceEntry.buildPlaceWithCollegeId(mCollegeId),
                    values, null, null);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON EXCEPTION: " + e);
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mCollegeId = intent.getIntExtra("collegeIdKey", -1);
        if (mCollegeId != -1) {
            mName = intent.getStringExtra(DetailActivity.NAME_KEY);
            mLat = intent.getDoubleExtra(DetailActivity.LAT_KEY, -1);
            mLon = intent.getDoubleExtra(DetailActivity.LON_KEY, -1);
            addPlace();
        } else {
            Log.i(LOG_TAG, "COLLEGE ID NOT FOUND IN INTENT EXTRA");
        }

    }
}
