package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
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

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.service.DetailService;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EarningsFragment.class.getSimpleName();
    private CandleStickChart mEarningsCandleChart;
    private ArrayList<CandleEntry> mEntries;
    private int mCollegeId;
    private boolean mIsPublic;

    private int m6yrs25pct;
    private int m6yrs50pct;
    private int m6yrs75pct;

    private int m8yrs25pct;
    private int m8yrs50pct;
    private int m8yrs75pct;

    private int m10yrs25pct;
    private int m10yrs50pct;
    private int m10yrs75pct;

    public EarningsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_earnings, container, false);
        mEntries = new ArrayList<>();
        mEarningsCandleChart = (CandleStickChart) rootView.findViewById(R.id.earnings_candlestick);
        mEarningsCandleChart.setBackgroundColor(Color.WHITE);
        mEarningsCandleChart.getDescription().setEnabled(false);
        YAxis leftAxis = mEarningsCandleChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = mEarningsCandleChart.getAxisRight();
        rightAxis.setEnabled(false);
        mEarningsCandleChart.getLegend().setEnabled(false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollegeId = getActivity().getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        mIsPublic = getActivity().getIntent().getBooleanExtra(Utility.PUBLIC_COLLEGE_KEY, false);
        getLoaderManager().initLoader(Utility.EARNINGS_LOADER, null, this);
        Log.i(LOG_TAG, "COLLEGE ID: " + mCollegeId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Utility.EARNINGS_LOADER:
                return new CursorLoader(getContext(), CollegeContract.EarningsEntry.buildEarningsWithCollegeId(mCollegeId),
                        Utility.EARNINGS_COLUMNS, null, null, null);
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
            getContext().startService(new Intent(getContext(), DetailService.class)
                    .putExtra(Utility.COLLEGE_ID_KEY, mCollegeId)
                    .putExtra(Utility.PUBLIC_COLLEGE_KEY, mIsPublic));
            return;
        }
        Log.i(LOG_TAG, "BEFORE ID CHECKED");
        switch (loader.getId()) {
            case Utility.EARNINGS_LOADER:
                Log.i(LOG_TAG, "LOADER ID IDENTIFIED");
                m6yrs25pct = data.getInt(Utility.COL_EARN_6YRS_25PCT);
                m6yrs50pct = data.getInt(Utility.COL_EARN_6YRS_50PCT);
                m6yrs75pct = data.getInt(Utility.COL_EARN_6YRS_75PCT);

                m8yrs25pct = data.getInt(Utility.COL_EARN_8YRS_25PCT);
                m8yrs50pct = data.getInt(Utility.COL_EARN_8YRS_50PCT);
                m8yrs75pct = data.getInt(Utility.COL_EARN_8YRS_75PCT);

                m10yrs25pct = data.getInt(Utility.COL_EARN_10YRS_25PCT);
                m10yrs50pct = data.getInt(Utility.COL_EARN_10YRS_50PCT);
                m10yrs75pct = data.getInt(Utility.COL_EARN_10YRS_75PCT);

                createEarningsCandleChart();

            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void createEarningsCandleChart() {
        Log.i(LOG_TAG, "6 YEARS 25%: " + m6yrs25pct);
        mEntries.add(new CandleEntry(6f, m6yrs75pct, m6yrs25pct, m6yrs75pct, m6yrs25pct));
        mEntries.add(new CandleEntry(8f, m8yrs75pct, m8yrs25pct, m8yrs75pct, m8yrs25pct));
        mEntries.add(new CandleEntry(10f, m10yrs75pct, m10yrs25pct, m10yrs75pct, m10yrs25pct));
        CandleDataSet dataSet = new CandleDataSet(mEntries, getResources().getString(R.string.earnings));
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setShadowColor(Color.DKGRAY);
        dataSet.setShadowWidth(0.7f);
        dataSet.setDecreasingColor(Color.RED);
        dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        dataSet.setIncreasingColor(Color.rgb(122, 242, 84));
        dataSet.setIncreasingPaintStyle(Paint.Style.STROKE);
        dataSet.setNeutralColor(Color.BLUE);

        CandleData data = new CandleData(dataSet);
        mEarningsCandleChart.setData(data);
        mEarningsCandleChart.invalidate();

    }


}
