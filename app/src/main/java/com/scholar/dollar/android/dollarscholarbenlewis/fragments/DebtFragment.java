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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DebtFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DebtFragment.class.getSimpleName();

    private int mCollegeId;

    private float mLoanPrincipal;
    private float mCompleters;
    private float mNoncompleters;
    private float mMonthly;
    private float m0to30;
    private float m30to75;
    private float m75plus;

    @BindView(R.id.debt_barchart)
    BarChart mChart;

    public DebtFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_debt, container, false);
        ButterKnife.bind(this, rootView);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollegeId = getActivity().getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        getActivity().getSupportLoaderManager().initLoader(Utility.DEBT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), CollegeContract.DebtEntry.buildDebtWithCollegeId(mCollegeId),
                Utility.DEBT_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || !data.moveToFirst()){
            Log.i(LOG_TAG, "CURSOR IS NULL");
            return;
        }

        mLoanPrincipal = (float) data.getDouble(Utility.COL_DEBT_LOAN_PPL);
        mCompleters = (float) data.getDouble(Utility.COL_DEBT_COMPLETERS);
        mNoncompleters = (float) data.getDouble(Utility.COL_DEBT_NONCOMPLETERS);
        mMonthly = (float) data.getDouble(Utility.COL_DEBT_MONTHLY);
        m0to30 = (float) data.getDouble(Utility.COL_DEBT_0to30);
        m30to75 = (float) data.getDouble(Utility.COL_DEBT_30to75);
        m75plus = (float) data.getDouble(Utility.COL_DEBT_75plus);

        setData();
        mChart.setFitBars(true);
        mChart.invalidate();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void setData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        BarDataSet barDataSet;

        entries.add(new BarEntry(0f, mLoanPrincipal));
        entries.add(new BarEntry(2f, mCompleters));
        entries.add(new BarEntry(3f, mNoncompleters));
        entries.add(new BarEntry(5f, mMonthly));
        entries.add(new BarEntry(7f, m0to30));
        entries.add(new BarEntry(8f, m30to75));
        entries.add(new BarEntry(9f, m75plus));

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(entries);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            barDataSet = new BarDataSet(entries, getResources().getString(R.string.debt));
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

}
