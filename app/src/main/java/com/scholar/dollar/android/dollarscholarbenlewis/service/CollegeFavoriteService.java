package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;

/**

 */
public class CollegeFavoriteService extends IntentService {

    private static final String LOG_TAG = CollegeFavoriteService.class.getSimpleName();

    public static final String  ACTION_DATA_UPDATED =
            "com.scholar.dollar.android.dollarscholarbenlewis.ACTION_DATA_UPDATED";
    public CollegeFavoriteService(String name) {
        super(name);
    }

    public CollegeFavoriteService(){
        super(CollegeFavoriteService.class.getSimpleName());
    }

    public void updateWidgets(){
        Context context = getApplicationContext();
        context.sendBroadcast(new Intent(ACTION_DATA_UPDATED).setPackage(context.getPackageName()));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int collegeId = intent.getIntExtra("collegeId", -1);
        int isFavorite = intent.getIntExtra("isFavorite", 0);
        if (collegeId != -1){
            Uri uri = CollegeContract.CollegeMainEntry.buildMainWithCollegeId(collegeId);
            int favorite = isFavorite == 0 ? 1 : 0;
            ContentValues values = new ContentValues();
            values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, favorite);
            getContentResolver().update(uri, values, null, null);
            updateWidgets();
        }
    }
}
