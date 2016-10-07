package com.scholar.dollar.android.dollarscholarbenlewis.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scholar.dollar.android.dollarscholarbenlewis.DetailFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail_container, new DetailFragment())
                    .commit();
        }
    }
}
