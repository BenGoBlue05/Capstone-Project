package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.scholar.dollar.android.dollarscholarbenlewis.BuildConfig;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.model.CollegeMain;
import com.scholar.dollar.android.dollarscholarbenlewis.ui.CollegeMainFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public final class CollegeService extends IntentService {

    public static final String BASE_URL = "https://api.data.gov/ed/collegescorecard/v1/schools.json?";

    //fields
    public static final String ID = "id";
    public static final String NAME = "school.name";
    public static final String SCHOOL_URL = "school.school_url";
    public static final String CITY = "school.city";
    public static final String STATE = "school.state";
    public static final String MED_EARNINGS_10_YEARS = "2012.earnings.10_yrs_after_entry.median";
    public static final String IN_STATE_TUITION_AND_FEES = "2014.cost.tuition.in_state";
    public static final String OUT_STATE_TUITION_AND_FEES = "2014.cost.tuition.out_of_state";
    public static final String OWNERSHIP = "school.ownership";
    public static final String GRADUATION_RATE_6_YEARS = "2014.completion.completion_rate_4yr_150nt";
    public static final String[] MAIN_FIELDS = {ID, NAME, SCHOOL_URL, CITY, STATE, MED_EARNINGS_10_YEARS,
            IN_STATE_TUITION_AND_FEES, OUT_STATE_TUITION_AND_FEES, OWNERSHIP, GRADUATION_RATE_6_YEARS};
    public String FIELDS_PARAMS = buildFieldsUrl(MAIN_FIELDS);

    //filters
    public static final String PREDOMINANT_DEGREE = "school.degrees_awarded.predominant=3"; // 3: bachelors
    public static final String LEVEL = "school.institutional_characteristics.level=1"; // 1: 4 year school
    public static final String COHORT_INFO_FILTER = "2008.completion.6_yr_completion.loan_students__range=20..";
    public static final String ACT_FILTER = "2014.admissions.act_scores.midpoint.cumulative__range=10.."; //act scores signals undergraduate program
    public static final String OWNERSHIP_FILTER = OWNERSHIP + "=1";
    public static final String API_KEY = "api_key=" + BuildConfig.COLLEGE_SCORECARD_API_KEY;
    public static final String[] FILTERS = {API_KEY, PREDOMINANT_DEGREE, LEVEL, COHORT_INFO_FILTER, ACT_FILTER};
    private static final String LOG_TAG = CollegeService.class.getSimpleName();
    public static final String SORT_BY = "sort=2012.earnings.10_yrs_after_entry.median:desc";
    public static final String PER_PAGE = "per_page=50";
//    public String REQUEST_URL = createFinalUrl(BASE_URL, FILTER_PARAMS, FIELDS_PARAMS, SORT_BY, PER_PAGE);

    public CollegeService(String name) {
        super(name);
    }

    public CollegeService() {
        super("CollegeService");
    }

    public static String createFinalUrl(String baseUrl, String filterUrl, String fieldsUrl, String sortBy, String perPage){
        return baseUrl + filterUrl + fieldsUrl + "&" + sortBy + "&" + perPage;
    }

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

    public static String fetchColleges(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = new OkHttpClient().newCall(request).execute()) {
            return response.body().string();
        }
    }



    public ArrayList<CollegeMain> getCollegeInfo(String url) {
        try {
            String jsonStr = fetchColleges(url);
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
                int id = college.getInt(ID);
                String name = college.getString(NAME);

                String domainUrl = college.getString(SCHOOL_URL);
                String logoUrl = getLogo(domainUrl);
//                Log.i(LOG_TAG, logoUrl);

                String city = college.getString(CITY);
                String state = college.getString(STATE);
                int earnings = college.getInt(MED_EARNINGS_10_YEARS);
                int tuitionInState = college.getInt(IN_STATE_TUITION_AND_FEES);
                int tuitionOutState = college.getInt(IN_STATE_TUITION_AND_FEES);
                int ownership = college.getInt(OWNERSHIP);
                double graduationRate = college.getDouble(GRADUATION_RATE_6_YEARS);
                CollegeMain collegeMain = new CollegeMain(id, name, logoUrl, city, state, ownership,
                        tuitionInState, tuitionOutState, earnings, graduationRate);
                collegesInfo.add(collegeMain);
            }
            return collegesInfo;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addColleges(ArrayList<CollegeMain> colleges){
        Vector<ContentValues> cvVector = new Vector<>();
        Cursor cursor = null;
        for (CollegeMain college : colleges){
            Uri uri = CollegeContract.CollegeMainEntry.buildMainWithCollegeId(college.getId());
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor == null || !cursor.moveToFirst()){
                ContentValues values = new ContentValues();
                values.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, college.getId());
                values.put(CollegeContract.CollegeMainEntry.NAME, college.getName());
                values.put(CollegeContract.CollegeMainEntry.LOGO_URL, college.getLogoUrl());
                values.put(CollegeContract.CollegeMainEntry.CITY, college.getCity());
                values.put(CollegeContract.CollegeMainEntry.STATE, college.getState());
                values.put(CollegeContract.CollegeMainEntry.OWNERSHIP, college.getOwnership());
                values.put(CollegeContract.CollegeMainEntry.TUITION_IN_STATE, college.getTuitionInState());
                values.put(CollegeContract.CollegeMainEntry.TUITION_OUT_STATE, college.getTuitionOutState());
                values.put(CollegeContract.CollegeMainEntry.MED_EARNINGS_2012, college.getEarnings());
                values.put(CollegeContract.CollegeMainEntry.GRADUATION_RATE_6_YEAR, college.getGraduationRate());
                values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, 0);

                cvVector.add(values);
            }
        }
        Log.i(LOG_TAG, "NUMBER OF COLLEGES: " + cvVector.size());
        if (cvVector.size() > 0){
            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);
            try{
                getContentResolver().bulkInsert(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI, cvArray);
            } catch (IllegalArgumentException e){
                Log.e(LOG_TAG, "CONTENT URI NOT RECOGNIZED");
            }
        }

        if (cursor != null){
            cursor.close();
        }
    }
    public String getLogo(String domainUrl){
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

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<String> filter = new ArrayList<>(Arrays.asList(FILTERS));
        if (intent.getBooleanExtra(CollegeMainFragment.PUBLIC_COLLEGES_BOOLEAN_KEY, false))
            filter.add(OWNERSHIP_FILTER);
        String state = intent.getStringExtra(MainActivity.STATE_KEY);
        if (state != null)
            filter.add(STATE + "=" + state);
        String filterParams = joinFilters(filter);
        Log.i(LOG_TAG, joinFilters(filter));
        String requestUrl = createFinalUrl(BASE_URL, filterParams, FIELDS_PARAMS, SORT_BY, PER_PAGE);
        Log.i(LOG_TAG, requestUrl);
        try{
            ArrayList<CollegeMain> colleges = getCollegeInfo(requestUrl);
            addColleges(colleges);
        } catch (NullPointerException e){
            Log.e(LOG_TAG, "NULL EXCEPTION");
        }
    }

}