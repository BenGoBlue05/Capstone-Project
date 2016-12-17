package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.scholar.dollar.android.dollarscholarbenlewis.activities.DetailActivity;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CollegeMainFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.model.Place;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlacesService extends IntentService {

    private static final String LOG_TAG = PlacesService.class.getSimpleName();

    public static final String KEYWORD_PARAM = "keyword=";
    public static final String LOCATION_PARAM = "location=";
    public static final String TYPE_PARAM = "type=";
    public static final String API_KEY_PARAM = "key=";
    public static final String AND = "&";

    public static final String PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    public PlacesService(String name) {
        super(name);
    }

    public PlacesService() {
        super(PlacesService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (Utility.ACTION_GET_PLACE_IDS.equals(intent.getAction())){
            List<Place> places = getPlacesWithoutId();
            addPlaceIds(places);
            return;
        }
        int collegeId = intent.getIntExtra(DetailActivity.COLLEGE_ID_KEY, -1);
        Log.i(LOG_TAG, "PLACES_SERVICES STARTED");
        if (collegeId != -1) {
            String name = intent.getStringExtra(DetailActivity.NAME_KEY);
            Log.i(LOG_TAG, name);
            double lat = intent.getDoubleExtra(DetailActivity.LAT_KEY, -1);
            double lon = intent.getDoubleExtra(DetailActivity.LON_KEY, -1);
            try {
                Utility.addPlaceId(getApplicationContext(), lat, lon, name, collegeId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(LOG_TAG, "COLLEGE ID NOT FOUND IN INTENT EXTRA");
        }
    }

    private void addPlaceIds(List<Place> places) {
        for (Place place : places) {
            try {
                Utility.addPlaceId(getApplicationContext(), place.getLat(), place.getLon(),
                        place.getName(), place.getCollegeId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Place> getPlacesWithoutId() {
        List<Place> places = new ArrayList<>();
        Cursor cursor = getContentResolver().query(CollegeContract.PlaceEntry.PLACE_CONTENT_URI, Utility.PLACE_COLUMNS,
                CollegeContract.PlaceEntry.PLACE_ID + "IS NULL OR " + CollegeContract.PlaceEntry.PLACE_ID + " = ?",
                new String[]{""}, CollegeMainFragment.SORT_HIGHEST_EARNINGS);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                int collegeId = cursor.getInt(Utility.COL_COLLEGE_ID);
                double lat = cursor.getDouble(Utility.COL_LAT);
                double lon = cursor.getDouble(Utility.COL_LON);
                String name = cursor.getString(Utility.COL_CSC_NAME);
                places.add(new Place(collegeId, name, lat, lon));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return places;
    }
}
