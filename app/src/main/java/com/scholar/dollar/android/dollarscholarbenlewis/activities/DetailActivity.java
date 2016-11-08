package com.scholar.dollar.android.dollarscholarbenlewis.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CompletionFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CostFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.DebtFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.EarningsFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.DetailService;
import com.scholar.dollar.android.dollarscholarbenlewis.service.PlacesService;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility.PLACE_LOADER;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private int mCollegeId;
    private boolean mIsPublic;

    public static final String COLLEGE_ID_KEY = "collegeId";
    public static final String NAME_KEY = "name";
    public static final String LAT_KEY = "lat";
    public static final String LON_KEY = "lon";
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

    TextView mAttrTV;
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
        mCollegeId = getIntent().getIntExtra(Utility.COLLEGE_ID_KEY, -1);
        ActionBar supportActionBar = getSupportActionBar();
        mDetailTabs.setupWithViewPager(mViewPager);
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupDetailViewPager(mViewPager);
        mDetailTabs.setupWithViewPager(mViewPager);

        setUpCollapsingToolbar();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        getSupportLoaderManager().initLoader(Utility.COLLEGE_MAIN_LOADER, null, this);
        getSupportLoaderManager().initLoader(PLACE_LOADER, null, this);
    }

    private void setupDetailViewPager(ViewPager viewPager) {
        mAdapter = new PageAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new EarningsFragment(), getString(R.string.earnings));
        mAdapter.addFragment(new CostFragment(), getString(R.string.cost));
        mAdapter.addFragment(new DebtFragment(), getString(R.string.debt));
        mAdapter.addFragment(new CompletionFragment(), getResources().getString(R.string.graduation_rates));
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mCollegeId != -1) {
            switch (id) {
                case Utility.COLLEGE_MAIN_LOADER:
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
            case Utility.COLLEGE_MAIN_LOADER:
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

                    String ownership = data.getInt(Utility.OWNERSHIP) == 1 ? "Public" : "Private";
                    mOwnershipTV.setText(ownership);
                    mOwnershipTV.setContentDescription(ownership);

//

                    Picasso.with(this).load(data.getString(Utility.LOGO))
                            .placeholder(R.drawable.ic_school_black_24dp).into(mLogoIV);
                    mLogoIV.setContentDescription(getString(R.string.logo));

//        mIsFavorite = data.getInt(CollegeMainFragment.FAVORITE);
//        int star = mIsFavorite == 0 ? R.drawable.ic_star_unfilled_24dp : R.drawable.ic_star_filled_24dp;
//        Picasso.with(mContext).load(star).placeholder(star).into(mStarIV);
//        mStarIV.setContentDescription(mContext.getString(R.string.favorites_button));
//        break;
                }
                break;
            case Utility.PLACE_LOADER:
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
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(mGoogleApiClient, mBackgroundPhotoIV.getWidth(),
                                            mBackgroundPhotoIV.getHeight())
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }
                        photoMetadataBuffer.release();
                    }
                });
    }

    public void setUpCollapsingToolbar(){
        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        mCollapsingToolbar.setExpandedTitleGravity(Gravity.TOP);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
    }



}