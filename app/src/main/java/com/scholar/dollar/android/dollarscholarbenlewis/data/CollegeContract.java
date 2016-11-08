package com.scholar.dollar.android.dollarscholarbenlewis.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bplewis5 on 10/5/16.
 */

public class CollegeContract {


    public static final String CONTENT_AUTHORITY = "com.scholar.dollar.android.dollarscholarbenlewis";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_COLLEGE_MAIN = "college_main";
    public static final String PATH_EARNINGS = "earnings";
    public static final String PATH_COST = "cost";
    public static final String PATH_DEBT = "debt";
    public static final String PATH_ADMISSION = "admission";
    public static final String PATH_COMPLETION = "completion";
    public static final String PATH_PLACE = "place";

    public static final class CollegeMainEntry implements BaseColumns{

        public static final Uri COLLEGE_MAIN_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COLLEGE_MAIN);
        public static final String COLLEGE_MAIN_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGE_MAIN;
        public static final String COLLEGE_MAIN_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGE_MAIN;

        public static final String COLLEGE_MAIN_TABLE = "college_main";

        public static final String COLLEGE_ID = "college_id";
        public static final String NAME = "name";
        public static final String LOGO_URL = "logo_url";
        public static final String CITY = "city";
        public static final String STATE = "state";
        public static final String OWNERSHIP = "ownership";
        public static final String TUITION_IN_STATE = "tuition_in_state";
        public static final String TUITION_OUT_STATE = "tuition_out_state";
        public static final String MED_EARNINGS_2012 = "earnings_med_2012_coh";
        public static final String GRADUATION_RATE_4_YEARS = "grad_rate_4_yrs";
        public static final String GRADUATION_RATE_6_YEARS = "grad_rate_6_years";
        public static final String UNDERGRAD_SIZE = "undergrads"; //New addition
        public static final String IS_FAVORITE = "is_favorite";


        public static Uri buildCollegeMainUri(long id){
            return ContentUris.withAppendedId(COLLEGE_MAIN_CONTENT_URI, id);
        }

