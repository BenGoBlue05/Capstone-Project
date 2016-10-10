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
    public static final String PATH_COLLEGE_DETAIL = "college_detail";


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
        public static final String GRADUATION_RATE_6_YEAR = "graduation_rate_6_years";
        public static final String IS_FAVORITE = "is_favorite";

        public static Uri buildCollegeMainUri(long id){
            return ContentUris.withAppendedId(COLLEGE_MAIN_CONTENT_URI, id);
        }

        public static Uri buildMainWithCollegeId(int collegeId){
            return COLLEGE_MAIN_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }

    }

    public static final class CollegeDetailEntry implements BaseColumns{

        public static final Uri COLLEGE_DETAIL_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COLLEGE_DETAIL);

        public static final String COLLEGE_DETAIL_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGE_DETAIL;
        public static final String COLLEGE_DETAIL_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGE_DETAIL;
        public static final String COLLEGE_DETAIL_TABLE = "college_detail";

        public static final String COLLEGE_ID = "college_id";
        public static final String MED_EARNINGS_25_PCT = "earnings_25_pct";
        public static final String MED_EARNINGS_75_PCT = "earnings_75_pct";
        public static final String ACT_MED = "act_med";
        public static final String ACT_25_PCT = "act_25_pct";
        public static final String ACT_75_PCT = "act_75_pct";
        public static final String SAT_MED = "sat_med";
        public static final String UNDERGRAD_SIZE = "undergrads";
        public static final String FAMILY_INCOME = "family_income_med";
        public static final String GRAD_RATE_4_YEARS = "grad_rate_4_yrs";
        public static final String LOAN_PRINCIPAL = "loan_principal";
        public static final String MED_DEBT_COMPLETERS = "debt_completers";
        public static final String MED_MONTH_PAYMENT = "monthly_payment";
        public static final String LOAN_STUDENTS_PCT = "loan_students_pct";
        public static final String  PELL_STUDENTS_PCT = "pell_students_pct";
        public static final String SCHOOL_URL = "school_url";
        public static final String  PRICE_CALCULATOR = "price_calc";

        public static Uri buildCollegeDetailUri(long id){
            return ContentUris.withAppendedId(COLLEGE_DETAIL_CONTENT_URI, id);
        }

        public static Uri buildDetailWithCollegeId(int collegeId){
            return COLLEGE_DETAIL_CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }
    }
}