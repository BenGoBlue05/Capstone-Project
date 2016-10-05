package com.scholar.dollar.android.dollarscholarbenlewis.model;

import android.support.annotation.Nullable;

/**
 * Created by bplewis5 on 10/3/16.
 */

public class CollegeQuery {
    private boolean publicChecked;
    private boolean privateChecked;
    private String state;



    public CollegeQuery(boolean publicChecked, boolean privateChecked, @Nullable String state) {
        this.publicChecked = publicChecked;
        this.privateChecked = privateChecked;
        this.state = state;
    }
}
