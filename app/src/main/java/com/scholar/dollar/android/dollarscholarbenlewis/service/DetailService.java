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
    public static final String[] COST_PUBLIC_FIELDS =
            {COST_PUBLIC_0to30, COST_PUBLIC_30to48, COST_PUBLIC_48to75, COST_PUBLIC_75to110, COST_PUBLIC_110plus};
    public static final String COST_PRIVATE_0to30 = "2014.cost.net_price.private.by_income_level.0-30000";
    public static final String COST_PRIVATE_30to48 = "2014.cost.net_price.private.by_income_level.30001-48000";
    public static final String COST_PRIVATE_48to75 = "2014.cost.net_price.private.by_income_level.48001-75000";
    public static final String COST_PRIVATE_75to110 = "2014.cost.net_price.private.by_income_level.75001-110000";
    public static final String COST_PRIVATE_110plus = "2014.cost.net_price.private.by_income_level.110001-plus";
    public static final String[] COST_PRIVATE_FIELDS =
            {COST_PRIVATE_0to30, COST_PRIVATE_30to48, COST_PRIVATE_48to75, COST_PRIVATE_75to110, COST_PRIVATE_110plus};

    public static final String DEBT_LOAN_PRINCIPAL = "2014.aid.loan_principal";
    public static final String DEBT_COMPLETERS = "2014.aid.median_debt.completers.overall"; //students who completed/graduated
    public static final String DEBT_NONCOMPLETERS = "2014.aid.median_debt.noncompleters";
    public static final String DEBT_MONTHLY_PAYMENT = "2014.aid.median_debt.completers.monthly_payments"; //10-year amortization plan
    public static final String DEBT_0to30 = "2014.aid.median_debt.income.0_30000";
    public static final String DEBT_30to75 = "2014.aid.median_debt.income.30001_75000";
    public static final String DEBT_75plus = "2014.aid.median_debt.income.greater_than_75000";
    public static final String[] DEBT_FIELDS = {DEBT_LOAN_PRINCIPAL, DEBT_COMPLETERS, DEBT_NONCOMPLETERS,
    DEBT_MONTHLY_PAYMENT, DEBT_0to30, DEBT_30to75, DEBT_75plus};

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
        if (cursor == null || !cursor.moveToFirst()) {
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

                if (mIsPublic){
                    costValues.put(CollegeContract.CostEntry.COST_FAM_0to30, college.getInt(COST_PUBLIC_0to30));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_30to48, college.getInt(COST_PUBLIC_30to48));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_48to75, college.getInt(COST_PUBLIC_48to75));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_75to110, college.getInt(COST_PUBLIC_75to110));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_OVER_110, college.getInt(COST_PUBLIC_110plus));
                } else{
                    costValues.put(CollegeContract.CostEntry.COST_FAM_0to30, college.getInt(COST_PRIVATE_0to30));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_30to48, college.getInt(COST_PRIVATE_30to48));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_48to75, college.getInt(COST_PRIVATE_48to75));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_75to110, college.getInt(COST_PRIVATE_75to110));
                    costValues.put(CollegeContract.CostEntry.COST_FAM_OVER_110, college.getInt(COST_PRIVATE_110plus));
                }

                debtValues.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, mCollegeId);
                debtValues.put(CollegeContract.DebtEntry.LOAN_PRINCIPAL_MED, college.getDouble(DEBT_LOAN_PRINCIPAL));
                debtValues.put(CollegeContract.DebtEntry.DEBT_COMPLETERS_MED, college.getDouble(DEBT_COMPLETERS));
                debtValues.put(CollegeContract.DebtEntry.DEBT_NONCOMPLETERS_MED, college.getDouble(DEBT_NONCOMPLETERS));
                debtValues.put(CollegeContract.DebtEntry.MONTH_PAYMENT_10YR_MED, college.getDouble(DEBT_MONTHLY_PAYMENT));
                debtValues.put(CollegeContract.DebtEntry.DEBT_FAM_0to30_MED, college.getDouble(DEBT_0to30));
                debtValues.put(CollegeContract.DebtEntry.DEBT_FAM_30to75_MED, college.getDouble(DEBT_30to75));
                debtValues.put(CollegeContract.DebtEntry.DEBT_FAM_75up_MED, college.getDouble(DEBT_75plus));

                getContentResolver().insert(CollegeContract.EarningsEntry.EARNINGS_CONTENT_URI, earningsValues);
                getContentResolver().insert(CollegeContract.CostEntry.COST_CONTENT_URI, costValues);
                getContentResolver().insert(CollegeContract.DebtEntry.DEBT_CONTENT_URI, debtValues);

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
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
            String url = Utility.BASE_URL + Utility.createCollegeFilter(mCollegeId) + Utility.buildFields(fieldArrays);
            Log.i(LOG_TAG, url);

            try {
                Log.i(LOG_TAG, "FETCHING COLLEGES STARTED");
                addDetails(Utility.fetch(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i(LOG_TAG, "SERVICE INTENT IS NULL");
        }
    }
}
