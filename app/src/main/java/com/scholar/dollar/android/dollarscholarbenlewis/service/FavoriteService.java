package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.model.CollegeMain;
import com.scholar.dollar.android.dollarscholarbenlewis.model.User;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import static com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeService.IN_STATE_TUITION_AND_FEES;
import static com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeService.OUT_STATE_TUITION_AND_FEES;


public class FavoriteService extends IntentService {

    private static final String LOG_TAG = FavoriteService.class.getSimpleName();

    public FavoriteService(String name) {
        super(name);
    }

    public FavoriteService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "FAVORITES INTENT CALLED");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String id = user.getUid();
            Log.i(LOG_TAG, "USER ID: " + user.getUid());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(id);
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    User user = mutableData.getValue(User.class);
                    if (user == null) {
                        return Transaction.success(mutableData);
                    }
                    List<String> fbFavs;
                    List<String> fbFavsNotStoredLocal;
                    if (user.favoritesCount > 0) {
                        Log.i(LOG_TAG, "FAVORITES COUNT: " + user.favoritesCount);
                        Set<String> favsSet = user.favorites.keySet();
                        fbFavs = new ArrayList<>(favsSet);
                        fbFavsNotStoredLocal = new ArrayList<>(favsSet);
                        Log.i(LOG_TAG, "FB FAVORITES: " + fbFavs.toString());
                        Cursor cursor = getContentResolver().query(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI,
                                new String[]{String.valueOf(CollegeContract.CollegeMainEntry.COLLEGE_ID)},
                                CollegeContract.CollegeMainEntry.IS_FAVORITE + " = ? ", new String[]{"1"}, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToPosition(-1);
                            while (cursor.moveToNext()) {
                                String id = String.valueOf(cursor.getInt(0));
                                if (!fbFavs.contains(id)) {
                                    ContentValues values = new ContentValues();
                                    values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, 0);
                                    getContentResolver().update(CollegeContract.CollegeMainEntry
                                            .buildMainWithCollegeId(Integer.parseInt(id)), values, null, null);
                                } else {
                                    fbFavsNotStoredLocal.remove(id);
                                }
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        if (fbFavsNotStoredLocal.size() > 0) {
                            String url = Utility.BASE_URL + createFilter(fbFavsNotStoredLocal) + CollegeService.FIELDS_PARAMS;
                            Log.i(LOG_TAG, url);
                            try {
                                ArrayList<CollegeMain> colleges = getCollegeInfo(url);
                                addColleges(colleges);
                            } catch (NullPointerException e) {
                                Log.e(LOG_TAG, "NULL EXCEPTION");
                            }
                        }

                    } else {
                        Cursor cursor = getContentResolver().query(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI,
                                new String[]{String.valueOf(CollegeContract.CollegeMainEntry.COLLEGE_ID)},
                                CollegeContract.CollegeMainEntry.IS_FAVORITE + " = ? ", new String[]{"1"}, null);
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToPosition(-1);
                            while (cursor.moveToNext()) {
                                String id = String.valueOf(cursor.getInt(0));
                                ContentValues values = new ContentValues();
                                values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, 0);
                                getContentResolver().update(CollegeContract.CollegeMainEntry
                                        .buildMainWithCollegeId(Integer.parseInt(id)), values, null, null);
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }


                    mutableData.setValue(user);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Log.d(LOG_TAG, "postTransaction:onComplete:" + databaseError);
                }
            });

        }

    }

    public ArrayList<CollegeMain> getCollegeInfo(String url) {
        try {
            String jsonStr = Utility.fetch(url);
            JSONObject results = new JSONObject(jsonStr);
            JSONArray colleges = results.getJSONArray("results");
            int collegesLength;
            if (colleges != null) {
                collegesLength = colleges.length();
            } else {
                Log.i(LOG_TAG, "ARRAY IS NULL");
                return null;
            }
            ArrayList<CollegeMain> collegesInfo = new ArrayList<>();
            for (int i = 0; i < collegesLength; i++) {
                JSONObject college = colleges.getJSONObject(i);
                int id = college.getInt(CollegeService.ID);
                String name = college.getString(CollegeService.NAME);
                String domainUrl = college.getString(CollegeService.SCHOOL_URL);
                String logoUrl = getLogo(domainUrl);
//                Log.i(LOG_TAG, logoUrl);

                String city = college.getString(CollegeService.CITY);
                String state = college.getString(CollegeService.STATE);
                int earnings = college.getInt(CollegeService.MED_EARNINGS_10_YEARS);
                int tuitionInState = college.getInt(IN_STATE_TUITION_AND_FEES);
                int tuitionOutState = college.getInt(OUT_STATE_TUITION_AND_FEES);
                int ownership = college.getInt(CollegeService.OWNERSHIP);
                int undergradSize = college.getInt(CollegeService.UNDERGRAD_SIZE);
                double lat = college.getDouble(CollegeService.LATITUDE);
                double lon = college.getDouble(CollegeService.LONGITUDE);
                int locale = college.getInt(CollegeService.LOCALE);
                CollegeMain collegeMain = new CollegeMain(id, name, logoUrl, city, state, ownership,
                        tuitionInState, tuitionOutState, earnings,
                        handleNullDouble(college.get(CollegeService.GRADUATION_RATE_4_YEARS)),
                        handleNullDouble(college.get(CollegeService.GRADUATION_RATE_6_YEARS)),
                        undergradSize, lat, lon, locale,
                        handleNullDouble(college.get(CollegeService.ADMISSION_RATE)));
                collegesInfo.add(collegeMain);
            }
            return collegesInfo;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Double handleNullDouble(Object o) {
        if (o == null || o.toString().contains("null")) {
            return null;
        }
        try {
            return (Double) o;
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, o.toString());
            Log.e(LOG_TAG, "CLASS CAST EXCEPTION: " + e);
            return null;
        }
    }

    public void addColleges(ArrayList<CollegeMain> colleges) {
        Vector<ContentValues> cvVector = new Vector<>();
        Vector<ContentValues> placeVector = new Vector<>();
        Cursor cursor = null;
        for (CollegeMain college : colleges) {
            Uri uri = CollegeContract.CollegeMainEntry.buildMainWithCollegeId(college.getId());
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                ContentValues placeValues = new ContentValues();

                int collegeId = college.getId();
                String name = college.getName();
                values.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, collegeId);
                values.put(CollegeContract.CollegeMainEntry.NAME, name);
                values.put(CollegeContract.CollegeMainEntry.LOGO_URL, college.getLogoUrl());
                values.put(CollegeContract.CollegeMainEntry.CITY, college.getCity());
                values.put(CollegeContract.CollegeMainEntry.STATE, college.getState());
                values.put(CollegeContract.CollegeMainEntry.OWNERSHIP, college.getOwnership());
                values.put(CollegeContract.CollegeMainEntry.TUITION_IN_STATE, college.getTuitionInState());
                values.put(CollegeContract.CollegeMainEntry.TUITION_OUT_STATE, college.getTuitionOutState());
                values.put(CollegeContract.CollegeMainEntry.MED_EARNINGS_2012, college.getEarnings());
                values.put(CollegeContract.CollegeMainEntry.MED_EARNINGS_2012, college.getEarnings());
                values.put(CollegeContract.CollegeMainEntry.GRADUATION_RATE_4_YEARS, college.getGraduationRate4yr());
                values.put(CollegeContract.CollegeMainEntry.GRADUATION_RATE_6_YEARS, college.getGraduationRate6yr());
                values.put(CollegeContract.CollegeMainEntry.UNDERGRAD_SIZE, college.getUndergradSize());
                values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, 1);
                values.put(CollegeContract.CollegeMainEntry.ADMISSION_RATE, college.getAdmissionRate());
                placeValues.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, collegeId);
                placeValues.put(CollegeContract.CollegeMainEntry.NAME, name);
                placeValues.put(CollegeContract.PlaceEntry.LATITUDE, college.getLatitude());
                placeValues.put(CollegeContract.PlaceEntry.LONGITUDE, college.getLongitude());
                placeValues.put(CollegeContract.PlaceEntry.LOCALE_CODE, college.getLocaleCode());

                cvVector.add(values);
                placeVector.add(placeValues);
            } else if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, 1);
                getContentResolver().update(CollegeContract.CollegeMainEntry.buildMainWithCollegeId(college.getId()),
                        values, null, null);
            }
        }
        Log.i(LOG_TAG, "NUMBER OF COLLEGES: " + cvVector.size());
        if (cvVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);
            try {
                getContentResolver().bulkInsert(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI, cvArray);
            } catch (IllegalArgumentException e) {
                Log.e(LOG_TAG, "CONTENT URI NOT RECOGNIZED");
            }
        }
        if (placeVector.size() > 0) {
            ContentValues[] placeArray = new ContentValues[placeVector.size()];
            placeVector.toArray(placeArray);
            try {
                getContentResolver().bulkInsert(CollegeContract.PlaceEntry.PLACE_CONTENT_URI, placeArray);
            } catch (IllegalArgumentException e) {
                Log.e(LOG_TAG, "CONTENT URI NOT RECOGNIZED");
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public String getLogo(String domainUrl) {
        String domain = domainUrl.toLowerCase();
        int indWww = domain.indexOf("www.") + 4;
//        Log.i(LOG_TAG, "" + indWww);
        if (indWww == 3) {
            indWww = domain.indexOf("web.") + 4;
//            Log.i(LOG_TAG, "" + indWww);
        }
        int indEdu = domain.indexOf(".edu") + 4;
        String baseUrl = "https://logo.clearbit.com/";
        String schoolEdu = domain.substring(indWww, indEdu);
        String imageSize = "?s=128";
        return baseUrl + schoolEdu + imageSize;
    }

    private String createFilter(List<String> favsToAdd) {
        String idFilter = "id=";
        int len = favsToAdd.size();
        for (int i = 0; i < len; i++) {
            idFilter += favsToAdd.get(i);
            if (i < len - 1) {
                idFilter += ",";
            }
        }
        return idFilter + "&" + Utility.CSC_API_KEY + "&";
    }
}
