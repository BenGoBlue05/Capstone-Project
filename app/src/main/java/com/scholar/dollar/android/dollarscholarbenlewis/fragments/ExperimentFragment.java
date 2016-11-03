package com.scholar.dollar.android.dollarscholarbenlewis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scholar.dollar.android.dollarscholarbenlewis.R;

/**
 * Created by benjaminlewis on 11/2/16.
 */

public class ExperimentFragment extends Fragment {

    public ExperimentFragment(){}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.include_pie_chart_grad, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
