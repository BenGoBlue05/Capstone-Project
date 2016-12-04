package com.scholar.dollar.android.dollarscholarbenlewis.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.adapter.PageAdapter;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CostFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.DebtFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.EarningsFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.DetailService;
import com.scholar.dollar.android.dollarscholarbenlewis.service.PlacesService;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.OnConnectionFailedListener,
        CostFragment.CostClickListener {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private int mCollegeId;
    public boolean mIsPublic;
    public int mCostInState;
    public int mCostOutState;

    private static final int COLLEGE_MAIN_LOADER = 100;
    private static final int PLACE_LOADER = 200;
    public static final String COLLEGE_ID_KEY = "collegeId";
    public static final String NAME_KEY = "name";
    public static final String LAT_KEY = "lat";
    public static final String LON_KEY = "lon";
    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback;

    @BindView(R.id.detail_photo)
    ImageView mBackgroundPhotoIV;
    @BindView(R.id.detail_name)
    TextView mNameTV;
    @BindView(R.id.detail_city_state)
    TextView mCityStateTV;
    @BindView(R.id.detail_ownership)
    TextView mOwnershipTV;
    @BindView(R.id.detail_logo)
    ImageView mLogoIV;
    @BindView(R.id.detail_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.detail_tabs)
    TabLayout mDetailTabs;
    @BindView(R.id.detail_size_tv)
    TextView mSizeTV;
    @BindView(R.id.detail_admission_pieChart)
    PieChart mAdmissionPieChart;
    @BindView(R.id.detail_grad_piechart)
    PieChart mGradRatePieChart;
//    @BindView(R.id.detail_grad4yrs_tv)
//    TextView m4yearGradRateTV;


    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mCollegeId = getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        setupDetailViewPager(mViewPager);
        mDetailTabs.setupWithViewPager(mViewPager);
        setUpCollapsingToolbar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDisplayPhotoResultCallback = new ResultCallback<PlacePhotoResult>() {
            @Override
            public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                if (!placePhotoResult.getStatus().isSuccess()) {
                    return;
                }
                mBackgroundPhotoIV.setImageBitmap(placePhotoResult.getBitmap());

            }
        };
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        getSupportLoaderManager().initLoader(COLLEGE_MAIN_LOADER, null, this);
        getSupportLoaderManager().initLoader(PLACE_LOADER, null, this);
    }


    private void setupDetailViewPager(ViewPager viewPager) {
        PageAdapter mAdapter = new PageAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new EarningsFragment(), getString(R.string.earnings));
        mAdapter.addFragment(new CostFragment(), getString(R.string.cost));
        mAdapter.addFragment(new DebtFragment(), getString(R.string.debt));
