package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.content.Intent;
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
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.service.DetailService;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EarningsFragment.class.getSimpleName();
    private ArrayList<CandleEntry> mEntries;
    private int mCollegeId;
    private boolean mIsPublic;

    private float m6yrs25pct;
    private float m6yrs50pct;
    private float m6yrs75pct;

    private float m8yrs25pct;
    private float m8yrs50pct;
    private float m8yrs75pct;

    private float m10yrs25pct;
    private float m10yrs50pct;
    private float m10yrs75pct;

    @BindView(R.id.earnings_10yrs_tv)
    TextView m10yrsTV;
    @BindView(R.id.earnings_8yrs_tv)
    TextView m8yrsTV;
    @BindView(R.id.earnings_6yrs_tv)
    TextView m6yrsTV;
    @BindView(R.id.earnings_chart)
    CombinedChart mChart;

    public EarningsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_earnings, container, false);
        ButterKnife.bind(this, rootView);
        mEntries = new ArrayList<>();
        mChart.setBackgroundColor(Color.WHITE);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});



        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(20000f);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setValueFormatter(new LargeValueFormatter());

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(5f);
        xAxis.setAxisMaximum(11f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(2f);


        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

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
                m6yrs25pct = (float) data.getInt(Utility.COL_EARN_6YRS_25PCT);
                m6yrs50pct = (float) data.getInt(Utility.COL_EARN_6YRS_50PCT);
                m6yrs75pct = (float) data.getInt(Utility.COL_EARN_6YRS_75PCT);

                m8yrs25pct = (float) data.getInt(Utility.COL_EARN_8YRS_25PCT);
                m8yrs50pct = (float) data.getInt(Utility.COL_EARN_8YRS_50PCT);
                m8yrs75pct = (float) data.getInt(Utility.COL_EARN_8YRS_75PCT);

                m10yrs25pct = (float) data.getInt(Utility.COL_EARN_10YRS_25PCT);
                m10yrs50pct = (float) data.getInt(Utility.COL_EARN_10YRS_50PCT);
                m10yrs75pct = (float) data.getInt(Utility.COL_EARN_10YRS_75PCT);

                m6yrsTV.setText(Utility.formatThousandsCircle(m6yrs50pct));
                m8yrsTV.setText(Utility.formatThousandsCircle(m8yrs50pct));
                m10yrsTV.setText(Utility.formatThousandsCircle(m10yrs50pct));

                CombinedData combinedData = new CombinedData();

                combinedData.setData(generateLineData());
                combinedData.setData(generateBarData());

                mChart.setData(combinedData);
                mChart.invalidate();

            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private LineData generateLineData(){
        LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(6f, m6yrs50pct));
        entries.add(new Entry(8f, m8yrs50pct));
        entries.add(new Entry(10f, m10yrs50pct));

        LineDataSet set = new LineDataSet(entries, getResources().getString(R.string.median));
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setValueTextColor(Color.TRANSPARENT);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData(){
        float[] pct25 = {m6yrs25pct, m8yrs25pct, m10yrs25pct};
        float[] pct50 = {m6yrs50pct, m8yrs50pct, m10yrs50pct};
        float[] pct75 = {m6yrs75pct, m8yrs75pct, m10yrs75pct};

        List<BarEntry> pct25Entries = new ArrayList<>();
        List<BarEntry> pct50Entries = new ArrayList<>();
        List<BarEntry> pct75Entries = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            int year = 2*i + 6;
            pct25Entries.add(new BarEntry(year, pct25[i]));
            pct50Entries.add(new BarEntry(year, pct50[i]));
            pct75Entries.add(new BarEntry(year, pct75[i]));
        }

        BarDataSet set25pct = new BarDataSet(pct25Entries,"25th percentile");
        BarDataSet set50pct = new BarDataSet(pct50Entries,getResources().getString(R.string.median));
        BarDataSet set75pct = new BarDataSet(pct75Entries,"75th percentile");


        set25pct.setColor(Color.MAGENTA);
        set50pct.setColor(Color.BLUE);
        set75pct.setColor(Color.GREEN);

        set25pct.setValueTextSize(10f);
        set50pct.setValueTextSize(10f);
        set75pct.setValueTextSize(10f);

        float groupSpace = .98f;
        float barSpace = 0.02f;
        float barWidth = 0.32f;

        BarData d = new BarData(set25pct, set50pct, set75pct);
        d.setValueFormatter(new LargeValueFormatter());

        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(5, groupSpace, barSpace); // start at x = 0

        return d;
    }



}
