package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.graphics.Color;
import android.os.Bundle;
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
import com.scholar.dollar.android.dollarscholarbenlewis.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdmissionFragment extends Fragment {

    private static final String LOG_TAG = AdmissionFragment.class.getSimpleName();
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


    public void setmAct25(float mAct25) {
        this.mAct25 = mAct25;
    }

    public void setmAct50(float mAct50) {
        this.mAct50 = mAct50;
    }

    public void setmAct75(float mAct75) {
        this.mAct75 = mAct75;
    }

    public void setmActMath25(float mActMath25) {
        this.mActMath25 = mActMath25;
    }

    public void setmActMath50(float mActMath50) {
        this.mActMath50 = mActMath50;
    }

    public void setmActMath75(float mActMath75) {
        this.mActMath75 = mActMath75;
    }

    public void setmActEng25(float mActEng25) {
        this.mActEng25 = mActEng25;
    }

    public void setmActEng50(float mActEng50) {
        this.mActEng50 = mActEng50;
    }

    public void setmActEng75(float mActEng75) {
        this.mActEng75 = mActEng75;
    }

    public void setmSat(float mSat) {
        this.mSat = mSat;
    }

    public void setmSatMath25(float mSatMath25) {
        this.mSatMath25 = mSatMath25;
    }

    public void setmSatMath50(float mSatMath50) {
        this.mSatMath50 = mSatMath50;
    }

    public void setmSatMath75(float mSatMath75) {
        this.mSatMath75 = mSatMath75;
    }

    public void setmSatRead25(float mSatRead25) {
        this.mSatRead25 = mSatRead25;
    }

    public void setmSatRead50(float mSatRead50) {
        this.mSatRead50 = mSatRead50;
    }

    public void setmSatRead75(float mSatRead75) {
        this.mSatRead75 = mSatRead75;
    }

    @BindView(R.id.admission_barchart)
    BarChart mChart;

    public AdmissionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admission, container, false);
        ButterKnife.bind(this, rootView);
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
        setData(true);
        return rootView;
    }


    public void setData(boolean isACT) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        if (isACT) {
            entries.add(new BarEntry(0, new float[]{mAct25, mAct50 - mAct25, mAct75 - mAct50, 36 - mAct75}));
            entries.add(new BarEntry(1, new float[]{mActMath25, mActMath50 - mActMath25, mActMath75 - mActMath50, 36 - mActMath75}));
            entries.add(new BarEntry(2, new float[]{mActEng25, mActEng50 - mActEng25, mActEng75 - mActEng50, 36 - mActEng75}));
        } else {
            entries.add(new BarEntry(0, new float[]{mSatMath25, mSatMath50, mSatMath75, 36}));
            entries.add(new BarEntry(1, new float[]{mSatRead25, mSatRead50, mSatRead75, 36}));
        }

        BarDataSet set1;

        int[] colors = {Color.GRAY, Color.YELLOW, Color.GREEN, Color.GRAY};

        if (mChart != null){
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
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

}
