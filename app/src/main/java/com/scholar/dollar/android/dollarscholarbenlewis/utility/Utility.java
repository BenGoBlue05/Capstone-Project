package com.scholar.dollar.android.dollarscholarbenlewis.utility;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.scholar.dollar.android.dollarscholarbenlewis.BuildConfig;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.model.CollegeBasic;
import com.scholar.dollar.android.dollarscholarbenlewis.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by benjaminlewis on 11/2/16.
 */

public final class Utility {

    public static final String BASE_URL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?";
    public static String COLLEGE_ID_KEY = "collegeIdKey";
    public static String PUBLIC_COLLEGE_KEY = "publicCollegeKey";
    public static String FAVORITE_COLLEGE_KEY = "favoriteCollegeKey";
    public static String STATE_KEY = "stateKey";
    public static String NAME_KEY = "nameKey";
    public static String LON_KEY = "lonKey";
    public static String LAT_KEY = "latKey";


    public static String[] EARNINGS_COLUMNS = {
            CollegeContract.EarningsEntry.EARNINGS_6YRS_25PCT,
            CollegeContract.EarningsEntry.EARNINGS_6YRS_50PCT,
            CollegeContract.EarningsEntry.EARNINGS_6YRS_75PCT,
            CollegeContract.EarningsEntry.EARNINGS_8YRS_25PCT,
            CollegeContract.EarningsEntry.EARNINGS_8YRS_50PCT,
            CollegeContract.EarningsEntry.EARNINGS_8YRS_75PCT,
            CollegeContract.EarningsEntry.EARNINGS_10YRS_25PCT,
            CollegeContract.EarningsEntry.EARNINGS_10YRS_50PCT,
            CollegeContract.EarningsEntry.EARNINGS_10YRS_75PCT
    };



    public final static String[] PLACE_COLUMNS = {
            CollegeContract.CollegeMainEntry.COLLEGE_ID,
            CollegeContract.CollegeMainEntry.NAME,
            CollegeContract.PlaceEntry.LATITUDE,
            CollegeContract.PlaceEntry.LONGITUDE,
            CollegeContract.PlaceEntry.PLACE_ID
    };

    public static final String[] COLLEGE_COLUMNS = {
            CollegeContract.CollegeMainEntry.COLLEGE_ID,
            CollegeContract.CollegeMainEntry.NAME,
            CollegeContract.CollegeMainEntry.LOGO_URL,
            CollegeContract.CollegeMainEntry.CITY,
            CollegeContract.CollegeMainEntry.STATE,
            CollegeContract.CollegeMainEntry.OWNERSHIP,
            CollegeContract.CollegeMainEntry.TUITION_IN_STATE,
            CollegeContract.CollegeMainEntry.TUITION_OUT_STATE,
            CollegeContract.CollegeMainEntry.MED_EARNINGS_2012,
            CollegeContract.CollegeMainEntry.GRADUATION_RATE_4_YEARS,
            CollegeContract.CollegeMainEntry.GRADUATION_RATE_6_YEARS,
            CollegeContract.CollegeMainEntry.UNDERGRAD_SIZE,
            CollegeContract.CollegeMainEntry.IS_FAVORITE,
            CollegeContract.CollegeMainEntry.ADMISSION_RATE
    };



    public static String[] ADMISSION_COLUMNS = {
            CollegeContract.CollegeMainEntry.COLLEGE_ID,
            CollegeContract.AdmissionEntry.ACT_CUM_25_PCT,
            CollegeContract.AdmissionEntry.ACT_CUM_50_PCT,
            CollegeContract.AdmissionEntry.ACT_CUM_75_PCT,
            CollegeContract.AdmissionEntry.ACT_MATH_25_PCT,
            CollegeContract.AdmissionEntry.ACT_MATH_50_PCT,
            CollegeContract.AdmissionEntry.ACT_MATH_75_PCT,
            CollegeContract.AdmissionEntry.ACT_ENG_25_PCT,
            CollegeContract.AdmissionEntry.ACT_ENG_50_PCT,
            CollegeContract.AdmissionEntry.ACT_ENG_75_PCT,
            CollegeContract.AdmissionEntry.SAT_CUM_50_PCT,
            CollegeContract.AdmissionEntry.SAT_MATH_25_PCT,
            CollegeContract.AdmissionEntry.SAT_MATH_50_PCT,
            CollegeContract.AdmissionEntry.SAT_MATH_75_PCT,
            CollegeContract.AdmissionEntry.SAT_READ_25_PCT,
            CollegeContract.AdmissionEntry.SAT_READ_50_PCT,
            CollegeContract.AdmissionEntry.SAT_READ_75_PCT,
    };

    public static int COL_ADMSN_CSC_ID = 0;
    public static int COL_ADMSN_ACT25 = 1;
    public static int COL_ADMSN_ACT50 = 2;
    public static int COL_ADMSN_ACT75 = 3;
    public static int COL_ADMSN_ACT_MATH25 = 4;
    public static int COL_ADMSN_ACT_MATH50 = 5;
    public static int COL_ADMSN_ACT_MATH75 = 6;
    public static int COL_ADMSN_ACT_ENG25 = 7;
    public static int COL_ADMSN_ACT_ENG50 = 8;
    public static int COL_ADMSN_ACT_ENG75 = 9;
    public static int COL_ADMSN_SAT50 = 10;
    public static int COL_ADMSN_SAT_MATH25 = 11;
    public static int COL_ADMSN_SAT_MATH50 = 12;
    public static int COL_ADMSN_SAT_MATH75 = 13;
    public static int COL_ADMSN_SAT_READ25 = 14;
    public static int COL_ADMSN_SAT_READ50 = 15;
    public static int COL_ADMSN_SAT_READ75 = 16;

