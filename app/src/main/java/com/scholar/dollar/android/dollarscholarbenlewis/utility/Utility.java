package com.scholar.dollar.android.dollarscholarbenlewis.utility;

import com.scholar.dollar.android.dollarscholarbenlewis.BuildConfig;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;

import java.io.IOException;
import java.util.ArrayList;

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

    public static final int COLLEGE_MAIN_LOADER = 100;
    public static final int EARNINGS_LOADER = 200;
    public static final int COST_LOADER = 300;
    public static final int DEBT_LOADER = 400;
    public static final int COMPLETION_LOADER = 500;
    public static final int ADMISSION_LOADER = 600;
    public static final int PLACE_LOADER = 700;


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


    public static String buildFieldsUrl(String[] fields) {
        String baseUrl = "fields=";
        for (int i = 0; i < fields.length; i++) {
            baseUrl += i == 0 ? fields[i] : "," + fields[i];
        }
        return baseUrl;
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

    public static String createCollegeFilter(int collegeId){
        return "id=" + collegeId + "&" + CSC_API_KEY + "&";
    }
}
