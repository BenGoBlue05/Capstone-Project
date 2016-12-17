package com.scholar.dollar.android.dollarscholarbenlewis.service;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

/**
 * Created by benjaminlewis on 12/17/16.
 */

public class PlacesFbJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Bundle extras = job.getExtras();
        assert extras != null;
        startService(new Intent(getApplicationContext(), PlacesService.class)
                .setAction(Utility.ACTION_GET_PLACE_IDS));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

}
