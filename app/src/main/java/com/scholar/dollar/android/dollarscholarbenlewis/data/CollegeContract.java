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

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COLLEGE_MAIN);
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGE_MAIN;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGE_MAIN;

        public static final String COLLEGE_MAIN_TABLE = "college_main_tab";

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

        public static Uri buildCollegeMainUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMainWithCollegeId(int collegeId){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(collegeId)).build();
        }

        public static int getCollegeIdFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    public static final class CollegeDetailEntry implements BaseColumns{
        public static final String COLLEGE_ID = "college_id";
        public static final String NAME = "name";
        public static final String LOGO_URL = "logo_url";
        public static final String CITY = "city";
        public static final String STATE = "state";
        public static final String OWNERSHIP = "school.ownership";
        public static final String TUITION_IN_STATE = "tuition_in_state";
        public static final String TUITION_OUT_STATE = "tuition_out_state";
        public static final String MED_EARNINGS_2012 = "earnings_med_2012_coh";
        public static final String GRADUATION_RATE_6_YEAR = "graduation_rate_6_years";
    }


}
