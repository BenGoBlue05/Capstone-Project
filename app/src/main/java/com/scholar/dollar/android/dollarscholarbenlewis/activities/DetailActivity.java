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
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.AdmissionFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CostFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.DebtFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.EarningsFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.DetailService;
import com.scholar.dollar.android.dollarscholarbenlewis.service.PlacesService;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility.COLLEGE_ID_KEY;
import static com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility.LAT_KEY;
import static com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility.LON_KEY;
import static com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility.PUBLIC_COLLEGE_KEY;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.OnConnectionFailedListener,
        CostFragment.StudentAidSiteClickListener {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private int mCollegeId;
    public boolean mIsPublic;
    public int mCostInState;
    public int mCostOutState;

    public static final int MAIN_INFO_LOADER = 100;
    public static final int PLACE_LOADER = 101;
    public static final int EARNINGS_LOADER = 200;
    public static final int COST_LOADER = 300;
    public static final int DEBT_LOADER = 400;
    public static final int ADMISSIONS_LOADER = 500;

    EarningsFragment mEarningsFragment;
    CostFragment mCostFragment;
    DebtFragment mDebtFragment;
    AdmissionFragment mAdmissionFragment;

    private String mPhotoId;
    private int mIsFavorite;
    private PageAdapter mAdapter;
    @BindView(R.id.detail_ab_background_photo)
    ImageView mBackgroundPhotoIV;
    @BindView(R.id.detail_ab_name)
    TextView mNameTV;
    @BindView(R.id.detail_ab_city_state)
    TextView mCityStateTV;
    @BindView(R.id.detail_ab_ownership)
    TextView mOwnershipTV;
    @BindView(R.id.detail_ab_size)
    TextView mSizeTV;

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

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mCollegeId = getIntent().getIntExtra(COLLEGE_ID_KEY, -1);
        mIsPublic = getIntent().getBooleanExtra(PUBLIC_COLLEGE_KEY, false);

        ActionBar supportActionBar = getSupportActionBar();
        LoaderManager manager = getSupportLoaderManager();
        manager.initLoader(MAIN_INFO_LOADER, null, this);

        setupDetailViewPager(mViewPager);
        mDetailTabs.setupWithViewPager(mViewPager);
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        setUpCollapsingToolbar();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        manager.initLoader(PLACE_LOADER, null, this);

    }

    private void setupDetailViewPager(ViewPager viewPager) {
        if (mAdapter == null || mAdapter.getCount() == 0){
            Log.i(LOG_TAG, "ADAPTER IS NULL");
            mAdapter = new PageAdapter(getSupportFragmentManager());
            LoaderManager manager = getSupportLoaderManager();
            manager.initLoader(EARNINGS_LOADER, null, this);
            manager.initLoader(COST_LOADER, null, this);
            manager.initLoader(DEBT_LOADER, null, this);
            manager.initLoader(ADMISSIONS_LOADER, null, this);
        }
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mCollegeId != -1) {
            switch (id) {
                case MAIN_INFO_LOADER:
                    return new CursorLoader(this, CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                            Utility.COLLEGE_COLUMNS, null, null, null);
                case PLACE_LOADER:
                    return new CursorLoader(this, CollegeContract.PlaceEntry.buildPlaceWithCollegeId(mCollegeId),
                            Utility.PLACE_COLUMNS, null, null, null);
                case EARNINGS_LOADER:
                    return new CursorLoader(this, CollegeContract.EarningsEntry.buildEarningsWithCollegeId(mCollegeId),
                            Utility.EARNINGS_COLUMNS, null, null, null);
                case COST_LOADER:
                    return new CursorLoader(this, CollegeContract.CostEntry.buildCostWithCollegeId(mCollegeId),
                            Utility.COST_COLUMNS, null, null, null);
                case DEBT_LOADER:
                    return new CursorLoader(this, CollegeContract.DebtEntry.buildDebtWithCollegeId(mCollegeId),
                            Utility.DEBT_COLUMNS, null, null, null);
                case ADMISSIONS_LOADER:
                    return new CursorLoader(this, CollegeContract.AdmissionEntry.buildAdmissionWithCollegeId(mCollegeId),
                            Utility.ADMISSION_COLUMNS, null, null, null);
                default:
                    Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND: " + id);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case MAIN_INFO_LOADER:
                if (data == null || !data.moveToFirst()) {
                    if (mCollegeId != -1) {
                        startService(new Intent(this, DetailService.class)
                                .putExtra(COLLEGE_ID_KEY, mCollegeId));
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
                    String ownership = mIsPublic ? "Public" : "Private";
                    mOwnershipTV.setText(ownership);
                    mOwnershipTV.setContentDescription(ownership);

                    Picasso.with(this).load(data.getString(Utility.LOGO))
                            .placeholder(R.drawable.ic_school_black_24dp).into(mLogoIV);
                    mLogoIV.setContentDescription(getString(R.string.logo));
                }
                break;
            case PLACE_LOADER:
                if (data != null && data.moveToFirst()) {
                    String placeId = data.getString(Utility.COL_PLACE_ID);
                    if (placeId == null) {
                        startService(new Intent(getApplicationContext(), PlacesService.class)
                                .putExtra(Utility.COLLEGE_ID_KEY, mCollegeId)
                                .putExtra(Utility.NAME_KEY, data.getString(Utility.COL_CSC_NAME))
                                .putExtra(LAT_KEY, data.getDouble(Utility.COL_LAT))
                                .putExtra(LON_KEY, data.getDouble(Utility.COL_LON)));
                    } else {
                        placePhotosAsync(placeId);
                    }
                }
                break;
            case EARNINGS_LOADER:
                if (data == null || !data.moveToFirst()) {
                    startService(new Intent(this, DetailService.class)
                            .putExtra(COLLEGE_ID_KEY, mCollegeId)
                            .putExtra(PUBLIC_COLLEGE_KEY, mIsPublic));
                } else {
                    mEarningsFragment = new EarningsFragment();
                    mEarningsFragment.setM6yrs25pct((float) data.getInt(Utility.COL_EARN_6YRS_25PCT));
                    mEarningsFragment.setM6yrs50pct((float) data.getInt(Utility.COL_EARN_6YRS_50PCT));
                    mEarningsFragment.setM6yrs75pct((float) data.getInt(Utility.COL_EARN_6YRS_75PCT));
                    mEarningsFragment.setM8yrs25pct((float) data.getInt(Utility.COL_EARN_8YRS_25PCT));
                    mEarningsFragment.setM8yrs50pct((float) data.getInt(Utility.COL_EARN_8YRS_50PCT));
                    mEarningsFragment.setM8yrs75pct((float) data.getInt(Utility.COL_EARN_8YRS_75PCT));
                    mEarningsFragment.setM10yrs25pct((float) data.getInt(Utility.COL_EARN_10YRS_25PCT));
                    mEarningsFragment.setM10yrs50pct((float) data.getInt(Utility.COL_EARN_10YRS_50PCT));
                    mEarningsFragment.setM10yrs75pct((float) data.getInt(Utility.COL_EARN_10YRS_75PCT));
                    mAdapter.addFragment(mEarningsFragment, getString(R.string.earnings));
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case COST_LOADER:
                if (data != null && data.moveToFirst()) {
                    mCostFragment = new CostFragment();
                    mCostFragment.setmCost0to30(data.getInt(Utility.COL_COST_0to30));
                    mCostFragment.setmCost30to48(data.getInt(Utility.COL_COST_30to48));
                    mCostFragment.setmCost48to75(data.getInt(Utility.COL_COST_48to75));
                    mCostFragment.setmCost75to110(data.getInt(Utility.COL_COST_75to110));
                    mCostFragment.setmCost110plus(data.getInt(Utility.COL_COST_110plus));
                    mCostFragment.setmIsPublic(mIsPublic);
                    mCostFragment.setmLoanPct((float) data.getDouble(Utility.COL_COST_LOAN_PCT));
                    mCostFragment.setmGrantPct(((float) data.getDouble(Utility.COL_COST_GRANT_PCT)));
                    mCostFragment.setmTuitionInState(mCostInState);
                    mCostFragment.setmTuitionOutState(mCostOutState);
                    mAdapter.addFragment(mCostFragment, getString(R.string.cost));
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case DEBT_LOADER:
                if (data != null && data.moveToFirst()) {
                    mDebtFragment = new DebtFragment();
                    mDebtFragment.setmLoanPrincipal((float) data.getDouble(Utility.COL_DEBT_LOAN_PPL));
                    mDebtFragment.setmCompleters((float) data.getDouble(Utility.COL_DEBT_COMPLETERS));
                    mDebtFragment.setmNoncompleters((float) data.getDouble(Utility.COL_DEBT_NONCOMPLETERS));
                    mDebtFragment.setmMonthly((float) data.getDouble(Utility.COL_DEBT_MONTHLY));
                    mDebtFragment.setM0to30((float) data.getDouble(Utility.COL_DEBT_0to30));
                    mDebtFragment.setM30to75((float) data.getDouble(Utility.COL_DEBT_30to75));
                    mDebtFragment.setM75plus((float) data.getDouble(Utility.COL_DEBT_75plus));
                    mAdapter.addFragment(mDebtFragment, getString(R.string.debt));
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case ADMISSIONS_LOADER:
                if (data != null && data.moveToFirst()) {
                    mAdmissionFragment = new AdmissionFragment();
                    mAdmissionFragment.setmAct25((float) data.getDouble(Utility.COL_ADMSN_ACT25));
                    mAdmissionFragment.setmAct50((float) data.getDouble(Utility.COL_ADMSN_ACT50));
                    mAdmissionFragment.setmAct75((float) data.getDouble(Utility.COL_ADMSN_ACT75));
                    mAdmissionFragment.setmSat((float) data.getDouble(Utility.COL_ADMSN_SAT50));
                    mAdmissionFragment.setmActEng25((float) data.getDouble(Utility.COL_ADMSN_ACT_ENG25));
                    mAdmissionFragment.setmActEng50((float) data.getDouble(Utility.COL_ADMSN_ACT_ENG50));
                    mAdmissionFragment.setmActEng75((float) data.getDouble(Utility.COL_ADMSN_ACT_ENG75));
                    mAdmissionFragment.setmActMath25((float) data.getDouble(Utility.COL_ADMSN_ACT_MATH25));
                    mAdmissionFragment.setmActMath50((float) data.getDouble(Utility.COL_ADMSN_ACT_MATH50));
                    mAdmissionFragment.setmActMath75((float) data.getDouble(Utility.COL_ADMSN_ACT_MATH75));
                    mAdmissionFragment.setmSatMath25((float) data.getDouble(Utility.COL_ADMSN_SAT_MATH25));
                    mAdmissionFragment.setmSatMath50((float) data.getDouble(Utility.COL_ADMSN_SAT_MATH50));
                    mAdmissionFragment.setmSatMath75((float) data.getDouble(Utility.COL_ADMSN_SAT_MATH75));
                    mAdmissionFragment.setmSatRead25((float) data.getDouble(Utility.COL_ADMSN_SAT_READ25));
                    mAdmissionFragment.setmSatRead50((float) data.getDouble(Utility.COL_ADMSN_SAT_READ50));
                    mAdmissionFragment.setmSatRead75((float) data.getDouble(Utility.COL_ADMSN_SAT_READ75));
                    mAdapter.addFragment(mAdmissionFragment, getResources().getString(R.string.admission));
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                Log.i(LOG_TAG, "LOADER NOT FOUND");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "GOOGLE_API CONNECTION FAILED");
    }

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                return;
            }
            mBackgroundPhotoIV.setImageBitmap(placePhotoResult.getBitmap());

        }
    };

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

    public void setUpCollapsingToolbar() {
        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        mCollapsingToolbar.setExpandedTitleGravity(Gravity.TOP);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
    }


    @Override
    public void onStudentAidSiteButtonClicked() {
        Uri webpage = Uri.parse("https://studentaid.ed.gov/sa/types");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}