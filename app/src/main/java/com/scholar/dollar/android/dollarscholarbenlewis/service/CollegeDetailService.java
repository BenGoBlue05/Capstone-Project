package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scholar.dollar.android.dollarscholarbenlewis.model.CollegeDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.scholar.dollar.android.dollarscholarbenlewis.fragment.CollegeMainFragment.LOG_TAG;
import static com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeService.fetchColleges;

/**

 */
public final class CollegeDetailService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

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

    public static final String[] FIELDS = {CollegeService.ID, MED_EARNINGS_10_YEARS_25_PCT, MED_EARNINGS_10_YEARS_75_PCT, UNDERGRAD_SIZE,
            MED_FAMILY_INCOME, ACT_MED, ACT_25_PERCENTILE, ACT_75_PERCENTILE, SAT_EQUIV_AVG, GRADUATION_RATE_4_YEARS,
            LOAN_PRINCIPAL, MED_DEBT_COMPLETERS, MED_MONTH_PAYMENT, PCT_LOAN_STUDENTS, PCT_PELL_GRANT, SCHOOL_URL, NET_PRICE_CALCULATOR_URL};

    public static final String FIELDS_URL = CollegeService.buildFieldsUrl(FIELDS);
    public static final String FINAL_URL = CollegeService.createFinalUrl(CollegeService.BASE_URL,
            CollegeService.FILTER_PARAMS, FIELDS_URL, CollegeService.SORT_BY, PER_PAGE);


    public CollegeDetailService(String name) {
        super(name);
    }

    public CollegeDetailService() {
        super(CollegeDetailService.class.getSimpleName());
    }

    public CollegeDetail getCollegeDetails(JSONObject jsonObject) {
        try {
            int collegeId = jsonObject.getInt(CollegeService.ID);
            int medianIncome25 = jsonObject.getInt(MED_EARNINGS_10_YEARS_25_PCT);
            int medianIncome75 = jsonObject.getInt(MED_EARNINGS_10_YEARS_75_PCT);
            double satMed = jsonObject.getDouble(SAT_EQUIV_AVG);
            double actMed = jsonObject.getDouble(ACT_MED);
            double act25 = jsonObject.getDouble(ACT_25_PERCENTILE);
            double act75 = jsonObject.getDouble(ACT_75_PERCENTILE);
            int schoolSize = jsonObject.getInt(UNDERGRAD_SIZE);
            int familyIncome = jsonObject.getInt(MED_FAMILY_INCOME);
            double gradRate4 = jsonObject.getDouble(GRADUATION_RATE_4_YEARS);
            double principal = jsonObject.getDouble(LOAN_PRINCIPAL);
            double debtCompleters = jsonObject.getDouble(MED_DEBT_COMPLETERS);
            double monthlyPayment = jsonObject.getDouble(MED_MONTH_PAYMENT);
            double pctLoanStudents = jsonObject.getDouble(PCT_LOAN_STUDENTS);
            double pctPellStudents = jsonObject.getDouble(PCT_PELL_GRANT);
            String priceCalculator = jsonObject.getString(NET_PRICE_CALCULATOR_URL);
            String website = jsonObject.getString(SCHOOL_URL);
            return new CollegeDetail(collegeId, medianIncome25, medianIncome75, satMed, actMed, act25, act75, schoolSize,
                    familyIncome, gradRate4, principal, debtCompleters, monthlyPayment, pctLoanStudents,
                    pctPellStudents, priceCalculator, website);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON EXCEPTION: " + e);
        }
        return null;
    }

    public int getLastPageNumber(){
        int lastPage = 0;
        try {
            String jsonStr = fetchColleges(FINAL_URL);
            JSONObject results = new JSONObject(jsonStr);
            int numColleges = results.getJSONObject("metadata").getInt("total");
            lastPage = numColleges / 10;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return lastPage;
    }

    public void addCollegeDetailsPerPage(String url){
        try {
            String jsonStr = fetchColleges(url);
            JSONObject results = new JSONObject(jsonStr);
            JSONArray colleges = results.getJSONArray("results");
            int collegesLength;
            if (colleges != null){
                collegesLength = colleges.length();
            } else{
                Log.i(LOG_TAG, "ARRAY IS NULL");
                return;
            }
            for (int i = 0; i < collegesLength; i++){
                JSONObject college = colleges.getJSONObject(i);
                CollegeDetail collegeDetail = getCollegeDetails(college);
                if (collegeDetail != null) {
                    String collegeId = Integer.toString(collegeDetail.getCollegeId());
                    mDatabase.child("college-detail").child(collegeId).setValue(collegeDetail);
                } else {
                    Log.i(LOG_TAG, "COLLEGE_DETAIL IS NULL");
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void addCollegeDetails(){
        int lastPageNumber = getLastPageNumber();
        for (int i = 0; i < lastPageNumber; i++){
            String url = FINAL_URL + "&page=" + i;
            addCollegeDetailsPerPage(url);
        }

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        addCollegeDetails();
    }
}
