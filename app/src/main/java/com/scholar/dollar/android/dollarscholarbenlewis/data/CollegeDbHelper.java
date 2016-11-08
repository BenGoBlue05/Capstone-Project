package com.scholar.dollar.android.dollarscholarbenlewis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bplewis5 on 10/5/16.
 */

public class CollegeDbHelper extends SQLiteOpenHelper {


    public static final String COMMA_SEPERATOR = ", ";
    public static final String TEXT = " TEXT";
    public static final String TEXT_NN = " TEXT NOT NULL";
    public static final String INT_NN = " INTEGER NOT NULL";
    public static final String INT = " INTEGER";
    public static final String REAL_NN = " REAL NOT NULL";
    public static final String REAL = " REAL";
    static final String DATABASE = "college.db";
    private static final int VERSION = 37;

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
                        CollegeContract.CompletionEntry.GRADUATION_RATE_4_YEARS + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.CompletionEntry.GRADUATION_RATE_6_YEARS + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.IS_FAVORITE + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.UNDERGRAD_SIZE + INT_NN +
                        " );";

        final String CREATE_TABLE_EARNINGS =
                "CREATE TABLE " + CollegeContract.EarningsEntry.EARNINGS_TABLE + " (" +
                        CollegeContract.EarningsEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_6YRS_25PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_6YRS_50PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_6YRS_75PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_8YRS_25PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_8YRS_50PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_8YRS_75PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_10YRS_25PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_10YRS_50PCT + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.EarningsEntry.EARNINGS_10YRS_75PCT + INT_NN + " );";

        final String CREATE_TABLE_COST =
                "CREATE TABLE " + CollegeContract.CostEntry.COST_TABLE + " (" +
                        CollegeContract.CostEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CostEntry.COST_FAM_0to30 + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CostEntry.COST_FAM_30to48 + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CostEntry.COST_FAM_48to75 + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CostEntry.COST_FAM_75to110 + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CostEntry.COST_FAM_OVER_110 + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CostEntry.LOAN_STUDENTS_PCT + REAL + COMMA_SEPERATOR +
                        CollegeContract.CostEntry.PELL_STUDENTS_PCT + REAL + " );";

        final String CREATE_TABLE_DEBT =
                "CREATE TABLE " + CollegeContract.DebtEntry.DEBT_TABLE + " (" +
                        CollegeContract.DebtEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.DebtEntry.LOAN_PRINCIPAL_MED + REAL_NN +COMMA_SEPERATOR +
                        CollegeContract.DebtEntry.MONTH_PAYMENT_10YR_MED + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.DebtEntry.DEBT_COMPLETERS_MED + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.DebtEntry.DEBT_NONCOMPLETERS_MED + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.DebtEntry.DEBT_FAM_0to30_MED + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.DebtEntry.DEBT_FAM_30to75_MED + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.DebtEntry.DEBT_FAM_75up_MED + REAL_NN + " );";

        final String CREATE_TABLE_ADMISSION =
                "CREATE TABLE " + CollegeContract.AdmissionEntry.ADMISSION_TABLE + " (" +
                        CollegeContract.AdmissionEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_CUM_25_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_CUM_50_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_CUM_75_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_MATH_25_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_MATH_50_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_MATH_75_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_ENG_25_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_ENG_50_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.ACT_ENG_75_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.SAT_CUM_50_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.SAT_MATH_25_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.SAT_MATH_50_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.SAT_MATH_75_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.SAT_READ_25_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.SAT_READ_50_PCT + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.AdmissionEntry.SAT_READ_75_PCT + REAL_NN + " );";

        final String CREATE_TABLE_COMPLETION =
                "CREATE TABLE " + CollegeContract.CompletionEntry.COMPLETION_TABLE + " (" +
                        CollegeContract.CompletionEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CompletionEntry.GRADUATION_RATE_4_YEARS + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.CompletionEntry.GRADUATION_RATE_6_YEARS + REAL_NN + " );";

        final String CREATE_TABLE_PLACE =
                "CREATE TABLE " + CollegeContract.PlaceEntry.PLACE_TABLE + " (" +
                        CollegeContract.PlaceEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.COLLEGE_ID + INT_NN + COMMA_SEPERATOR +
                        CollegeContract.CollegeMainEntry.NAME + TEXT_NN + COMMA_SEPERATOR +
                        CollegeContract.PlaceEntry.PLACE_NAME + TEXT + COMMA_SEPERATOR +
                        CollegeContract.PlaceEntry.LATITUDE + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.PlaceEntry.LONGITUDE + REAL_NN + COMMA_SEPERATOR +
                        CollegeContract.PlaceEntry.LOCALE_CODE + INT + COMMA_SEPERATOR +
                        CollegeContract.PlaceEntry.PLACE_ID + TEXT + " );";

        db.execSQL(CREATE_TABLE_COLLEGE_MAIN);
        db.execSQL(CREATE_TABLE_EARNINGS);
        db.execSQL(CREATE_TABLE_COST);
        db.execSQL(CREATE_TABLE_DEBT);
        db.execSQL(CREATE_TABLE_COMPLETION);
        db.execSQL(CREATE_TABLE_ADMISSION);
        db.execSQL(CREATE_TABLE_PLACE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.EarningsEntry.EARNINGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.CostEntry.COST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.DebtEntry.DEBT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.AdmissionEntry.ADMISSION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.CompletionEntry.COMPLETION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CollegeContract.PlaceEntry.PLACE_TABLE);

        onCreate(db);
    }
}