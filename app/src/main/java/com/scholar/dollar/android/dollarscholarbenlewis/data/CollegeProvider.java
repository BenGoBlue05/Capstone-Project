package com.scholar.dollar.android.dollarscholarbenlewis.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract.AdmissionEntry.ADMISSION_TABLE;
import static com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract.CostEntry.COST_TABLE;
import static com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract.DebtEntry.DEBT_TABLE;
import static com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract.EarningsEntry.EARNINGS_TABLE;
import static com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract.PlaceEntry.PLACE_TABLE;

public class CollegeProvider extends ContentProvider {

    static final int COLLEGE_MAIN = 100;
    static final int COLLEGE_MAIN_WITH_ID = 101;
    static final int EARNINGS = 200;
    static final int EARNINGS_WITH_ID = 201;
    static final int COST = 300;
    static final int COST_WITH_ID = 301;
    static final int DEBT = 400;
    static final int DEBT_WITH_ID = 401;
    static final int ADMISSION  = 600;
    static final int ADMISSION_WITH_ID = 601;
    static final int PLACE = 700;
    static final int PLACE_WITH_ID = 701;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String SELECTION_COLLEGE_MAIN_ID = CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE
            + "." + CollegeContract.CollegeMainEntry.COLLEGE_ID + " = ? ";
    private static final String SELECTION_EARNINGS_COLLEGE_ID = EARNINGS_TABLE
            + "." + CollegeContract.CollegeMainEntry.COLLEGE_ID + " = ? ";
    private static final String SELECTION_COST_COLLEGE_ID = COST_TABLE
            + "." + CollegeContract.CollegeMainEntry.COLLEGE_ID + " = ? ";
    private static final String SELECTION_DEBT_COLLEGE_ID = DEBT_TABLE
            + "." + CollegeContract.CollegeMainEntry.COLLEGE_ID + " = ? ";
    private static final String SELECTION_ADMISSION_COLLEGE_ID = ADMISSION_TABLE
            + "." + CollegeContract.CollegeMainEntry.COLLEGE_ID + " = ? ";
    private static final String SELECTION_PLACE_COLLEGE_ID = PLACE_TABLE
            + "." + CollegeContract.CollegeMainEntry.COLLEGE_ID + " = ? ";
    private CollegeDbHelper mHelper;

    public CollegeProvider() {
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CollegeContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, CollegeContract.PATH_COLLEGE_MAIN, COLLEGE_MAIN);
        matcher.addURI(authority, CollegeContract.PATH_COLLEGE_MAIN + "/#", COLLEGE_MAIN_WITH_ID);
        matcher.addURI(authority, CollegeContract.PATH_EARNINGS, EARNINGS);
        matcher.addURI(authority, CollegeContract.PATH_EARNINGS + "/#", EARNINGS_WITH_ID);
        matcher.addURI(authority, CollegeContract.PATH_COST, COST);
        matcher.addURI(authority, CollegeContract.PATH_COST + "/#", COST_WITH_ID);
        matcher.addURI(authority, CollegeContract.PATH_DEBT, DEBT);
        matcher.addURI(authority, CollegeContract.PATH_DEBT + "/#", DEBT_WITH_ID);
        matcher.addURI(authority, CollegeContract.PATH_ADMISSION, ADMISSION);
        matcher.addURI(authority, CollegeContract.PATH_ADMISSION + "/#", ADMISSION_WITH_ID);
        matcher.addURI(authority, CollegeContract.PATH_PLACE, PLACE);
        matcher.addURI(authority, CollegeContract.PATH_PLACE + "/#", PLACE_WITH_ID);