//        mAdapter.addFragment(new AdmissionFragment(), getResources().getString(R.string.admission));
//        mAdapter.addFragment(new PhotoFragment(), getString(R.string.photos));
        viewPager.setAdapter(mAdapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mCollegeId != -1) {
            switch (id) {
                case COLLEGE_MAIN_LOADER:
                    return new CursorLoader(this, CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                            Utility.COLLEGE_COLUMNS, null, null, null);
                case PLACE_LOADER:
                    return new CursorLoader(this, CollegeContract.PlaceEntry.buildPlaceWithCollegeId(mCollegeId),
                            Utility.PLACE_COLUMNS, null, null, null);
            }
        }

        Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND: " + id);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case COLLEGE_MAIN_LOADER:
                if (data == null || !data.moveToFirst()) {
                    if (mCollegeId != -1) {
                        startService(new Intent(this, DetailService.class)
                                .putExtra(Utility.COLLEGE_ID_KEY, mCollegeId));
                    }

                } else {
                    String name = data.getString(Utility.NAME);
                    mCollapsingToolbar.setTitle(name);
                    mNameTV.setText(name);
                    mNameTV.setContentDescription(name);

                    String cityState = (getString(R.string.city_state,
                            data.getString(Utility.CITY), data.getString(Utility.STATE)));
                    mCityStateTV.setText(cityState);
                    mCityStateTV.setContentDescription(cityState);
                    mCostInState = data.getInt(Utility.TUITION_IN_STATE);
                    mCostOutState = data.getInt(Utility.TUITION_OUT_STATE);
                    mIsPublic = data.getInt(Utility.OWNERSHIP) == 1;
                    String ownership = mIsPublic ? "Public" : "Private";
                    mOwnershipTV.setText(ownership);
                    mOwnershipTV.setContentDescription(ownership);
                    mSizeTV.setText(Utility.formatThousandsCircle((float) data.getInt(Utility.SIZE)));
                    Picasso.with(this).load(data.getString(Utility.LOGO))
                            .placeholder(R.drawable.ic_school_black_24dp).into(mLogoIV);
                    createPieChart(mAdmissionPieChart, data.getDouble(Utility.ADMISSION_RATE));
//                    m4yearGradRateTV.setText(String.format(Locale.getDefault(), "%f", data.getDouble(Utility.GRAD_RATE_4_YEARS)));
                    createPieChart(mGradRatePieChart, data.getDouble(Utility.GRAD_RATE_6_YEARS));
                    mLogoIV.setContentDescription(getString(R.string.logo));
                }
                break;

            case PLACE_LOADER:
                if (data != null && data.moveToFirst()) {
                    String placeId = data.getString(Utility.COL_PLACE_ID);
                    if (placeId == null) {
                        startService(new Intent(getApplicationContext(), PlacesService.class)
                                .putExtra(COLLEGE_ID_KEY, mCollegeId)
                                .putExtra(NAME_KEY, data.getString(Utility.COL_CSC_NAME))
                                .putExtra(LAT_KEY, data.getDouble(Utility.COL_LAT))
                                .putExtra(LON_KEY, data.getDouble(Utility.COL_LON)));
                    } else {
                        placePhotosAsync(placeId);
                    }
                }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "GOOGLE_API CONNECTION FAILED");
    }

    private void placePhotosAsync(String placeId) {
        Log.i(LOG_TAG, "PLACE PHOTOS ASYNC CALLED");
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                    @Override
                    public void onResult(@NonNull PlacePhotoMetadataResult photos) {
                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            photoMetadataBuffer.get(0).getScaledPhoto(mGoogleApiClient,
                                    mBackgroundPhotoIV.getWidth(), mBackgroundPhotoIV.getHeight())
                                    .setResultCallback(mDisplayPhotoResultCallback);

                        }
                        photoMetadataBuffer.release();
                    }
                });
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

        int[] colors = {ContextCompat.getColor(this, R.color.colorPrimary), Color.LTGRAY};

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(colors);

        PieData pieData = new PieData(set);
        pieData.setValueTextColor(Color.TRANSPARENT);

        pieChart.setData(pieData);
        pieChart.setRotationEnabled(false);
        pieChart.invalidate();
    }

    private SpannableString createCenterText(double pct) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        String str = nf.format(pct);
        SpannableString s = new SpannableString(str);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), 0, s.length(), 0);
        return s;
    }

    public void setUpCollapsingToolbar() {
        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        mCollapsingToolbar.setExpandedTitleGravity(Gravity.TOP);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
    }


    @Override
    public void onStudentAidSiteButtonClicked() {
        Log.i(LOG_TAG, "STUDENT AID BUTTON CLICKED");
        Uri webpage = Uri.parse("https://studentaid.ed.gov/sa/types");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onCalculatorButtonClicked(String uri) {
        Log.i(LOG_TAG, "CALCULATOR BUTTON CLICKED");
        if (!uri.contains("http")){
            uri = "https://" + uri;
        }
        Log.i(LOG_TAG, "URI: " + uri);
        Uri webpage = Uri.parse(uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }
}