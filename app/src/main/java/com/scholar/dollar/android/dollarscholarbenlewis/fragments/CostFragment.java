package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CostFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CostFragment.class.getSimpleName();
    private int mCollegeId;

    private float mCost0to30;
    private float mCost30to48;
    private float mCost48to75;
    private float mCost75to110;
    private float mCost110plus;
    private boolean mIsPublic;

    public static final String[] COST_COLUMNS = {
            CollegeContract.CostEntry.COST_FAM_0to30,
            CollegeContract.CostEntry.COST_FAM_30to48,
            CollegeContract.CostEntry.COST_FAM_48to75,
            CollegeContract.CostEntry.COST_FAM_75to110,
            CollegeContract.CostEntry.COST_FAM_OVER_110,
            CollegeContract.CostEntry.LOAN_STUDENTS_PCT,
            CollegeContract.CostEntry.PELL_STUDENTS_PCT,
            CollegeContract.CostEntry.PRICE_CALCULATOR_URL
    };

    public static final int COL_COST_0to30 = 0;
    public static final int COL_COST_30to48 = 1;
    public static final int COL_COST_48to75 = 2;
    public static final int COL_COST_75to110 = 3;
    public static final int COL_COST_110plus = 4;
    public static final int COL_LOAN_PCT = 5;
    public static final int COL_GRANT_PCT = 6;
    public static final int COL_PRICE_CALC = 7;

    private static final String[] TUITION_PROJECTION =
            {CollegeContract.CollegeMainEntry.TUITION_IN_STATE, CollegeContract.CollegeMainEntry.TUITION_OUT_STATE};

    private static final int COL_IN_STATE = 0;
    private static final int COL_OUT_STATE = 1;

    @BindView(R.id.cost_barchart)
    BarChart mChart;

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
    @BindView(R.id.cost_calculator_button)
    Button mCalculatorButton;


    private static final int COLLEGE_MAIN_LOADER = 100;
    private static final int COST_LOADER = 200;

    final String[] QUINTILES = {"0-30k", "30-48k", "48-75k", "75-110k", "110k+"};


    public CostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cost, container, false);
        ButterKnife.bind(this, rootView);

        mChart.setBackgroundColor(Color.WHITE);
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);

        mStudentAidSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CostClickListener) getActivity()).onStudentAidSiteButtonClicked();
            }
        });



        mGrantPiechart.setRotationEnabled(false);
        mLoanPiechart.setRotationEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(12f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return QUINTILES[(int) value];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(12f);
        mChart.getLegend().setEnabled(false);

        mCollegeId = getActivity().getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        mIsPublic = getActivity().getIntent().getBooleanExtra(Utility.PUBLIC_COLLEGE_KEY, false);
        getLoaderManager().initLoader(COST_LOADER, null, this);
        getLoaderManager().initLoader(COLLEGE_MAIN_LOADER, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case COLLEGE_MAIN_LOADER:
                return new CursorLoader(getContext(), CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                        TUITION_PROJECTION, null, null, null);
            case COST_LOADER:
                return new CursorLoader(getContext(), CollegeContract.CostEntry.buildCostWithCollegeId(mCollegeId),
                        COST_COLUMNS, null, null, null);
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

            case COLLEGE_MAIN_LOADER:
                if (mIsPublic){
                    mInStateLL.setVisibility(View.VISIBLE);
                    mOutStateLabelTV.setVisibility(View.VISIBLE);
                    mInStateTV.setText(Utility.formatThousandsCircle(
                            (data.getInt(COL_IN_STATE))));
                } else {
                    mInStateLL.setVisibility(View.GONE);
                    mOutStateLabelTV.setVisibility(View.GONE);
                }
                mOutStateTV.setText(Utility.formatThousandsCircle(
                        (data.getInt(COL_OUT_STATE))));
                break;

            case COST_LOADER:
                Log.i(LOG_TAG, "LOADER ID IDENTIFIED");

                mCost0to30 = (float) data.getInt(COL_COST_0to30);
                mCost30to48 = (float) data.getInt(COL_COST_30to48);
                mCost48to75 = (float) data.getInt(COL_COST_48to75);
                mCost75to110 = (float) data.getInt(COL_COST_75to110);
                mCost110plus = (float) data.getInt(COL_COST_110plus);
                final String priceCalc = data.getString(COL_PRICE_CALC);
                Log.i(LOG_TAG, "PRICE CALC: " + priceCalc);
                mCalculatorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((CostClickListener) getActivity()).onCalculatorButtonClicked(priceCalc);
                    }
                });
                double grantPct = data.getDouble(COL_GRANT_PCT);
                double loanPct = data.getDouble(COL_LOAN_PCT);

                createPieChart(mGrantPiechart, grantPct);
                createPieChart(mLoanPiechart, loanPct);

                setData();
                mChart.setFitBars(true);
                mChart.invalidate();
                break;

            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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
            set = new BarDataSet(entries, getResources().getString(R.string.average_net_cost));
            set.setColor(Color.RED);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            BarData data = new BarData(dataSets);
            data.setBarWidth(.9f);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTextSize(12f);
            mChart.setData(data);
        }
    }


    private void createPieChart(PieChart pieChart, double pct){
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

    private SpannableString createCenterText(double pct){
        NumberFormat nf = NumberFormat.getPercentInstance();
        String str = nf.format(pct);
        SpannableString s = new SpannableString(str);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary)), 0, s.length(), 0);
        return s;
    }

    public interface CostClickListener {
        void onStudentAidSiteButtonClicked();
        void onCalculatorButtonClicked(String uri);
    }

}
