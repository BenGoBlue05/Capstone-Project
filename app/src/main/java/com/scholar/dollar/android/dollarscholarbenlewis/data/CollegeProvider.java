package com.scholar.dollar.android.dollarscholarbenlewis.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by bplewis5 on 10/4/16.
 */
@ContentProvider(authority = CollegeProvider.AUTHORITY,
        database = CollegeDatabase.class,
        packageName = "com.scholar.dollar.android.dollarscholarbenlewis.provider")

public final class CollegeProvider {

    public CollegeProvider() {
    }

    public static final String AUTHORITY = "com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String COLLEGE_MAIN = "college_main";
//        String COLLEGE_DETAILS = "college_details";
    }

    @TableEndpoint(table = CollegeDatabase.COLLEGE_MAIN)
    public static class CollegesMain {


        @ContentUri(
                path = Path.COLLEGE_MAIN,
                type = "vnd.android.cursor.dir/college_mains")
        public static final Uri CONTENT_URI = buildUri(Path.COLLEGE_MAIN);

        @InexactContentUri(
                name = "PLANET_ID",
                path = Path.COLLEGE_MAIN + "/#",
                type = "vnd.android.cursor.item/college_mains",
                whereColumn = CollegeMainColumns.COLLEGE_ID,
                pathSegment = 1)
        public static Uri withCollegeId(String college_id) {
            return buildUri(Path.COLLEGE_MAIN, college_id);
        }
    }

//    @TableEndpoint(table = CollegeDatabase.COLLEGE_DETAILS)
//    public static class CollegeDetails {
//        @ContentUri(
//                path = Path.COLLEGE_DETAILS,
//                type = "vnd.android.cursor.dir/college_details",
//                defaultSort = CollegeDetailColumns.MED_EARNINGS_2012 + " DESC"
//        )
//        public static final Uri CONTENT_URI = buildUri(Path.COLLEGE_DETAILS);
//
//        @InexactContentUri(
//                name = "ARCHIVED_PLANET_ID",
//                path = Path.COLLEGE_DETAILS + "/#",
//                type = "vnd.android.cursor.item/college_details",
//                whereColumn = CollegeDetailColumns.COLLEGE_ID,
//                pathSegment = 1
//        )
//
//        public static Uri withCollegeId(String college_id) {
//            return buildUri(Path.COLLEGE_DETAILS, college_id);
//        }
//    }
}
