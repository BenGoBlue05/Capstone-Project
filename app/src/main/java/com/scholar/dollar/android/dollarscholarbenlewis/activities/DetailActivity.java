package com.scholar.dollar.android.dollarscholarbenlewis.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CollegeMainFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.DetailFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeDetailService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private final static int MAIN_INFO_CURSOR_ID = 100;
    private final static int DETAIL_CURSOR_ID = 200;
    private int mCollegeId;

    private int mIsFavorite;
    private Adapter mAdapter;
    @BindView(R.id.detail_ab_name)
    TextView mNameTV;
    @BindView(R.id.detail_ab_city_state)
    TextView mCityStateTV;
    @BindView(R.id.detail_ab_ownership)
    TextView mOwnershipTV;
    @BindView(R.id.detail_ab_size)
    TextView mSizeTV;
    @BindView(R.id.detail_ab_grad_4yr)
    TextView m4yearTV;
    @BindView(R.id.detail_ab_grad_6yr)
    TextView m6yearTV;
    @BindView(R.id.detail_ab_logo)
    ImageView mLogoIV;
    @BindView(R.id.detail_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
//    @BindView(R.id.detail_star_iv)
//    ImageView mStarIV;
    @BindView(R.id.detail_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.detail_tabs)
    TabLayout mDetailTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mCollegeId = getIntent().getIntExtra("collegeIdKey", -1);
        if (mCollegeId != -1){
            startService(new Intent(this, CollegeDetailService.class)
                    .putExtra("collegeIdKey", mCollegeId));
        }
        mDetailTabs.setupWithViewPager(mViewPager);
        setupViewPager(mViewPager);
        mDetailTabs.setupWithViewPager(mViewPager);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportLoaderManager().initLoader(MAIN_INFO_CURSOR_ID, null, this);
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new Adapter(getSupportFragmentManager());
        mAdapter.addFragment(new DetailFragment(), getString(R.string.colleges));
        mAdapter.addFragment(new DetailFragment(), getString(R.string.public_));
        mAdapter.addFragment(new DetailFragment(), getString(R.string.favorites));
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == MAIN_INFO_CURSOR_ID) {
            if (mCollegeId != -1){
                return new CursorLoader(this, CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                        CollegeMainFragment.COLLEGE_COLUMNS, null, null, null);
            }
        }
        Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND: " + id);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || !data.moveToFirst()) {
            Log.i(LOG_TAG, "CURSOR IS NULL");
            return;
        }

        String name = data.getString(CollegeMainFragment.NAME);
        mCollapsingToolbar.setTitle(name);
        mNameTV.setText(name);
        mNameTV.setContentDescription(name);

        String cityState = (getString(R.string.city_state,
                data.getString(CollegeMainFragment.CITY), data.getString(CollegeMainFragment.STATE)));
        mCityStateTV.setText(cityState);
        mCityStateTV.setContentDescription(cityState);

        String ownership = data.getInt(CollegeMainFragment.OWNERSHIP) == 1 ? "Public" : "Private";
        mOwnershipTV.setText(ownership);
        mOwnershipTV.setContentDescription(ownership);

        String sixYearGradRate = (getString(R.string.six_year_grad_rate,
                data.getDouble(CollegeMainFragment.GRAD_RATE_6_YEARS)));
        m6yearTV.setText(sixYearGradRate);
        m6yearTV.setContentDescription(sixYearGradRate);

        Picasso.with(this).load(data.getString(CollegeMainFragment.LOGO))
                .placeholder(R.drawable.ic_school_black_24dp).into(mLogoIV);
        mLogoIV.setContentDescription(getString(R.string.logo));

//        mIsFavorite = data.getInt(CollegeMainFragment.FAVORITE);
//        int star = mIsFavorite == 0 ? R.drawable.ic_star_unfilled_24dp : R.drawable.ic_star_filled_24dp;
//        Picasso.with(mContext).load(star).placeholder(star).into(mStarIV);
//        mStarIV.setContentDescription(mContext.getString(R.string.favorites_button));
//        break;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class Adapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
