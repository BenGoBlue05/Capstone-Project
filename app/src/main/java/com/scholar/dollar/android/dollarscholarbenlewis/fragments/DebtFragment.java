package com.scholar.dollar.android.dollarscholarbenlewis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DebtFragment extends Fragment {

    private static final String LOG_TAG = DebtFragment.class.getSimpleName();

    private float mLoanPrincipal;
    private float mCompleters;
    private float mNoncompleters;
    private float mMonthly;
    private float m0to30;
    private float m30to75;
    private float m75plus;


    public void setmLoanPrincipal(float mLoanPrincipal) {
        this.mLoanPrincipal = mLoanPrincipal;
    }

    public void setmCompleters(float mCompleters) {
        this.mCompleters = mCompleters;
    }

    public void setmNoncompleters(float mNoncompleters) {
        this.mNoncompleters = mNoncompleters;
    }

    public void setmMonthly(float mMonthly) {
        this.mMonthly = mMonthly;
    }

    public void setM0to30(float m0to30) {
        this.m0to30 = m0to30;
    }

    public void setM30to75(float m30to75) {
        this.m30to75 = m30to75;
    }

    public void setM75plus(float m75plus) {
        this.m75plus = m75plus;
    }

    @BindView(R.id.debt_barchart)
    BarChart mChart;

    public DebtFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_debt, container, false);
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

        createBarGraph();

        return rootView;
    }

    public void createBarGraph() {
        if (mChart != null) {
            setData();
            mChart.setFitBars(true);
            mChart.invalidate();
        }
    }

    private void setData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        BarDataSet barDataSet;

        entries.add(new BarEntry(0f, mLoanPrincipal));
        entries.add(new BarEntry(2f, mCompleters));
        entries.add(new BarEntry(3f, mNoncompleters));
        entries.add(new BarEntry(5f, mMonthly));
        entries.add(new BarEntry(7f, m0to30));
        entries.add(new BarEntry(8f, m30to75));
        entries.add(new BarEntry(9f, m75plus));
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
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
