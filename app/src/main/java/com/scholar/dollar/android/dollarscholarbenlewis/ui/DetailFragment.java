package com.scholar.dollar.android.dollarscholarbenlewis.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeDetailService;
import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeFavoriteService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private final static int MAIN_INFO_CURSOR_ID = 100;
    private final static int DETAIL_CURSOR_ID = 200;
    private int mCollegeId;
    private Context mContext;
    private int mIsFavorite;

    @BindView(R.id.detail_college_name_tv) TextView mNameTV;
    @BindView(R.id.detail_city_state_tv) TextView mCityStateTV;
    @BindView(R.id.detail_ownership_tv) TextView mOwnershipTV;
    @BindView(R.id.detail_size_tv) TextView mSizeTV;
    @BindView(R.id.detail_earnings_tv) TextView mEarningsTV;
    @BindView(R.id.detail_tuition_tv) TextView mTuitionTV;
    @BindView(R.id.detail_debt_tv) TextView mDebtTV;
    @BindView(R.id.detail_monthly_tv) TextView mMonthlyTV;
    @BindView(R.id.detail_4_yr_grad_tv) TextView m4yearTV;
    @BindView(R.id.detail_6_yr_grad_tv) TextView m6yearTV;
    @BindView(R.id.act_tv) TextView mActTV;
    @BindView(R.id.sat_tv) TextView mSatTV;
    @BindView(R.id.detail_logo_iv) ImageView mLogoIV;
    @BindView(R.id.detail_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.detail_star_iv) ImageView mStarIV;

    public DetailFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollegeId = getActivity().getIntent().getIntExtra("collegeIdKey", -1);
        Log.i(LOG_TAG, "COLLEGE ID: " + mCollegeId);
        mContext = getContext();
        if (mCollegeId != -1){
            mContext.startService(new Intent(getContext(), CollegeDetailService.class)
                    .putExtra("collegeIdKey", mCollegeId));
            getLoaderManager().initLoader(MAIN_INFO_CURSOR_ID, null, this);
            getLoaderManager().initLoader(DETAIL_CURSOR_ID, null, this);
        }
    }

    public static final String[] COLLEGE_DETAIL_COLUMNS = {
            CollegeContract.CollegeDetailEntry.UNDERGRAD_SIZE,
            CollegeContract.CollegeDetailEntry.MED_DEBT_COMPLETERS,
            CollegeContract.CollegeDetailEntry.MED_MONTH_PAYMENT,
            CollegeContract.CollegeDetailEntry.GRAD_RATE_4_YEARS,
            CollegeContract.CollegeDetailEntry.ACT_MED,
            CollegeContract.CollegeDetailEntry.SAT_MED
    };

    public static final int SIZE = 0;
    public static final int DEBT = 1;
    public static final int MONTHLY_PAYMENT = 2;
    public static final int GRAD_RATE_4_YEAR = 3;
    public static final int ACT = 4;
    public static final int SAT = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startService(new Intent(getContext(), CollegeFavoriteService.class)
                .putExtra("collegeId", mCollegeId)
                .putExtra("isFavorite", mIsFavorite));
            }
        });

        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case MAIN_INFO_CURSOR_ID:
                return new CursorLoader(getContext(), CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                        CollegeMainFragment.COLLEGE_COLUMNS, null, null, null);
            case DETAIL_CURSOR_ID:
                return new CursorLoader(getContext(), CollegeContract.CollegeDetailEntry.buildDetailWithCollegeId(mCollegeId),
                        COLLEGE_DETAIL_COLUMNS, null, null, null);
            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || !data.moveToFirst()){
            Log.i(LOG_TAG, "CURSOR IS NULL");
            return;
        }
        switch (loader.getId()){
            case MAIN_INFO_CURSOR_ID:


                String name = data.getString(CollegeMainFragment.NAME);
                mCollapsingToolbar.setTitle(name);
                mNameTV.setText(name);
                mNameTV.setContentDescription(name);

                String cityState = (mContext.getString(R.string.city_state,
                        data.getString(CollegeMainFragment.CITY), data.getString(CollegeMainFragment.STATE)));
                mCityStateTV.setText(cityState);
                mCityStateTV.setContentDescription(cityState);

                String ownership = data.getInt(CollegeMainFragment.OWNERSHIP) == 1 ? "Public" : "Private";
                mOwnershipTV.setText(ownership);
                mOwnershipTV.setContentDescription(ownership);

                String earnings = mContext.getString(R.string.earnings_cardview,
                        data.getInt(CollegeMainFragment.EARNINGS));
                mEarningsTV.setText(earnings);
                mEarningsTV.setContentDescription(earnings);

                String tuitionOutState =  mContext.getString(R.string.tuition_cardview,
                        data.getInt(CollegeMainFragment.TUITION_OUT_STATE));
                mTuitionTV.setText(tuitionOutState);
                mTuitionTV.setContentDescription(tuitionOutState);

                String sixYearGradRate = (mContext.getString(R.string.six_year_grad_rate,
                        data.getDouble(CollegeMainFragment.GRAD_RATE_6_YEARS)));
                m6yearTV.setText(sixYearGradRate);
                m6yearTV.setContentDescription(sixYearGradRate);

                Picasso.with(mContext).load(data.getString(CollegeMainFragment.LOGO))
                        .placeholder(R.drawable.ic_school_black_24dp).into(mLogoIV);
                mLogoIV.setContentDescription(mContext.getString(R.string.logo));

                mIsFavorite = data.getInt(CollegeMainFragment.FAVORITE);
                int star = mIsFavorite == 0 ? R.drawable.ic_star_unfilled_24dp : R.drawable.ic_star_filled_24dp;
                Picasso.with(mContext).load(star).placeholder(star).into(mStarIV);
                mStarIV.setContentDescription(mContext.getString(R.string.favorites_button));

                break;
            case DETAIL_CURSOR_ID:
                mSizeTV.setText(mContext.getString(R.string.undergrads, data.getInt(SIZE)));
                mDebtTV.setText(mContext.getString(R.string.debt, data.getInt(DEBT)));
                mMonthlyTV.setText(mContext.getString(R.string.mp, data.getInt(MONTHLY_PAYMENT)));
                m4yearTV.setText(mContext.getString(R.string.four_year_grad_rate, data.getDouble(GRAD_RATE_4_YEAR)));
                mActTV.setText(mContext.getString(R.string.act, data.getInt(ACT)));
                mSatTV.setText(mContext.getString(R.string.sat, data.getInt(SAT)));
                break;
            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}