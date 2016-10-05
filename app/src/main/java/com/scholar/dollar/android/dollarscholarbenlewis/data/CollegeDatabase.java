package com.scholar.dollar.android.dollarscholarbenlewis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by bplewis5 on 10/4/16.
 */
@Database(version = CollegeDatabase.VERSION, packageName = "com.scholar.dollar.android.dollarscholarbenlewis.provider")
public class CollegeDatabase {

    public static final int VERSION = 3;
//    @Table(CollegeDetailColumns.class)
//    public static final String COLLEGE_DETAILS = "college_details";
    @Table(CollegeMainColumns.class)
    public static final String COLLEGE_MAIN = "college_main";
    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + COLLEGE_MAIN;

    public CollegeDatabase() {
    }

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }
}


