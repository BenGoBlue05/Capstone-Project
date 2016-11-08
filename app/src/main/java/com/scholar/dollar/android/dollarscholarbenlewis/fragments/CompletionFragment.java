package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletionFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener{

    private static final String LOG_TAG = CompletionFragment.class.getSimpleName();
    private static int mCollegeId;
    @BindView(R.id.comp_piechart)
    PieChart mChart;

    public CompletionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.include_comp_piechart, container, false);
        ButterKnife.bind(this, rootView);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setOnChartValueSelectedListener(this);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mChart.setEntryLabelColor(Color.WHITE);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollegeId = getActivity().getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        getActivity().getSupportLoaderManager().initLoader(Utility.COLLEGE_MAIN_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case Utility.COLLEGE_MAIN_LOADER:
                return new CursorLoader(getContext(), CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                        Utility.COLLEGE_COLUMNS, null, null, null);
            default:
                Log.i(LOG_TAG, "LOADER ID NOT FOUND");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()){
            float comp4yrs  = 100f * (float) data.getDouble(Utility.GRAD_RATE_4_YEARS);
            float comp6yrs  = 100f * (float) data.getDouble(Utility.GRAD_RATE_6_YEARS);
            float noComp = 100f - comp6yrs;

            Log.i(LOG_TAG, "4 YEAR GRAD RATE: " + comp4yrs);
            Log.i(LOG_TAG, "6 YEAR GRAD RATE: " + comp6yrs);
            Log.i(LOG_TAG, "NONCOMPLETION RATE: " + noComp);

            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(comp4yrs, getResources().getString(R.string.four_years)));
            entries.add(new PieEntry(comp6yrs - comp4yrs, getResources().getString(R.string.six_years)));
            entries.add(new PieEntry(noComp, getResources().getString(R.string.didNotGraduate)));

            PieDataSet dataSet = new PieDataSet(entries, getResources().getString(R.string.graduation_rates));
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.GREEN);
            colors.add(Color.BLUE);
            colors.add(Color.GRAY);

            dataSet.setColors(colors);

            PieData pieData = new PieData(dataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(11f);
            pieData.setValueTextColor(Color.WHITE);

            mChart.setData(pieData);

            mChart.highlightValues(null);

            mChart.invalidate();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
