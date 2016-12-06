//package com.scholar.dollar.android.dollarscholarbenlewis.service;
//
//import android.app.IntentService;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.util.Log;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.scholar.dollar.android.dollarscholarbenlewis.BuildConfig;
//import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
//import com.scholar.dollar.android.dollarscholarbenlewis.model.College;
//import com.scholar.dollar.android.dollarscholarbenlewis.model.CollegeMain;
//import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Vector;
//
//import static com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility.BASE_URL;
//
//
///**
// * An {@link IntentService} subclass for handling asynchronous task requests in
// * a service on a separate handler thread.
// * <p>
// * TODO: Customize class - update intent actions and extra parameters.
// */
//public class CollegeFbService extends IntentService {
//
//    private static final String LOG_TAG = CollegeFbService.class.getSimpleName();
//
//    public static final String ID = "id";
//    public static final String NAME = "school.name";
//    public static final String SCHOOL_URL = "school.school_url";
//    public static final String CITY = "school.city";
//    public static final String STATE = "school.state";
//    public static final String MED_EARNINGS_10_YEARS = "2012.earnings.10_yrs_after_entry.median";
//    public static final String IN_STATE_TUITION_AND_FEES = "2014.cost.tuition.in_state";
//    public static final String OUT_STATE_TUITION_AND_FEES = "2014.cost.tuition.out_of_state";
//    public static final String OWNERSHIP = "school.ownership";
//    public static final String GRADUATION_RATE_4_YEARS = "2014.completion.completion_rate_4yr_100nt";
//    public static final String GRADUATION_RATE_6_YEARS = "2014.completion.completion_rate_4yr_150nt";
//    public static final String UNDERGRAD_SIZE = "2014.student.size";
//    public static final String LATITUDE = "location.lat";
//    public static final String LONGITUDE = "location.lon";
//    public static final String LOCALE = "school.locale";
//    public static final String ADMISSION_RATE = "2014.admissions.admission_rate.overall";
//    public static final String[] MAIN_FIELDS = {ID, NAME, SCHOOL_URL, CITY, STATE, MED_EARNINGS_10_YEARS,
//            IN_STATE_TUITION_AND_FEES, OUT_STATE_TUITION_AND_FEES, OWNERSHIP, GRADUATION_RATE_4_YEARS,
//            GRADUATION_RATE_6_YEARS, UNDERGRAD_SIZE, LATITUDE, LONGITUDE, LOCALE, ADMISSION_RATE};
//    public String FIELDS_PARAMS = Utility.buildFieldsUrl(new ArrayList<>(Arrays.asList(MAIN_FIELDS)));
//
//    //filters
//    public static final String PREDOMINANT_DEGREE = "school.degrees_awarded.predominant=3"; // 3: bachelors
//    public static final String LEVEL = "school.institutional_characteristics.level=1"; // 1: 4 year school
//    public static final String COHORT_INFO_FILTER = "2008.completion.6_yr_completion.loan_students__range=20..";
//    public static final String ACT_FILTER = "2014.admissions.act_scores.midpoint.cumulative__range=10.."; //act scores signals undergraduate program
//    public static final String OWNERSHIP_FILTER = OWNERSHIP + "=1";
//    public static final String API_KEY = "api_key=" + BuildConfig.COLLEGE_SCORECARD_API_KEY;
//    public static final String[] FILTERS = {API_KEY, PREDOMINANT_DEGREE, LEVEL, COHORT_INFO_FILTER,
//            IN_STATE_TUITION_AND_FEES + "__range=1000..", ACT_FILTER};
//    public final String FILTER_PARAMS = Utility.joinFilters(new ArrayList<>(Arrays.asList(FILTERS)));
//    public static final String SORT_BY = "sort=2012.earnings.10_yrs_after_entry.median:desc";
//    public static final String PER_PAGE = "per_page=100";
//    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("csc-ref");
//    public String FINAL_URL = BASE_URL + FILTER_PARAMS + FIELDS_PARAMS + "&" + SORT_BY + "&" + PER_PAGE;
//
//    public CollegeFbService(String name) {
//        super(name);
//    }
//    public CollegeFbService(){
//        super(LOG_TAG);
//    }
////    public String REQUEST_URL = createFinalUrl(BASE_URL, FILTER_PARAMS, FIELDS_PARAMS, SORT_BY, PER_PAGE);
//
//
//    public static String createFinalUrl(String baseUrl, String filterUrl, String fieldsUrl, String sortBy, String perPage) {
//        return baseUrl + filterUrl + fieldsUrl + "&" + sortBy + "&" + perPage;
//    }
//
//
//    public ArrayList<College> getCollegeInfo(String url) {
//        try {
//            String jsonStr = Utility.fetch(url);
//            JSONObject results = new JSONObject(jsonStr);
//            JSONArray colleges = results.getJSONArray("results");
//            int collegesLength;
//            if (colleges != null) {
//                collegesLength = colleges.length();
//            } else {
//                Log.i(LOG_TAG, "ARRAY IS NULL");
//                return null;
//            }
//            ArrayList<College> collegesInfo = new ArrayList<>();
//            for (int i = 0; i < collegesLength; i++) {
//                JSONObject college = colleges.getJSONObject(i);
//                int id = college.getInt(ID);
//                String name = college.getString(NAME);
//                String domainUrl = college.getString(SCHOOL_URL);
//                String logoUrl = getLogo(domainUrl);
////                Log.i(LOG_TAG, logoUrl);
//
//                String city = college.getString(CITY);
//                String state = college.getString(STATE);
//                int earnings = college.getInt(MED_EARNINGS_10_YEARS);
//                int tuitionInState = college.getInt(IN_STATE_TUITION_AND_FEES);
//                int tuitionOutState = college.getInt(OUT_STATE_TUITION_AND_FEES);
//                int ownership = college.getInt(OWNERSHIP);
//                int undergradSize = college.getInt(UNDERGRAD_SIZE);
//                College col = new College(id, name, logoUrl, city, state, ownership,
//                        tuitionInState, tuitionOutState, earnings,
//                        handleNullDouble(college.get(GRADUATION_RATE_4_YEARS)),
//                        handleNullDouble(college.get(GRADUATION_RATE_6_YEARS)),
//                        undergradSize, handleNullDouble(college.get(ADMISSION_RATE)));
//                collegesInfo.add(col);
//            }
//            return collegesInfo;
//
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private Double handleNullDouble(Object o) {
//        if (o == null || o.toString().contains("null")) {
//            return null;
//        }
//        try {
//            return (Double) o;
//        } catch (ClassCastException e) {
//            Log.e(LOG_TAG, o.toString());
//            Log.e(LOG_TAG, "CLASS CAST EXCEPTION: " + e);
//            return null;
//        }
//    }
//
//    public int getLastPageNumber() {
//        int lastPage = 0;
//        try {
//            String jsonStr = Utility.fetch(FINAL_URL);
//            Log.i(LOG_TAG, FINAL_URL);
//            JSONObject results = new JSONObject(jsonStr);
//            int numColleges = results.getJSONObject("metadata").getInt("total");
//            lastPage = numColleges / 10;
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//        return lastPage;
//    }
//
//    public void addColleges(ArrayList<CollegeMain> colleges) {
//        Vector<ContentValues> cvVector = new Vector<>();
//        Vector<ContentValues> placeVector = new Vector<>();
//        Cursor cursor = null;
//        for (CollegeMain college : colleges) {
//            Uri uri = CollegeContract.CollegeMainEntry.buildMainWithCollegeId(college.getId());
//            cursor = getContentResolver().query(uri, null, null, null, null);
//            if (cursor == null || !cursor.moveToFirst()) {
//                ContentValues values = new ContentValues();
//                ContentValues placeValues = new ContentValues();
//
//                int collegeId = college.getId();
//                String name = college.getName();
//                values.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, collegeId);
//                values.put(CollegeContract.CollegeMainEntry.NAME, name);
//                values.put(CollegeContract.CollegeMainEntry.LOGO_URL, college.getLogoUrl());
//                values.put(CollegeContract.CollegeMainEntry.CITY, college.getCity());
//                values.put(CollegeContract.CollegeMainEntry.STATE, college.getState());
//                values.put(CollegeContract.CollegeMainEntry.OWNERSHIP, college.getOwnership());
//                values.put(CollegeContract.CollegeMainEntry.TUITION_IN_STATE, college.getTuitionInState());
//                values.put(CollegeContract.CollegeMainEntry.TUITION_OUT_STATE, college.getTuitionOutState());
//                values.put(CollegeContract.CollegeMainEntry.MED_EARNINGS_2012, college.getEarnings());
//                values.put(CollegeContract.CollegeMainEntry.MED_EARNINGS_2012, college.getEarnings());
//                values.put(CollegeContract.CollegeMainEntry.GRADUATION_RATE_4_YEARS, college.getGraduationRate4yr());
//                values.put(CollegeContract.CollegeMainEntry.GRADUATION_RATE_6_YEARS, college.getGraduationRate6yr());
//                values.put(CollegeContract.CollegeMainEntry.UNDERGRAD_SIZE, college.getUndergradSize());
//                values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, 0);
//                values.put(CollegeContract.CollegeMainEntry.ADMISSION_RATE, college.getAdmissionRate());
//
//                placeValues.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, collegeId);
//                placeValues.put(CollegeContract.CollegeMainEntry.NAME, name);
//                placeValues.put(CollegeContract.PlaceEntry.LATITUDE, college.getLatitude());
//                placeValues.put(CollegeContract.PlaceEntry.LONGITUDE, college.getLongitude());
//                placeValues.put(CollegeContract.PlaceEntry.LOCALE_CODE, college.getLocaleCode());
//
//                cvVector.add(values);
//                placeVector.add(placeValues);
//            }
//        }
//        Log.i(LOG_TAG, "NUMBER OF COLLEGES: " + cvVector.size());
//        if (cvVector.size() > 0) {
//            ContentValues[] cvArray = new ContentValues[cvVector.size()];
//            cvVector.toArray(cvArray);
//            try {
//                getContentResolver().bulkInsert(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI, cvArray);
//            } catch (IllegalArgumentException e) {
//                Log.e(LOG_TAG, "CONTENT URI NOT RECOGNIZED");
//            }
//        }
//        if (placeVector.size() > 0) {
//            ContentValues[] placeArray = new ContentValues[placeVector.size()];
//            placeVector.toArray(placeArray);
//            try {
//                getContentResolver().bulkInsert(CollegeContract.PlaceEntry.PLACE_CONTENT_URI, placeArray);
//            } catch (IllegalArgumentException e) {
//                Log.e(LOG_TAG, "CONTENT URI NOT RECOGNIZED");
//            }
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//    }
//
//    public String getLogo(String domainUrl) {
//        String domain = domainUrl.toLowerCase();
//        int indWww = domain.indexOf("www.") + 4;
////        Log.i(LOG_TAG, "" + indWww);
//        if (indWww == 3) {
//            indWww = domain.indexOf("web.") + 4;
////            Log.i(LOG_TAG, "" + indWww);
//        }
//        int indEdu = domain.indexOf(".edu") + 4;
//        String baseUrl = "https://logo.clearbit.com/";
//        String schoolEdu = domain.substring(indWww, indEdu);
//        String imageSize = "?s=128";
//        return baseUrl + schoolEdu + imageSize;
//    }
//
//
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        String[] filterArr;
//        String state = intent.getStringExtra(Utility.STATE_KEY);
//        if (state == null || !isSpecialState(state)){
//            filterArr = FILTERS;
//        } else{
//            filterArr =  Arrays.copyOfRange(FILTERS, 0, 5);
//        }
//        ArrayList<String> filter = new ArrayList<>(Arrays.asList(filterArr));
//        if (intent.getBooleanExtra(Utility.PUBLIC_COLLEGE_KEY, false))
//            filter.add(OWNERSHIP_FILTER);
//        if (state != null){
//            filter.add(STATE + "=" + state);
//        }
//        String filterParams = Utility.joinFilters(filter);
//        Log.i(LOG_TAG, filterParams);
//        String requestUrl = createFinalUrl(BASE_URL, filterParams, FIELDS_PARAMS, SORT_BY, PER_PAGE);
//        Log.i(LOG_TAG, requestUrl);
//        try {
//            ArrayList<CollegeMain> colleges = getCollegeInfo(requestUrl);
//            addColleges(colleges);
//        } catch (NullPointerException e) {
//            Log.e(LOG_TAG, "NULL EXCEPTION");
//        }
//    }
//}
