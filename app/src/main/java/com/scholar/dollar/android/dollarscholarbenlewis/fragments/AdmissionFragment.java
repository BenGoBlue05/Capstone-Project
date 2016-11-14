package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdmissionFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener {

    private static final String LOG_TAG = AdmissionFragment.class.getSimpleName();

    private static final int ADMISSION_LOADER = 100;
    private int mCollegeId;
    private float mAct25;
    private float mAct50;
    private float mAct75;
    private float mActMath25;
    private float mActMath50;
    private float mActMath75;
    private float mActEng25;
    private float mActEng50;
    private float mActEng75;
    private float mSat;
    private float mSatMath25;
    private float mSatMath50;
    private float mSatMath75;
    private float mSatRead25;
    private float mSatRead50;
    private float mSatRead75;

    @BindView(R.id.admission_barchart)
    BarChart mChart;

    public AdmissionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admission, container, false);
        ButterKnife.bind(this, rootView);

        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(18f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        mCollegeId = getActivity().getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        getLoaderManager().initLoader(ADMISSION_LOADER, null, this);
        return rootView;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "COLLEGE ID: " + mCollegeId);
        return new CursorLoader(getContext(), CollegeContract.AdmissionEntry.buildAdmissionWithCollegeId(mCollegeId),
                Utility.ADMISSION_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || !data.moveToFirst()) {
            Log.i(LOG_TAG, "CURSOR IS NULL");
            return;
        }

        mAct25 = (float) data.getDouble(Utility.COL_ADMSN_ACT25);
        mAct50 = (float) data.getDouble(Utility.COL_ADMSN_ACT50);
        mAct75 = (float) data.getDouble(Utility.COL_ADMSN_ACT75);
        mActMath25 = (float) data.getDouble(Utility.COL_ADMSN_ACT_MATH25);
        mActMath50 = (float) data.getDouble(Utility.COL_ADMSN_ACT_MATH50);
        mActMath75 = (float) data.getDouble(Utility.COL_ADMSN_ACT_MATH75);
        mActEng25 = (float) data.getDouble(Utility.COL_ADMSN_ACT_ENG25);
        mActEng50 = (float) data.getDouble(Utility.COL_ADMSN_ACT_ENG50);
        mActEng75 = (float) data.getDouble(Utility.COL_ADMSN_ACT_ENG75);
        mSat = (float) data.getDouble(Utility.COL_ADMSN_SAT50);
        mSatMath25 = (float) data.getDouble(Utility.COL_ADMSN_SAT_MATH25);
        mSatMath50 = (float) data.getDouble(Utility.COL_ADMSN_SAT_MATH50);
        mSatMath75 = (float) data.getDouble(Utility.COL_ADMSN_SAT_MATH75);
        mSatRead25 = (float) data.getDouble(Utility.COL_ADMSN_SAT_READ25);
        mSatRead50 = (float) data.getDouble(Utility.COL_ADMSN_SAT_READ50);
        mSatRead75 = (float) data.getDouble(Utility.COL_ADMSN_SAT_READ75);

        setData(true);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void setData(boolean isACT) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        if (isACT) {
            entries.add(new BarEntry(0, new float[] {mAct25, mAct50 - mAct25, mAct75 - mAct50, 36 - mAct75}));
            entries.add(new BarEntry(1, new float[] {mActMath25, mActMath50 - mActMath25, mActMath75 - mActMath50, 36 - mActMath75}));
            entries.add(new BarEntry(2, new float[] {mActEng25, mActEng50 - mActEng25, mActEng75 - mActEng50, 36 - mActEng75}));
        } else{
            entries.add(new BarEntry(0, new float[] {mSatMath25, mSatMath50, mSatMath75, 36}));
            entries.add(new BarEntry(1, new float[] {mSatRead25, mSatRead50, mSatRead75, 36}));
        }

        BarDataSet set1;

        int[] colors = {Color.GRAY, Color.YELLOW, Color.GREEN, Color.GRAY};

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entries, getResources().getString(R.string.act_middle_50));
            set1.setColors(colors);
            set1.setStackLabels(new String[]{"25", "50", "75", getResources().getString(R.string.max_score)});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();
    }

}
