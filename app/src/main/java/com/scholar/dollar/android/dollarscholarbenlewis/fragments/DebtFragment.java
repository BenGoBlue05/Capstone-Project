package com.scholar.dollar.android.dollarscholarbenlewis.fragments;

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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
    private float m90pct;
    private float m10pct;
    private float mMonthly;
    private float m25pct;
    private float m75pct;
    private static final int DEBT_LOADER = 100;
    private static final String[] PERCENTILES = {"10", "25", "50", "75", "90"};

    public static final String[] DEBT_COLUMNS = {
            CollegeContract.DebtEntry.LOAN_PRINCIPAL_MED,
            CollegeContract.DebtEntry.DEBT_10PCT,
            CollegeContract.DebtEntry.DEBT_25PCT,
            CollegeContract.DebtEntry.DEBT_75PCT,
            CollegeContract.DebtEntry.DEBT_90PCT,
            CollegeContract.DebtEntry.MONTH_PAYMENT_10YR_MED
    };

    private static final int COL_PRINCIPAL = 0;
    private static final int COL_10_PCT = 1;
    private static final int COL_25_PCT = 2;
    private static final int COL_75_PCT = 3;
    private static final int COL_90_PCT = 4;
    private static final int COL_MONTHLY = 5;

    @BindView(R.id.debt_barchart)
    BarChart mChart;
    @BindView(R.id.debt_principal_tv)
    TextView mPrincipalTV;
    @BindView(R.id.debt_monthly)
    TextView mMonthlyTV;

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

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return PERCENTILES[(int) value];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        mCollegeId = getActivity().getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        getLoaderManager().initLoader(DEBT_LOADER, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), CollegeContract.DebtEntry.buildDebtWithCollegeId(mCollegeId),
                DEBT_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || !data.moveToFirst()){
            Log.i(LOG_TAG, "CURSOR IS NULL");
            return;
        }

        mLoanPrincipal = (float) data.getDouble(COL_PRINCIPAL);
        m10pct = (float) data.getDouble(COL_10_PCT);
        m25pct = (float) data.getDouble(COL_25_PCT);
        m75pct = (float) data.getDouble(COL_75_PCT);
        m90pct = (float) data.getDouble(COL_90_PCT);
        mMonthly = (float) data.getDouble(COL_MONTHLY);


        mPrincipalTV.setText(Utility.formatThousandsCircle(mLoanPrincipal));
        String monthly = Integer.toString(Math.round(mMonthly));
        mMonthlyTV.setText(monthly);

        setData();
        mChart.setFitBars(true);
        mChart.invalidate();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void setData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, m10pct));
        entries.add(new BarEntry(1f, m25pct));
        entries.add(new BarEntry(2f, mLoanPrincipal));
        entries.add(new BarEntry(3f, m75pct));
        entries.add(new BarEntry(4f, m90pct));
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

}
