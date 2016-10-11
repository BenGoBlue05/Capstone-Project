package com.scholar.dollar.android.dollarscholarbenlewis.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scholar.dollar.android.dollarscholarbenlewis.R;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(NullPointerException e){
            Log.i(LOG_TAG, "NULL POINTER: " + e);
        }
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail_container, new DetailFragment())
                    .commit();
        }
    }
}