        return matcher;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case COLLEGE_MAIN:
                rowsDeleted = db.delete(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE, selection, selectionArgs);
                break;
            case COLLEGE_MAIN_WITH_ID:
                rowsDeleted = db.delete(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE, SELECTION_COLLEGE_MAIN_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case EARNINGS:
                rowsDeleted = db.delete(EARNINGS_TABLE, selection, selectionArgs);
                break;
            case EARNINGS_WITH_ID:
                rowsDeleted = db.delete(EARNINGS_TABLE, SELECTION_EARNINGS_COLLEGE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case COST:
                rowsDeleted = db.delete(COST_TABLE, selection, selectionArgs);
                break;
            case COST_WITH_ID:
                rowsDeleted = db.delete(COST_TABLE, SELECTION_COST_COLLEGE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case DEBT:
                rowsDeleted = db.delete(DEBT_TABLE, selection, selectionArgs);
                break;
            case DEBT_WITH_ID:
                rowsDeleted = db.delete(DEBT_TABLE, SELECTION_DEBT_COLLEGE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case ADMISSION:
                rowsDeleted = db.delete(ADMISSION_TABLE, selection, selectionArgs);
                break;
            case ADMISSION_WITH_ID:
                rowsDeleted = db.delete(ADMISSION_TABLE, SELECTION_ADMISSION_COLLEGE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case PLACE:
                rowsDeleted = db.delete(PLACE_TABLE, selection, selectionArgs);
                break;
            case PLACE_WITH_ID:
                rowsDeleted = db.delete(PLACE_TABLE, SELECTION_PLACE_COLLEGE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case COLLEGE_MAIN:
                return CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_TYPE;
            case COLLEGE_MAIN_WITH_ID:
                return CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_ITEM_TYPE;
            case EARNINGS:
                return CollegeContract.EarningsEntry.EARNINGS_CONTENT_TYPE;
            case EARNINGS_WITH_ID:
                return CollegeContract.EarningsEntry.EARNINGS_CONTENT_ITEM_TYPE;
            case COST:
                return CollegeContract.CostEntry.COST_CONTENT_TYPE;
            case COST_WITH_ID:
                return CollegeContract.CostEntry.COST_CONTENT_ITEM_TYPE;
            case DEBT:
                return CollegeContract.DebtEntry.DEBT_CONTENT_TYPE;
            case DEBT_WITH_ID:
                return CollegeContract.DebtEntry.DEBT_CONTENT_ITEM_TYPE;
            case ADMISSION:
                return CollegeContract.AdmissionEntry.ADMISSION_CONTENT_TYPE;
            case ADMISSION_WITH_ID:
                return CollegeContract.AdmissionEntry.ADMISSION_CONTENT_ITEM_TYPE;
            case PLACE:
                return CollegeContract.PlaceEntry.PLACE_CONTENT_TYPE;
            case PLACE_WITH_ID:
                return CollegeContract.PlaceEntry.PLACE_CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Uri returnUri;
        long _id;
        switch (sUriMatcher.match(uri)) {
            case COLLEGE_MAIN:
                _id = db.insert(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE, null, values);
                if (_id > 0) {
                    returnUri = CollegeContract.CollegeMainEntry.buildCollegeMainUri(_id);
                } else {
                    throw new SQLException("FAILED TO INSERT ROW INTO " + uri);
                }
                break;
            case EARNINGS:
                _id = db.insert(EARNINGS_TABLE, null, values);
                if (_id > 0) {
                    returnUri = CollegeContract.EarningsEntry.buildEarningsUri(_id);
                } else {
                    throw new SQLException("FAILED TO INSERT ROW INTO " + uri);
                }
                break;
            case COST:
                _id = db.insert(COST_TABLE, null, values);
                if (_id > 0) {
                    returnUri = CollegeContract.CostEntry.buildCostUri(_id);
                } else {
                    throw new SQLException("FAILED TO INSERT ROW INTO " + uri);
                }
                break;
            case DEBT:
                _id = db.insert(DEBT_TABLE, null, values);
                if (_id > 0) {
                    returnUri = CollegeContract.DebtEntry.buildDebtUri(_id);
                } else {
                    throw new SQLException("FAILED TO INSERT ROW INTO " + uri);
                }
                break;
            case ADMISSION:
                _id = db.insert(ADMISSION_TABLE, null, values);
                if (_id > 0) {
                    returnUri = CollegeContract.AdmissionEntry.buildAdmissionUri(_id);
                } else {
                    throw new SQLException("FAILED TO INSERT ROW INTO " + uri);
                }
                break;
            case PLACE:
                _id = db.insert(PLACE_TABLE, null, values);
                if (_id > 0) {
                    returnUri = CollegeContract.PlaceEntry.buildPlaceUri(_id);
                } else {
                    throw new SQLException("FAILED TO INSERT ROW INTO " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mHelper = new CollegeDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor returnCursor;
        String collegeId;
        switch (sUriMatcher.match(uri)) {
            case COLLEGE_MAIN:
                returnCursor = db.query(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COLLEGE_MAIN_WITH_ID:
                collegeId = uri.getLastPathSegment();
                returnCursor = db.query(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        projection,
                        SELECTION_COLLEGE_MAIN_ID,
                        new String[]{collegeId},
                        null, null, sortOrder);
                break;
            case EARNINGS:
                returnCursor = db.query(EARNINGS_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EARNINGS_WITH_ID:
                collegeId = uri.getLastPathSegment();
                returnCursor = db.query(EARNINGS_TABLE,
                        projection,
                        SELECTION_EARNINGS_COLLEGE_ID,
                        new String[]{collegeId},
                        null, null, sortOrder);
                break;
            case COST:
                returnCursor = db.query(COST_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COST_WITH_ID:
                collegeId = uri.getLastPathSegment();
                returnCursor = db.query(COST_TABLE,
                        projection,
                        SELECTION_COST_COLLEGE_ID,
                        new String[]{collegeId},
                        null, null, sortOrder);
                break;
            case DEBT:
                returnCursor = db.query(DEBT_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DEBT_WITH_ID:
                collegeId = uri.getLastPathSegment();
                returnCursor = db.query(DEBT_TABLE,
                        projection,
                        SELECTION_DEBT_COLLEGE_ID,
                        new String[]{collegeId},
                        null, null, sortOrder);
                break;
            case ADMISSION:
                returnCursor = db.query(ADMISSION_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ADMISSION_WITH_ID:
                collegeId = uri.getLastPathSegment();
                returnCursor = db.query(ADMISSION_TABLE,
                        projection,
                        SELECTION_ADMISSION_COLLEGE_ID,
                        new String[]{collegeId},
                        null, null, sortOrder);
                break;

            case PLACE:
                returnCursor = db.query(PLACE_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PLACE_WITH_ID:
                collegeId = uri.getLastPathSegment();
                returnCursor = db.query(PLACE_TABLE,
                        projection,
                        SELECTION_PLACE_COLLEGE_ID,
                        new String[]{collegeId},
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case COLLEGE_MAIN:
                rowsUpdated = db.update(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        values, selection, selectionArgs);
                break;
            case COLLEGE_MAIN_WITH_ID:
                rowsUpdated = db.update(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        values, SELECTION_COLLEGE_MAIN_ID, new String[]{uri.getLastPathSegment()});
                break;
            case EARNINGS:
                rowsUpdated = db.update(EARNINGS_TABLE,
                        values, selection, selectionArgs);
                break;
            case EARNINGS_WITH_ID:
                rowsUpdated = db.update(EARNINGS_TABLE,
                        values, SELECTION_EARNINGS_COLLEGE_ID, new String[]{uri.getLastPathSegment()});
                break;
            case COST:
                rowsUpdated = db.update(COST_TABLE,
                        values, selection, selectionArgs);
                break;
            case COST_WITH_ID:
                rowsUpdated = db.update(COST_TABLE,
                        values, SELECTION_COST_COLLEGE_ID, new String[]{uri.getLastPathSegment()});
                break;
            case DEBT:
                rowsUpdated = db.update(DEBT_TABLE,
                        values, selection, selectionArgs);
                break;
            case DEBT_WITH_ID:
                rowsUpdated = db.update(DEBT_TABLE,
                        values, SELECTION_DEBT_COLLEGE_ID, new String[]{uri.getLastPathSegment()});
                break;
            case ADMISSION:
                rowsUpdated = db.update(ADMISSION_TABLE,
                        values, selection, selectionArgs);
                break;
            case ADMISSION_WITH_ID:
                rowsUpdated = db.update(ADMISSION_TABLE,
                        values, SELECTION_ADMISSION_COLLEGE_ID, new String[]{uri.getLastPathSegment()});
                break;
            case PLACE:
                rowsUpdated = db.update(PLACE_TABLE,
                        values, selection, selectionArgs);
                break;
            case PLACE_WITH_ID:
                rowsUpdated = db.update(PLACE_TABLE,
                        values, SELECTION_PLACE_COLLEGE_ID, new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        int returnCount = 0;
        switch (sUriMatcher.match(uri)) {
            case COLLEGE_MAIN:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case PLACE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CollegeContract.PlaceEntry.PLACE_TABLE, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}