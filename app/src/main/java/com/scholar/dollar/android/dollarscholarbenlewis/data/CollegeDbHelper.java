package com.scholar.dollar.android.dollarscholarbenlewis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bplewis5 on 10/5/16.
 */

public class CollegeDbHelper extends SQLiteOpenHelper {


    public static final String COMMA_SEPERATOR = ", ";
    public static final String TEXT_NN = " TEXT NOT NULL";
    public static final String INTEGER_NN = " INTEGER NOT NULL";
    public static final String REAL_NN = " REAL NOT NULL";
    static final String DATABASE = "college.db";
    private static final int VERSION = 5;
    public CollegeDbHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_COLLEGE_MAIN =
                "CREATE TABLE " + CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE + " (" +
                        CollegeContract.CollegeMainEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INTEGER_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.NAME + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.LOGO_URL + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.CITY + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.STATE + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.OWNERSHIP + INTEGER_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.TUITION_IN_STATE + INTEGER_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.TUITION_OUT_STATE + INTEGER_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.MED_EARNINGS_2012 + INTEGER_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.GRADUATION_RATE_6_YEAR + REAL_NN + " );";

    db.execSQL(CREATE_TABLE_COLLEGE_MAIN);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE);
        onCreate(db);
    }
}