    public static final int COLLEGE_ID = 0;
    public static final int NAME = 1;
    public static final int LOGO = 2;
    public static final int CITY = 3;
    public static final int STATE = 4;
    public static final int OWNERSHIP = 5;
    public static final int TUITION_IN_STATE = 6;
    public static final int TUITION_OUT_STATE = 7;
    public static final int EARNINGS = 8;
    public static final int GRAD_RATE_4_YEARS = 9;
    public static final int GRAD_RATE_6_YEARS = 10;
    public static final int SIZE = 11;
    public static final int FAVORITE = 12;
    public static final int ADMISSION_RATE = 13;

    public static final int COL_COLLEGE_ID = 0;
    public static final int COL_CSC_NAME = 1;
    public static final int COL_LAT = 2;
    public static final int COL_LON = 3;
    public static final int COL_PLACE_ID = 4;

    public static int COL_EARN_6YRS_25PCT = 0;
    public static int COL_EARN_6YRS_50PCT = 1;
    public static int COL_EARN_6YRS_75PCT = 2;
    public static int COL_EARN_8YRS_25PCT = 3;
    public static int COL_EARN_8YRS_50PCT = 4;
    public static int COL_EARN_8YRS_75PCT = 5;
    public static int COL_EARN_10YRS_25PCT = 6;
    public static int COL_EARN_10YRS_50PCT = 7;
    public static int COL_EARN_10YRS_75PCT = 8;


    //filters
    public static final String PREDOMINANT_DEGREE = "school.degrees_awarded.predominant=3"; // 3: bachelors
    public static final String LEVEL = "school.institutional_characteristics.level=1"; // 1: 4 year school
    public static final String COHORT_INFO_FILTER = "2008.completion.6_yr_completion.loan_students__range=20..";
    public static final String ACT_FILTER = "2014.admissions.act_scores.midpoint.cumulative__range=10.."; //act scores signals undergraduate program
    public static final String CSC_API_KEY = "api_key=" + BuildConfig.COLLEGE_SCORECARD_API_KEY;
    public static final String[] BASE_FILTERS = {CSC_API_KEY, PREDOMINANT_DEGREE, LEVEL, COHORT_INFO_FILTER, ACT_FILTER};
    public static final String FAVORITES_SYNC_KEY = "favorites_sync";


    public static String buildFieldsUrl(ArrayList<String> fields) {
        String baseUrl = "fields=";
        for (int i = 0; i < fields.size(); i++) {
            baseUrl += i == 0 ? fields.get(i) : "," + fields.get(i);
        }
        return baseUrl;
    }

    public static String buildFields(ArrayList<String[]> fieldArrays) {
        ArrayList<String> fieldList = new ArrayList<>();
        for (String[] fieldArray : fieldArrays) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(fieldArray)));
        }
        return buildFieldsUrl(fieldList);
    }

    public static String joinFilters(ArrayList<String> filters) {
        String url = "";
        for (String filter : filters) {
            url += filter + "&";
        }
        return url;
    }

    public static String fetch(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = new OkHttpClient().newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String createCollegeFilter(int collegeId) {
        return "id=" + collegeId + "&" + CSC_API_KEY + "&";
    }

    public static String formatThousandsCircle(float i){
        int thousand = Math.round(i / 1000f);
        return Integer.toString(thousand);
    }

    public static void onStarClicked(DatabaseReference ref, boolean isUser, final int collegeId, final boolean newFavorite,
                               final String uId) {
        if (!isUser) {
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    CollegeBasic college = mutableData.getValue(CollegeBasic.class);

                    if (college == null) {
                        return Transaction.success(mutableData);
                    }

                    if (newFavorite) {
                        if (!college.stars.containsKey(uId)) {
                            college.starCount = college.starCount + 1;
                            college.stars.put(uId, true);
                        }
                    } else {
                        if (college.stars.containsKey(uId)) {
                            college.starCount = college.starCount - 1;
                            college.stars.remove(uId);
                        }
                    }
                    // Set value and report transaction success
                    mutableData.setValue(college);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    // Transaction completed

                }
            });
        } else {
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    User user = mutableData.getValue(User.class);
                    if (user == null) {
                        return Transaction.success(mutableData);
                    }

                    if (newFavorite){
                        if (!user.favorites.containsKey(String.valueOf(collegeId))){
                            user.favoritesCount = user.favoritesCount + 1;
                            user.favorites.put(String.valueOf(collegeId), true);
                        }
                    } else{
                        if (user.favorites.containsKey(String.valueOf(collegeId))){
                            user.favoritesCount = user.favoritesCount - 1;
                            user.favorites.remove(String.valueOf(collegeId));
                        }
                    }
                    // Set value and report transaction success
                    mutableData.setValue(user);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                }
            });
        }

    }
}
