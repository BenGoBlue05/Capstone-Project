package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.database.Cursor;
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
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CostFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CostFragment.class.getSimpleName();
    private int mCollegeId;
    private boolean mIsPublic;

    private float mCost0to30;
    private float mCost30to48;
    private float mCost48to75;
    private float mCost75to110;
    private float mCost110plus;

    @BindView(R.id.cost_med10_tv)
    TextView mCostTV;
    @BindView(R.id.cost_barchart)
    HorizontalBarChart mChart;


    public CostFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollegeId = getActivity().getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        getActivity().getSupportLoaderManager().initLoader(Utility.COST_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cost, container, false);
        ButterKnife.bind(this, rootView);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);


        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        return rootView;
    }

    private void setData() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, mCost0to30));
        entries.add(new BarEntry(1f, mCost30to48));
        entries.add(new BarEntry(2f, mCost48to75));
        entries.add(new BarEntry(3f, mCost75to110));
        entries.add(new BarEntry(4f, mCost110plus));

        BarDataSet set;


        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(entries);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(entries, getResources().getString(R.string.cost));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);
            data.setBarWidth(.9f);
            mChart.setData(data);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Utility.COLLEGE_MAIN_LOADER:
                return new CursorLoader(getContext(), CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                        Utility.COLLEGE_COLUMNS, null, null, null);
            case Utility.COST_LOADER:
                return new CursorLoader(getContext(), CollegeContract.CostEntry.buildCostWithCollegeId(mCollegeId),
                        Utility.COST_COLUMNS, null, null, null);
            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "ON LOAD FINISHED STARTED");
        if (data == null || !data.moveToFirst()) {
            Log.i(LOG_TAG, "CURSOR IS NULL");
            return;
        }
        Log.i(LOG_TAG, "BEFORE ID CHECKED");
        switch (loader.getId()) {
            case Utility.COLLEGE_MAIN_LOADER:
                mCostTV.setText(data.getInt(Utility.TUITION_OUT_STATE));
            case Utility.COST_LOADER:
                Log.i(LOG_TAG, "LOADER ID IDENTIFIED");
                mCost0to30 = (float) data.getInt(Utility.COL_COST_0to30);
                mCost30to48 = (float) data.getInt(Utility.COL_COST_30to48);
                mCost48to75 = (float) data.getInt(Utility.COL_COST_48to75);
                mCost75to110 = (float) data.getInt(Utility.COL_COST_75to110);
                mCost110plus = (float) data.getInt(Utility.COL_COST_110plus);

                setData();
                mChart.setFitBars(true);
                mChart.invalidate();

            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


}