        public static Uri buildMainWithCollegeId(int collegeId){
            return COLLEGE_MAIN_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }

    }

    public static final class EarningsEntry implements BaseColumns{

        public static final Uri EARNINGS_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EARNINGS);

        public static final String EARNINGS_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EARNINGS;
        public static final String EARNINGS_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EARNINGS;


        public static final String EARNINGS_TABLE = "earnings";

        //EARNINGS_xYRS_yPCT: y percentile of earnings of students working and not enrolled x years after entry

        public static final String EARNINGS_6YRS_25PCT = "earnings_6yrs_25pct";
        public static final String EARNINGS_6YRS_50PCT = "earnings_6yrs_50pct";
        public static final String EARNINGS_6YRS_75PCT = "earnings_6yrs_75pct";

        public static final String EARNINGS_8YRS_25PCT = "earnings_8yrs_25pct";
        public static final String EARNINGS_8YRS_50PCT = "earnings_8yrs_50pct";
        public static final String EARNINGS_8YRS_75PCT = "earnings_8yrs_75pct";

        public static final String EARNINGS_10YRS_25PCT = "earnings_10yrs_25pct";
        public static final String EARNINGS_10YRS_50PCT = "earnings_10yrs_50pct";
        public static final String EARNINGS_10YRS_75PCT = "earnings_10yrs_75pct";

        public static Uri buildEarningsUri(long id){
            return ContentUris.withAppendedId(EARNINGS_CONTENT_URI, id);
        }

        public static Uri buildEarningsWithCollegeId(int collegeId){
            return EARNINGS_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }
    }


    public static final class CostEntry implements BaseColumns{

        public static final Uri COST_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COST);

        public static final String COST_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COST;
        public static final String COST_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COST;
        public static final String COST_TABLE = "cost";

        //COST_FAM_XtoY: average net price X for students of income between X and Y
        //eg. COST_FAM_30to48: family income between $30-48k
        public static final String  COST_FAM_0to30= "cost_fam_0to30";
        public static final String  COST_FAM_30to48= "cost_fam_30to48";
        public static final String  COST_FAM_48to75= "cost_fam_40to75";
        public static final String  COST_FAM_75to110= "cost_fam_75to110";
        public static final String  COST_FAM_OVER_110 = "cost_fam_over110";

        public static final String LOAN_STUDENTS_PCT = "loan_students_pct";
        public static final String  PELL_STUDENTS_PCT = "pell_students_pct";

        public static final String  PRICE_CALCULATOR_URL= "price_calc_url";


        public static Uri buildCostUri(long id){
            return ContentUris.withAppendedId(COST_CONTENT_URI, id);
        }

        public static Uri buildCostWithCollegeId(int collegeId){
            return COST_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }
    }

    public static final class DebtEntry implements BaseColumns{

        public static final Uri DEBT_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DEBT);

        public static final String DEBT_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEBT;
        public static final String DEBT_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEBT;
        public static final String DEBT_TABLE = "debt";

        public static final String LOAN_PRINCIPAL_MED = "loan_principal";
        public static final String DEBT_NONCOMPLETERS_MED = "debt_noncomplerers";
        public static final String DEBT_COMPLETERS_MED = "debt_completers";
        public static final String MONTH_PAYMENT_10YR_MED = "monthly_payment";

        public static final String DEBT_FAM_0to30_MED = "debt_fam_0to30_med";
        public static final String DEBT_FAM_30to75_MED = "debt_fam_30to75_med";
        public static final String DEBT_FAM_75up_MED = "debt_fam_75up_med";

        public static Uri buildDebtUri(long id){
            return ContentUris.withAppendedId(DEBT_CONTENT_URI, id);
        }

        public static Uri buildDebtWithCollegeId(int collegeId){
            return DEBT_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }
    }


    public static final class CompletionEntry implements BaseColumns{

        public static final Uri COMPLETION_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COMPLETION);

        public static final String COMPLETION_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMPLETION;
        public static final String COMPLETION_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMPLETION;
        public static final String COMPLETION_TABLE = "completion";

        public static final String GRADUATION_RATE_4_YEARS = "grad_rate_4_yrs";
        public static final String GRADUATION_RATE_6_YEARS = "grad_rate_6_years";

        public static Uri buildCompletionUri(long id){
            return ContentUris.withAppendedId(COMPLETION_CONTENT_URI, id);
        }

        public static Uri buildCompletionWithCollegeId(int collegeId){
            return COMPLETION_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }
    }

    public static final class AdmissionEntry implements BaseColumns{

        public static final Uri ADMISSION_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ADMISSION);

        public static final String ADMISSION_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADMISSION;
        public static final String ADMISSION_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ADMISSION;

        public static final String ADMISSION_TABLE = "admission";

        public static final String ADMISSION_RATE = "admission_rate";

        public static final String ACT_CUM_50_PCT = "act_med";
        public static final String ACT_CUM_25_PCT = "act_25_pct";
        public static final String ACT_CUM_75_PCT = "act_75_pct";

        public static final String ACT_MATH_50_PCT = "act_math_med";
        public static final String ACT_MATH_25_PCT = "act_math_25_pct";
        public static final String ACT_MATH_75_PCT = "act_math_75_pct";

        public static final String ACT_ENG_50_PCT = "act_eng_med";
        public static final String ACT_ENG_25_PCT = "act_eng_25_pct";
        public static final String ACT_ENG_75_PCT = "act_eng_75_pct";

        public static final String SAT_CUM_50_PCT = "sat_med";

        public static final String SAT_MATH_50_PCT = "sat_math_med";
        public static final String SAT_MATH_25_PCT = "sat_math_25_pct";
        public static final String SAT_MATH_75_PCT = "sat_math_75_pct";

        public static final String SAT_READ_50_PCT = "sat_read_med";
        public static final String SAT_READ_25_PCT = "sat_read_25_pct";
        public static final String SAT_READ_75_PCT = "sat_read_75_pct";

        public static Uri buildAdmissionUri(long id){
            return ContentUris.withAppendedId(ADMISSION_CONTENT_URI, id);
        }

        public static Uri buildAdmissionWithCollegeId(int collegeId){
            return ADMISSION_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }
    }

    public static final class PlaceEntry implements BaseColumns{

        public static final Uri PLACE_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLACE);

        public static final String PLACE_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;
        public static final String PLACE_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;
        public static final String PLACE_TABLE = "place";

        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LOCALE_CODE = "locale";
        public static final String PLACE_ID = "place_id";
        public static final String PLACE_NAME = "place_name";

        public static Uri buildPlaceUri(long id){
            return ContentUris.withAppendedId(PLACE_CONTENT_URI, id);
        }

        public static Uri buildPlaceWithCollegeId(int collegeId){
            return PLACE_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }
    }

}