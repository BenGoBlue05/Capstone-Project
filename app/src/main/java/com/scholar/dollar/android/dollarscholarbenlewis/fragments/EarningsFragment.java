package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningsFragment extends Fragment{

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

    public int getmCollegeId() {
        return mCollegeId;
    }

    public void setmCollegeId(int mCollegeId) {
        this.mCollegeId = mCollegeId;
    }

    public boolean ismIsPublic() {
        return mIsPublic;
    }

    public void setmIsPublic(boolean mIsPublic) {
        this.mIsPublic = mIsPublic;
    }

    public float getM6yrs25pct() {
        return m6yrs25pct;
    }

    public void setM6yrs25pct(float m6yrs25pct) {
        this.m6yrs25pct = m6yrs25pct;
    }

    public float getM6yrs50pct() {
        return m6yrs50pct;
    }

    public void setM6yrs50pct(float m6yrs50pct) {
        this.m6yrs50pct = m6yrs50pct;
    }

    public float getM6yrs75pct() {
        return m6yrs75pct;
    }

    public void setM6yrs75pct(float m6yrs75pct) {
        this.m6yrs75pct = m6yrs75pct;
    }

    public float getM8yrs25pct() {
        return m8yrs25pct;
    }

    public void setM8yrs25pct(float m8yrs25pct) {
        this.m8yrs25pct = m8yrs25pct;
    }

    public float getM8yrs50pct() {
        return m8yrs50pct;
    }

    public void setM8yrs50pct(float m8yrs50pct) {
        this.m8yrs50pct = m8yrs50pct;
    }

    public float getM8yrs75pct() {
        return m8yrs75pct;
    }

    public void setM8yrs75pct(float m8yrs75pct) {
        this.m8yrs75pct = m8yrs75pct;
    }

    public float getM10yrs25pct() {
        return m10yrs25pct;
    }

    public void setM10yrs25pct(float m10yrs25pct) {
        this.m10yrs25pct = m10yrs25pct;
    }

    public float getM10yrs50pct() {
        return m10yrs50pct;
    }

    public void setM10yrs50pct(float m10yrs50pct) {
        this.m10yrs50pct = m10yrs50pct;
    }

    public float getM10yrs75pct() {
        return m10yrs75pct;
    }

    public void setM10yrs75pct(float m10yrs75pct) {
        this.m10yrs75pct = m10yrs75pct;
    }

    public TextView getM10yrsTV() {
        return m10yrsTV;
    }

    public void setM10yrsTV(TextView m10yrsTV) {
        this.m10yrsTV = m10yrsTV;
    }

    public TextView getM8yrsTV() {
        return m8yrsTV;
    }

    public void setM8yrsTV(TextView m8yrsTV) {
        this.m8yrsTV = m8yrsTV;
    }

    public TextView getM6yrsTV() {
        return m6yrsTV;
    }

    public void setM6yrsTV(TextView m6yrsTV) {
        this.m6yrsTV = m6yrsTV;
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

        createGraphs();

        return rootView;
    }

    public void createGraphs(){
        m6yrsTV.setText(Utility.formatThousandsCircle(m6yrs50pct));
        m8yrsTV.setText(Utility.formatThousandsCircle(m8yrs50pct));
        m10yrsTV.setText(Utility.formatThousandsCircle(m10yrs50pct));
        CombinedData combinedData = new CombinedData();

        combinedData.setData(generateLineData());
        combinedData.setData(generateBarData());

        mChart.setData(combinedData);
        mChart.invalidate();
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
