package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scholar.dollar.android.dollarscholarbenlewis.R.string.cost;

public class CostFragment extends Fragment {

    private static final String LOG_TAG = CostFragment.class.getSimpleName();

    private float mCost0to30;
    private float mCost30to48;
    private float mCost48to75;
    private float mCost75to110;
    private float mCost110plus;
    private boolean mIsPublic;

    private float mLoanPct;
    private float mGrantPct;

    private int mTuitionInState;
    private int mTuitionOutState;

    @BindView(R.id.cost_barchart)
    HorizontalBarChart mChart;
    @BindView(R.id.cost_grant_piechart)
    PieChart mGrantPiechart;
    @BindView(R.id.cost_loan_piechart)
    PieChart mLoanPiechart;
    @BindView(R.id.cost_aid_button)
    Button mStudentAidSiteButton;
    @BindView(R.id.cost_in_state)
    TextView mInStateTV;
    @BindView(R.id.cost_out_state)
    TextView mOutStateTV;
    @BindView(R.id.cost_outstate_label_tv)
    TextView mOutStateLabelTV;
    @BindView(R.id.cost_instate_ll)
    LinearLayout mInStateLL;


    public void setmLoanPct(float mLoanPct) {
        this.mLoanPct = mLoanPct;
    }

    public void setmGrantPct(float mGrantPct) {
        this.mGrantPct = mGrantPct;
    }

    public void setmIsPublic(boolean mIsPublic) {
        this.mIsPublic = mIsPublic;
    }

    public CostFragment() {
    }

    public void setmCost0to30(float mCost0to30) {
        this.mCost0to30 = mCost0to30;
    }

    public void setmCost30to48(float mCost30to48) {
        this.mCost30to48 = mCost30to48;
    }

    public void setmCost48to75(float mCost48to75) {
        this.mCost48to75 = mCost48to75;
    }

    public void setmCost75to110(float mCost75to110) {
        this.mCost75to110 = mCost75to110;
    }

    public void setmCost110plus(float mCost110plus) {
        this.mCost110plus = mCost110plus;
    }
    public void setmTuitionInState(int mTuitionInState) {
        this.mTuitionInState = mTuitionInState;
    }

    public void setmTuitionOutState(int mTuitionOutState) {
        this.mTuitionOutState = mTuitionOutState;
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

        mStudentAidSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StudentAidSiteClickListener) getActivity()).onStudentAidSiteButtonClicked();
            }
        });

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

        createGraphs();
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
            set = new BarDataSet(entries, getResources().getString(cost));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);
            data.setBarWidth(.9f);
            mChart.setData(data);
        }
    }

    public void createGraphs() {
        if (mIsPublic) {
            mInStateLL.setVisibility(View.VISIBLE);
            mOutStateLabelTV.setVisibility(View.VISIBLE);
            mInStateTV.setText(Utility.formatThousandsCircle(mTuitionInState));
        } else {
            mInStateLL.setVisibility(View.GONE);
            mOutStateLabelTV.setVisibility(View.GONE);
        }
        mOutStateTV.setText(Utility.formatThousandsCircle(mTuitionOutState));
        createPieChart(mGrantPiechart, mGrantPct);
        createPieChart(mLoanPiechart, mLoanPct);

        setData();
        mChart.setFitBars(true);
        mChart.invalidate();
    }

    private void createPieChart(PieChart pieChart, double pct) {
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(75f);
        pieChart.getLegend().setEnabled(false);
        pieChart.setCenterText(createCenterText(pct));
        pieChart.getDescription().setEnabled(false);

        List<PieEntry> entries = new ArrayList<>();

        float grant = 100f * (float) pct;
        entries.add(new PieEntry(grant, ""));
        entries.add(new PieEntry(100f - grant, ""));

        int[] colors = {ContextCompat.getColor(getContext(), R.color.colorPrimary), Color.LTGRAY};

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);

        PieData pieData = new PieData(set);
        pieData.setValueTextColor(Color.TRANSPARENT);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private SpannableString createCenterText(double pct) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        String str = nf.format(pct);
        SpannableString s = new SpannableString(str);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary)), 0, s.length(), 0);
        return s;
    }

    public interface StudentAidSiteClickListener {
        public void onStudentAidSiteButtonClicked();
    }


}
