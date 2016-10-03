package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scholar.dollar.android.dollarscholarbenlewis.BuildConfig;
import com.scholar.dollar.android.dollarscholarbenlewis.model.College;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class CollegeIntentService extends IntentService {

    public static final String BASE_URL =
            "https://api.data.gov/ed/collegescorecard/v1/schools.json?";
    public static final String FIELDS_PARAMS = "fields=id,school.name";
    //filters
    public static final String PREDOMINANT_DEGREE = "school.degrees_awarded.predominant=3"; // 3: bachelors
    public static final String LEVEL = "school.institutional_characteristics.level=1"; // 1: 4 year school
    public static final String COHORT_INFO_FILTER = "2008.completion.6_yr_completion.loan_students__range=20..";
    public static final String ACT_FILTER = "2014.admissions.act_scores.midpoint.cumulative__range=10.."; //act scores signals undergraduate program
    public static final String API_KEY = "api_key=" + BuildConfig.COLLEGE_SCORECARD_API_KEY;
    public static final String[] FILTERS = {API_KEY, PREDOMINANT_DEGREE, LEVEL, COHORT_INFO_FILTER, ACT_FILTER};
    public static final String SORT_BY = "sort=2012.earnings.10_yrs_after_entry.median:desc";
    private static final String LOG_TAG = CollegeIntentService.class.getSimpleName();
    private static final String PER_PAGE = "per_page=100";
    public final String FILTER_PARAMS = joinFilters(FILTERS);
    public String FINAL_URL = BASE_URL + FILTER_PARAMS + FIELDS_PARAMS + "&" + SORT_BY + "&" + PER_PAGE;
    private OkHttpClient mClient = new OkHttpClient();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public CollegeIntentService(String name) {
        super(name);
    }

    public CollegeIntentService() {
        super("CollegeIntentService");
    }

    public String joinFilters(String[] filters) {
        String url = "";
        for (String filter : filters) {
            url += filter + "&";
        }
        return url;
    }

    String fetchCollegeScorecard(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = mClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public int getLastPageNumber() {
        int lastPage = 0;
        try {
            String jsonStr = fetchCollegeScorecard(FINAL_URL);
            Log.i(LOG_TAG, FINAL_URL);
            JSONObject results = new JSONObject(jsonStr);
            int numColleges = results.getJSONObject("metadata").getInt("total");
            lastPage = numColleges / 10;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return lastPage;
    }

    public void addCollegeMains() {
        int lastPageNumber = getLastPageNumber();
        for (int i = 0; i < lastPageNumber; i++) {
            String url = FINAL_URL + "&page=" + i;
            addCollegeMainsPerPage(url);
        }
    }

    public void addCollegeMainsPerPage(String url) {
        try {
            String jsonStr = fetchCollegeScorecard(url);
            JSONObject results = new JSONObject(jsonStr);
            JSONArray colleges = results.getJSONArray("results");
            int collegesLength;
            if (colleges != null) {
                collegesLength = colleges.length();
            } else {
                Log.i(LOG_TAG, "ARRAY IS NULL");
                return;
            }
            for (int i = 0; i < collegesLength; i++) {
                JSONObject college = colleges.getJSONObject(i);
                String id = "" + college.getInt("id");
                String name = college.getString("school.name");
                Map<String, Object> values = new College(name).toMap();
                mDatabase.child("colleges").child(id).setValue(values);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        addCollegeMains();
    }
}
