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

public class EarningsService extends IntentService {

    private static int mCollegeId;

    private static final String LOG_TAG = EarningsService.class.getSimpleName();

    public static final String EARNINGS_10YRS_25PCT =
            "2012.earnings.10_yrs_after_entry.working_not_enrolled.earnings_percentile.25"; //25th percentile
    public static final String EARNINGS_10YRS_50PCT = "2012.earnings.10_yrs_after_entry.median";
    public static final String EARNINGS_10YRS_75PCT =
            "2012.earnings.10_yrs_after_entry.working_not_enrolled.earnings_percentile.75"; //75th percentile

    public static final String EARNINGS_8YRS_25PCT =
            "2012.earnings.8_yrs_after_entry.25th_percentile_earnings"; //25th percentile
    public static final String EARNINGS_8YRS_50PCT = "2012.earnings.8_yrs_after_entry.median_earnings";
    public static final String EARNINGS_8YRS_75PCT =
            "2012.earnings.8_yrs_after_entry.75th_percentile_earnings";

    public static final String EARNINGS_6YRS_25PCT =
            "2012.earnings.6_yrs_after_entry.working_not_enrolled.earnings_percentile.25";
    public static final String EARNINGS_6YRS_50PCT = "2012.earnings.6_yrs_after_entry.median";
    public static final String EARNINGS_6YRS_75PCT =
            "2012.earnings.6_yrs_after_entry.working_not_enrolled.earnings_percentile.75";

    public static final String[] FIELDS = {EARNINGS_6YRS_25PCT, EARNINGS_6YRS_50PCT, EARNINGS_6YRS_75PCT,
            EARNINGS_8YRS_25PCT, EARNINGS_8YRS_50PCT, EARNINGS_8YRS_75PCT, EARNINGS_10YRS_25PCT,
            EARNINGS_10YRS_50PCT, EARNINGS_10YRS_75PCT};


    public EarningsService(String name) {
        super(name);
    }

    public EarningsService() {
        super(LOG_TAG);
    }

    public void addEarningsInfo(String json) {
        ContentValues values = new ContentValues();
        Cursor cursor = getContentResolver().query(
                CollegeContract.EarningsEntry.buildEarningsWithCollegeId(mCollegeId),
                new String[]{CollegeContract.CollegeMainEntry.COLLEGE_ID}, null, null, null, null);
        if (cursor == null || !cursor.moveToFirst()) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject college = jsonObject.getJSONArray("results").getJSONObject(0);

                values.put(CollegeContract.CollegeMainEntry.COLLEGE_ID, mCollegeId);

                values.put(CollegeContract.EarningsEntry.EARNINGS_6YRS_25PCT, college.getInt(EARNINGS_6YRS_25PCT));
                values.put(CollegeContract.EarningsEntry.EARNINGS_6YRS_50PCT, college.getInt(EARNINGS_6YRS_50PCT));
                values.put(CollegeContract.EarningsEntry.EARNINGS_6YRS_75PCT, college.getInt(EARNINGS_6YRS_75PCT));

                values.put(CollegeContract.EarningsEntry.EARNINGS_8YRS_25PCT, college.getInt(EARNINGS_8YRS_25PCT));
                values.put(CollegeContract.EarningsEntry.EARNINGS_8YRS_50PCT, college.getInt(EARNINGS_8YRS_50PCT));
                values.put(CollegeContract.EarningsEntry.EARNINGS_8YRS_75PCT, college.getInt(EARNINGS_8YRS_75PCT));

                values.put(CollegeContract.EarningsEntry.EARNINGS_10YRS_25PCT, college.getInt(EARNINGS_10YRS_25PCT));
                values.put(CollegeContract.EarningsEntry.EARNINGS_10YRS_50PCT, college.getInt(EARNINGS_10YRS_50PCT));
                values.put(CollegeContract.EarningsEntry.EARNINGS_10YRS_75PCT, college.getInt(EARNINGS_10YRS_75PCT));

                getContentResolver().insert(CollegeContract.EarningsEntry.COLLEGE_EARNINGS_CONTENT_URI, values);
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
        Log.i(LOG_TAG, "INTENT RECEIVED");
        mCollegeId = intent.getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        if (mCollegeId == -1) {
            Log.i(LOG_TAG, "COLLEGE ID NOT FOUND IN INTENT EXTRA");
            return;
        }
        String finalUrl = Utility.BASE_URL + Utility.createCollegeFilter(mCollegeId) + Utility.buildFieldsUrl(FIELDS);
        Log.i(LOG_TAG, finalUrl);

        try {
            Log.i(LOG_TAG, "FETCHING COLLEGES STARTED");
            addEarningsInfo(Utility.fetch(finalUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
