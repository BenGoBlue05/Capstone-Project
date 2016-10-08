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
    public static final String INT_NN = " INTEGER NOT NULL";
    public static final String REAL_NN = " REAL NOT NULL";
    static final String DATABASE = "college.db";
    private static final int VERSION = 11;

    public CollegeDbHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_COLLEGE_MAIN =
                "CREATE TABLE " + CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE + " (" +
                        CollegeContract.CollegeMainEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.NAME + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.LOGO_URL + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.CITY + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.STATE + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.OWNERSHIP + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.TUITION_IN_STATE + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.TUITION_OUT_STATE + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.MED_EARNINGS_2012 + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.GRADUATION_RATE_6_YEAR + REAL_NN + " );";

        final String CREATE_TABLE_COLLEGE_DETAIL =
                "CREATE TABLE " + CollegeContract.CollegeDetailEntry.COLLEGE_DETAIL_TABLE + " (" +
                        CollegeContract.CollegeDetailEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.MED_EARNINGS_25_PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.MED_EARNINGS_75_PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.ACT_MED + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.ACT_25_PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.ACT_75_PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.SAT_MED + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.FAMILY_INCOME + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.UNDERGRAD_SIZE + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.GRAD_RATE_4_YEARS + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.LOAN_PRINCIPAL + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.MED_DEBT_COMPLETERS + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.MED_MONTH_PAYMENT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.LOAN_STUDENTS_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.PELL_STUDENTS_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.SCHOOL_URL + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeDetailEntry.PRICE_CALCULATOR + TEXT_NN + " );";

        db.execSQL(CREATE_TABLE_COLLEGE_MAIN);
        db.execSQL(CREATE_TABLE_COLLEGE_DETAIL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.CollegeDetailEntry.COLLEGE_DETAIL_TABLE);
        onCreate(db);
    }
}
