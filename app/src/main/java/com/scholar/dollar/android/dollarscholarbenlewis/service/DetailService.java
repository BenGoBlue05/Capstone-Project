package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class DetailService extends IntentService {

    private static int mCollegeId;
    private static boolean mIsPublic;

    private static final String LOG_TAG = DetailService.class.getSimpleName();

    public static final String EARNINGS_10YRS_25PCT = "2012.earnings.10_yrs_after_entry.working_not_enrolled.earnings_percentile.25"; //25th percentile
    public static final String EARNINGS_10YRS_50PCT = "2012.earnings.10_yrs_after_entry.median";
    public static final String EARNINGS_10YRS_75PCT = "2012.earnings.10_yrs_after_entry.working_not_enrolled.earnings_percentile.75"; //75th percentile
    public static final String EARNINGS_8YRS_25PCT = "2012.earnings.8_yrs_after_entry.25th_percentile_earnings"; //25th percentile
    public static final String EARNINGS_8YRS_50PCT = "2012.earnings.8_yrs_after_entry.median_earnings";
    public static final String EARNINGS_8YRS_75PCT = "2012.earnings.8_yrs_after_entry.75th_percentile_earnings";
    public static final String EARNINGS_6YRS_25PCT = "2012.earnings.6_yrs_after_entry.working_not_enrolled.earnings_percentile.25";
    public static final String EARNINGS_6YRS_50PCT = "2012.earnings.6_yrs_after_entry.median";
    public static final String EARNINGS_6YRS_75PCT = "2012.earnings.6_yrs_after_entry.working_not_enrolled.earnings_percentile.75";
    public static final String[] EARNINGS_FIELDS = {EARNINGS_6YRS_25PCT, EARNINGS_6YRS_50PCT, EARNINGS_6YRS_75PCT, EARNINGS_8YRS_25PCT,
            EARNINGS_8YRS_50PCT, EARNINGS_8YRS_75PCT, EARNINGS_10YRS_25PCT, EARNINGS_10YRS_50PCT, EARNINGS_10YRS_75PCT};

    public static final String COST_PUBLIC_0to30 = "2014.cost.net_price.public.by_income_level.0-30000";
    public static final String COST_PUBLIC_30to48 = "2014.cost.net_price.public.by_income_level.30001-48000";
    public static final String COST_PUBLIC_48to75 = "2014.cost.net_price.public.by_income_level.48001-75000";
    public static final String COST_PUBLIC_75to110 = "2014.cost.net_price.public.by_income_level.75001-110000";
    public static final String COST_PUBLIC_110plus = "2014.cost.net_price.public.by_income_level.110001-plus";
    public static final String COST_LOANS_PCT = "2014.aid.federal_loan_rate";
    public static final String COST_GRANTS_PCT = "2014.student.students_with_pell_grant";
    public static final String COST_PRICE_CALCULATOR = "school.price_calculator_url";
    public static final String[] COST_PUBLIC_FIELDS =
            {COST_PUBLIC_0to30, COST_PUBLIC_30to48, COST_PUBLIC_48to75, COST_PUBLIC_75to110, COST_PUBLIC_110plus,
                    COST_LOANS_PCT, COST_GRANTS_PCT, COST_PRICE_CALCULATOR};
    public static final String COST_PRIVATE_0to30 = "2014.cost.net_price.private.by_income_level.0-30000";
    public static final String COST_PRIVATE_30to48 = "2014.cost.net_price.private.by_income_level.30001-48000";
    public static final String COST_PRIVATE_48to75 = "2014.cost.net_price.private.by_income_level.48001-75000";
    public static final String COST_PRIVATE_75to110 = "2014.cost.net_price.private.by_income_level.75001-110000";
    public static final String COST_PRIVATE_110plus = "2014.cost.net_price.private.by_income_level.110001-plus";
    public static final String[] COST_PRIVATE_FIELDS =
            {COST_PRIVATE_0to30, COST_PRIVATE_30to48, COST_PRIVATE_48to75, COST_PRIVATE_75to110, COST_PRIVATE_110plus,
                    COST_LOANS_PCT, COST_GRANTS_PCT, COST_PRICE_CALCULATOR};

    public static final String DEBT_LOAN_PRINCIPAL = "2014.aid.loan_principal";
    public static final String DEBT_MONTHLY_PAYMENT = "2014.aid.median_debt.completers.monthly_payments"; //10-year amortization plan
    public static final String DEBT_10_PCT = "2014.aid.cumulative_debt.10th_percentile";
    public static final String DEBT_25_PCT = "2014.aid.cumulative_debt.25th_percentile";
    public static final String DEBT_75_PCT = "2014.aid.cumulative_debt.75th_percentile";
    public static final String DEBT_90_PCT = "2014.aid.cumulative_debt.90th_percentile";
    public static final String[] DEBT_FIELDS = {DEBT_LOAN_PRINCIPAL, DEBT_MONTHLY_PAYMENT,
            DEBT_10_PCT, DEBT_25_PCT, DEBT_75_PCT, DEBT_90_PCT};

//    public static final String ADMISSION_RATE = "2014.admissions.admission_rate.overall";
//    public static final String ADMISSION_ACT_25PCT = "2014.admissions.act_scores.25th_percentile.cumulative";
//    public static final String ADMISSION_ACT_50PCT = "2014.admissions.act_scores.midpoint.cumulative";
//    public static final String ADMISSION_ACT_75PCT = "2014.admissions.act_scores.75th_percentile.cumulative";
//    public static final String ADMISSION_ACT_ENG_25PCT = "2014.admissions.act_scores.25th_percentile.english";
//    public static final String ADMISSION_ACT_ENG_50PCT = "2014.admissions.act_scores.midpoint.english";
//    public static final String ADMISSION_ACT_ENG_75PCT = "2014.admissions.act_scores.75th_percentile.english";
//    public static final String ADMISSION_ACT_MATH_25PCT = "2014.admissions.act_scores.25th_percentile.math";
//    public static final String ADMISSION_ACT_MATH_50PCT = "2014.admissions.act_scores.midpoint.math";
//    public static final String ADMISSION_ACT_MATH_75PCT = "2014.admissions.act_scores.75th_percentile.math";
//    public static final String ADMISSION_SAT_50PCT = "2014.admissions.sat_scores.average.overall";
//    public static final String ADMISSION_SAT_READ_25PCT = "2014.admissions.sat_scores.25th_percentile.critical_reading";
//    public static final String ADMISSION_SAT_READ_50PCT = "2014.admissions.sat_scores.midpoint.critical_reading";
//    public static final String ADMISSION_SAT_READ_75PCT = "2014.admissions.sat_scores.75th_percentile.critical_reading";
//    public static final String ADMISSION_SAT_MATH_25PCT = "2014.admissions.sat_scores.25th_percentile.math";
//    public static final String ADMISSION_SAT_MATH_50PCT = "2014.admissions.sat_scores.midpoint.math";
//    public static final String ADMISSION_SAT_MATH_75PCT = "2014.admissions.sat_scores.75th_percentile.math";
//
//    public static final String[] ADMISSION_FIELDS = {ADMISSION_RATE, ADMISSION_ACT_25PCT, ADMISSION_ACT_50PCT, ADMISSION_ACT_75PCT,
//    ADMISSION_ACT_ENG_25PCT, ADMISSION_ACT_ENG_50PCT, ADMISSION_ACT_ENG_75PCT, ADMISSION_ACT_MATH_25PCT, ADMISSION_ACT_MATH_50PCT,
//    ADMISSION_ACT_MATH_75PCT, ADMISSION_SAT_50PCT, ADMISSION_SAT_READ_25PCT, ADMISSION_SAT_READ_50PCT, ADMISSION_SAT_READ_75PCT,
//    ADMISSION_SAT_MATH_25PCT, ADMISSION_SAT_MATH_50PCT, ADMISSION_SAT_MATH_75PCT};


    public DetailService(String name) {
        super(name);
    }

    public DetailService() {
        super(LOG_TAG);
    }

    public void addDetails(String json) {
        ContentValues earningsValues = new ContentValues();
        ContentValues costValues = new ContentValues();
        ContentValues debtValues = new ContentValues();
        Cursor cursor = getContentResolver().query(
                CollegeContract.EarningsEntry.buildEarningsWithCollegeId(mCollegeId),
                new String[]{CollegeContract.CollegeMainEntry.COLLEGE_ID}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject college = jsonObject.getJSONArray("results").getJSONObject(0);

                earningsValues.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, mCollegeId);

                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_6YRS_25PCT, college.getInt(EARNINGS_6YRS_25PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_6YRS_50PCT, college.getInt(EARNINGS_6YRS_50PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_6YRS_75PCT, college.getInt(EARNINGS_6YRS_75PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_8YRS_25PCT, college.getInt(EARNINGS_8YRS_25PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_8YRS_50PCT, college.getInt(EARNINGS_8YRS_50PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_8YRS_75PCT, college.getInt(EARNINGS_8YRS_75PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_10YRS_25PCT, college.getInt(EARNINGS_10YRS_25PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_10YRS_50PCT, college.getInt(EARNINGS_10YRS_50PCT));
                earningsValues.put(CollegeContract.EarningsEntry.EARNINGS_10YRS_75PCT, college.getInt(EARNINGS_10YRS_75PCT));
                costValues.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, mCollegeId);

                if (mIsPublic) {
                    costValues.put(CollegeContract.CostEntry.COST_FAM_0to30, college.getInt(COST_PUBLIC_0to30));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_30to48, college.getInt(COST_PUBLIC_30to48));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_48to75, college.getInt(COST_PUBLIC_48to75));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_75to110, college.getInt(COST_PUBLIC_75to110));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_OVER_110, college.getInt(COST_PUBLIC_110plus));
                } else {
                    costValues.put(CollegeContract.CostEntry.COST_FAM_0to30, college.getInt(COST_PRIVATE_0to30));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_30to48, college.getInt(COST_PRIVATE_30to48));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_48to75, college.getInt(COST_PRIVATE_48to75));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_75to110, college.getInt(COST_PRIVATE_75to110));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_OVER_110, college.getInt(COST_PRIVATE_110plus));
                }

                costValues.put(CollegeContract.CostEntry.LOAN_STUDENTS_PCT, college.getDouble(COST_LOANS_PCT));
                costValues.put(CollegeContract.CostEntry.PELL_STUDENTS_PCT, college.getDouble(COST_GRANTS_PCT));
                costValues.put(CollegeContract.CostEntry.PRICE_CALCULATOR_URL, college.getString(COST_PRICE_CALCULATOR));

                debtValues.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, mCollegeId);
                debtValues.put(CollegeContract.DebtEntry.LOAN_PRINCIPAL_MED, college.getDouble(DEBT_LOAN_PRINCIPAL));
                debtValues.put(CollegeContract.DebtEntry.MONTH_PAYMENT_10YR_MED, college.getInt(DEBT_MONTHLY_PAYMENT));
                debtValues.put(CollegeContract.DebtEntry.DEBT_10PCT, handleNullInt(college.get(DEBT_10_PCT)));
                debtValues.put(CollegeContract.DebtEntry.DEBT_25PCT, college.getInt(DEBT_25_PCT));
                debtValues.put(CollegeContract.DebtEntry.DEBT_75PCT, college.getInt(DEBT_75_PCT));
                debtValues.put(CollegeContract.DebtEntry.DEBT_90PCT, handleNullInt(college.get(DEBT_90_PCT)));

                getContentResolver().insert(CollegeContract.EarningsEntry.EARNINGS_CONTENT_URI, earningsValues);
                getContentResolver().insert(CollegeContract.CostEntry.COST_CONTENT_URI, costValues);
                getContentResolver().insert(CollegeContract.DebtEntry.DEBT_CONTENT_URI, debtValues);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON EXCEPTION: " + e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

    }

    private Integer handleNullInt(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return (Integer) o;
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "CLASS CAST EXCEPTION: " + e);
            return null;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.i(LOG_TAG, "INTENT RECEIVED");
            mCollegeId = intent.getIntExtra(Utility.COLLEGE_ID_KEY, -1);
            if (mCollegeId == -1) {
                Log.i(LOG_TAG, "COLLEGE ID NOT FOUND IN INTENT EXTRA");
                return;
            }
            mIsPublic = intent.getBooleanExtra(Utility.PUBLIC_COLLEGE_KEY, false);
            String[] costFields = mIsPublic ? COST_PUBLIC_FIELDS : COST_PRIVATE_FIELDS;
            ArrayList<String[]> fieldArrays = new ArrayList<>();
            fieldArrays.add(EARNINGS_FIELDS);
            fieldArrays.add(costFields);
            fieldArrays.add(DEBT_FIELDS);
//            fieldArrays.add(ADMISSION_FIELDS);
            String url = Utility.BASE_URL + Utility.createCollegeFilter(mCollegeId) + Utility.buildFields(fieldArrays);
            Log.i(LOG_TAG, url);

            try {
                Log.i(LOG_TAG, "FETCHING COLLEGES STARTED");
                addDetails(Utility.fetch(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(LOG_TAG, "SERVICE INTENT IS NULL");
        }
    }
}
