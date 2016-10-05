package com.scholar.dollar.android.dollarscholarbenlewis.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class CollegeProvider extends ContentProvider {

    static final int COLLEGE_MAIN = 100;
    static final int COLLEGE_MAIN_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CollegeDbHelper mHelper;

    private static final String SELECTION_COLLEGE_ID = CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE
            + "." + CollegeContract.CollegeMainEntry.COLLEGE_ID + " = ? ";

    public CollegeProvider() {
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CollegeContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, CollegeContract.PATH_COLLEGE_MAIN, COLLEGE_MAIN);
        matcher.addURI(authority, CollegeContract.PATH_COLLEGE_MAIN + "/#", COLLEGE_MAIN_WITH_ID);
        return matcher;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)){
            case COLLEGE_MAIN:
                rowsDeleted = db.delete(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE, selection, selectionArgs);
                break;
            case COLLEGE_MAIN_WITH_ID:
                rowsDeleted = db.delete(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE, SELECTION_COLLEGE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (rowsDeleted !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case COLLEGE_MAIN:
                return CollegeContract.CollegeMainEntry.CONTENT_TYPE;
            case COLLEGE_MAIN_WITH_ID:
                return CollegeContract.CollegeMainEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)){
            case COLLEGE_MAIN:
                long _id = db.insert(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE, null, values);
                if (_id > 0){
                    returnUri = CollegeContract.CollegeMainEntry.buildCollegeMainUri(_id);
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
        switch (sUriMatcher.match(uri)){
            case COLLEGE_MAIN:
                returnCursor = db.query(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COLLEGE_MAIN_WITH_ID:
                String collegeId = uri.getLastPathSegment();
                returnCursor = db.query(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        projection,
                        SELECTION_COLLEGE_ID,
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
        switch (sUriMatcher.match(uri)){
            case COLLEGE_MAIN:
                rowsUpdated = db.update(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        values, selection, selectionArgs);
                break;
            case COLLEGE_MAIN_WITH_ID:
                rowsUpdated = db.update(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_TABLE,
                        values, SELECTION_COLLEGE_ID, new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case COLLEGE_MAIN:
                db.beginTransaction();
                int returnCount = 0;
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

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
