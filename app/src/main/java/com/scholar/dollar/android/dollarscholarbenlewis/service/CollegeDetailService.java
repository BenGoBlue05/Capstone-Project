package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.scholar.dollar.android.dollarscholarbenlewis.BuildConfig;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**

 */
public final class CollegeDetailService extends IntentService {

    public static final String LOG_TAG = CollegeDetailService.class.getSimpleName();

    public static final String MED_EARNINGS_10_YEARS_25_PCT =
            "2012.earnings.10_yrs_after_entry.working_not_enrolled.earnings_percentile.25"; //25th percentile
    public static final String MED_EARNINGS_10_YEARS_75_PCT =
            "2012.earnings.10_yrs_after_entry.working_not_enrolled.earnings_percentile.75"; //75th percentile
    public static final String UNDERGRAD_SIZE = "2014.student.size";
    public static final String MED_FAMILY_INCOME = "2014.student.demographics.median_family_income";
    public static final String ACT_MED = "2014.admissions.act_scores.midpoint.cumulative";
    public static final String ACT_25_PERCENTILE = "2014.admissions.act_scores.25th_percentile.cumulative";
    public static final String ACT_75_PERCENTILE = "2014.admissions.act_scores.75th_percentile.cumulative";
    public static final String SAT_EQUIV_AVG = "2014.admissions.sat_scores.average.overall"; //average SAT equivalent
    public static final String GRADUATION_RATE_4_YEARS = "2014.completion.completion_rate_4yr_100nt";
    public static final String LOAN_PRINCIPAL = "2014.aid.loan_principal";
    public static final String MED_DEBT_COMPLETERS = "2014.aid.median_debt.completers.overall"; //students who completed/graduated
    public static final String MED_MONTH_PAYMENT = "2014.aid.median_debt.completers.monthly_payments"; //10-year amortization plan
    public static final String PCT_LOAN_STUDENTS = "2014.aid.students_with_any_loan"; //share who received federal aid
    public static final String PCT_PELL_GRANT = "2014.aid.pell_grant_rate";
    public static final String SCHOOL_URL = "school.school_url";
    public static final String NET_PRICE_CALCULATOR_URL = "school.price_calculator_url";
    public static final String PER_PAGE = "per_page=100";
    public static final String[] FIELDS = {MED_EARNINGS_10_YEARS_25_PCT, MED_EARNINGS_10_YEARS_75_PCT, UNDERGRAD_SIZE,
            MED_FAMILY_INCOME, ACT_MED, ACT_25_PERCENTILE, ACT_75_PERCENTILE, SAT_EQUIV_AVG, GRADUATION_RATE_4_YEARS,
            LOAN_PRINCIPAL, MED_DEBT_COMPLETERS, MED_MONTH_PAYMENT, PCT_LOAN_STUDENTS, PCT_PELL_GRANT, SCHOOL_URL, NET_PRICE_CALCULATOR_URL};
    public static final String FIELDS_URL = CollegeService.buildFieldsUrl(FIELDS);
//    public static final String FINAL_URL = CollegeService.createFinalUrl(CollegeService.BASE_URL,
//            CollegeService.FILTER_PARAMS, FIELDS_URL, CollegeService.SORT_BY, PER_PAGE);
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public static String createFilter(int collegeId){
        return "id=" + collegeId + "&api_key=" + BuildConfig.COLLEGE_SCORECARD_API_KEY + "&";
    }
    public CollegeDetailService(String name) {
        super(name);
    }

    public CollegeDetailService() {
        super(CollegeDetailService.class.getSimpleName());
    }

    public void addCollege(String json, int collegeId) {
        ContentValues values = new ContentValues();
        Cursor cursor = getContentResolver().query(
                CollegeContract.CollegeDetailEntry.buildDetailWithCollegeId(collegeId),
                new String[]{CollegeContract.CollegeDetailEntry.COLLEGE_ID}, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst()){
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject college = jsonObject.getJSONArray("results").getJSONObject(0);
                Log.i(LOG_TAG, college.toString());
                Log.i(LOG_TAG, college.getString(SCHOOL_URL));
                values.put(CollegeContract.CollegeDetailEntry.COLLEGE_ID, collegeId);
                values.put(CollegeContract.CollegeDetailEntry.MED_EARNINGS_25_PCT, college.getInt(MED_EARNINGS_10_YEARS_25_PCT));
                values.put(CollegeContract.CollegeDetailEntry.MED_EARNINGS_75_PCT, college.getInt(MED_EARNINGS_10_YEARS_75_PCT));
                values.put(CollegeContract.CollegeDetailEntry.SAT_MED, (int) Math.round(college.getDouble(SAT_EQUIV_AVG)));
                values.put(CollegeContract.CollegeDetailEntry.ACT_MED, (int) Math.round(college.getDouble(ACT_MED)));
                values.put(CollegeContract.CollegeDetailEntry.ACT_25_PCT, (int) Math.round(college.getDouble(ACT_25_PERCENTILE)));
                values.put(CollegeContract.CollegeDetailEntry.ACT_75_PCT, (int) Math.round(college.getDouble(ACT_75_PERCENTILE)));
                values.put(CollegeContract.CollegeDetailEntry.UNDERGRAD_SIZE, college.getInt(UNDERGRAD_SIZE));
                values.put(CollegeContract.CollegeDetailEntry.FAMILY_INCOME, college.getInt(MED_FAMILY_INCOME));
                values.put(CollegeContract.CollegeDetailEntry.GRAD_RATE_4_YEARS, college.getDouble(GRADUATION_RATE_4_YEARS));
                values.put(CollegeContract.CollegeDetailEntry.LOAN_PRINCIPAL, (int) Math.round(college.getDouble(LOAN_PRINCIPAL)));
                values.put(CollegeContract.CollegeDetailEntry.MED_DEBT_COMPLETERS, (int) Math.round(college.getDouble(MED_DEBT_COMPLETERS)));
                values.put(CollegeContract.CollegeDetailEntry.MED_MONTH_PAYMENT, (int) Math.round(college.getDouble(MED_MONTH_PAYMENT)));
                values.put(CollegeContract.CollegeDetailEntry.LOAN_STUDENTS_PCT, college.getDouble(PCT_LOAN_STUDENTS));
                values.put(CollegeContract.CollegeDetailEntry.PELL_STUDENTS_PCT, college.getDouble(PCT_PELL_GRANT));
                values.put(CollegeContract.CollegeDetailEntry.PRICE_CALCULATOR, college.getString(NET_PRICE_CALCULATOR_URL));
                values.put(CollegeContract.CollegeDetailEntry.SCHOOL_URL, college.getString(SCHOOL_URL));
                getContentResolver().insert(CollegeContract.CollegeDetailEntry.COLLEGE_DETAIL_CONTENT_URI, values);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON EXCEPTION: " + e);
            }
        } else{
            Log.i(LOG_TAG, "COLUMNS: " + cursor.getColumnCount());
        }

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "INTENT RECEIVED");
        int collegeId = intent.getIntExtra("collegeIdKey", -1);
        if (collegeId == -1){
            Log.i(LOG_TAG, "COLLEGE ID NOT FOUND IN INTENT EXTRA");
            return;
        }
        String finalUrl = CollegeService.createFinalUrl(CollegeService.BASE_URL,
                createFilter(collegeId), FIELDS_URL, CollegeService.SORT_BY, PER_PAGE);
        Log.i(LOG_TAG, finalUrl);

        try {
            Log.i(LOG_TAG, "FETCHING COLLEGES STARTED");
            addCollege(CollegeService.fetch(finalUrl), collegeId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
