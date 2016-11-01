package com.scholar.dollar.android.dollarscholarbenlewis.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CollegeMainFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.DetailFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeDetailService;
import com.scholar.dollar.android.dollarscholarbenlewis.service.PlacesService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private final static int MAIN_INFO_LOADER = 100;
    private final static int DETAIL_CURSOR_ID = 200;
    public static final int PLACE_LOADER = 300;
    private int mCollegeId;

    public static final String COLLEGE_ID_KEY = "collegeId";
    public static final String NAME_KEY = "name";
    public static final String LAT_KEY = "lat";
    public static final String LON_KEY = "lon";
    private String mPhotoId;
    private int mIsFavorite;
    private Adapter mAdapter;
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
    @BindView(R.id.detail_ab_grad_4yr)
    TextView m4yearTV;
    @BindView(R.id.detail_ab_grad_6yr)
    TextView m6yearTV;
    @BindView(R.id.detail_attribution)
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

    public final static String[] PLACE_COLUMNS = {
            CollegeContract.PlaceEntry.COLLEGE_ID,
            CollegeContract.PlaceEntry.NAME,
            CollegeContract.PlaceEntry.LATITUDE,
            CollegeContract.PlaceEntry.LONGITUDE,
            CollegeContract.PlaceEntry.PLACE_ID
    };
    public static final int COL_COLLEGE_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_LAT = 2;
    public static final int COL_LON = 3;
    public static final int COL_PLACE_ID = 4;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mCollegeId = getIntent().getIntExtra("collegeIdKey", -1);
        if (mCollegeId != -1) {
            startService(new Intent(this, CollegeDetailService.class)
                    .putExtra("collegeIdKey", mCollegeId));
        }
        mDetailTabs.setupWithViewPager(mViewPager);
        setupDetailViewPager(mViewPager);
        mDetailTabs.setupWithViewPager(mViewPager);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        mCollapsingToolbar.setExpandedTitleGravity(Gravity.TOP);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        getSupportLoaderManager().initLoader(MAIN_INFO_LOADER, null, this);
        getSupportLoaderManager().initLoader(PLACE_LOADER, null, this);
    }

    private void setupDetailViewPager(ViewPager viewPager) {
        mAdapter = new Adapter(getSupportFragmentManager());
        mAdapter.addFragment(new DetailFragment(), getString(R.string.colleges));
        mAdapter.addFragment(new DetailFragment(), getString(R.string.public_));
        mAdapter.addFragment(new DetailFragment(), getString(R.string.favorites));
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mCollegeId != -1) {
            switch (id) {
                case MAIN_INFO_LOADER:
                    return new CursorLoader(this, CollegeContract.CollegeMainEntry.buildMainWithCollegeId(mCollegeId),
                            CollegeMainFragment.COLLEGE_COLUMNS, null, null, null);
                case PLACE_LOADER:
                    return new CursorLoader(this, CollegeContract.PlaceEntry.buildPlaceWithCollegeId(mCollegeId),
                            PLACE_COLUMNS, null, null, null);
            }
        }

        Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND: " + id);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case MAIN_INFO_LOADER:
                if (data != null && data.moveToFirst()) {
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
                break;
            case PLACE_LOADER:
                if (data != null && data.moveToFirst()) {
                    String placeId = data.getString(COL_PLACE_ID);
                    if (placeId == null) {
                        startService(new Intent(getApplicationContext(), PlacesService.class)
                                .putExtra(COLLEGE_ID_KEY, mCollegeId)
                                .putExtra(NAME_KEY, data.getString(COL_NAME))
                                .putExtra(LAT_KEY, data.getDouble(COL_LAT))
                                .putExtra(LON_KEY, data.getDouble(COL_LON)));
                    }
                    else{
                        placePhotosAsyc(placeId);
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

    abstract class PhotoTask extends AsyncTask<String, Void, PhotoTask.AttributedPhoto> {

        private int mHeight;
        private int mWidth;

        public PhotoTask(int width, int height) {
            mHeight = height;
            mWidth = width;
        }

        /**
         * Loads the first photo for a place id from the Geo Data API.
         * The place id must be the first (and only) parameter.
         */
        @Override
        protected AttributedPhoto doInBackground(String... params) {
            if (params.length != 1) {
                return null;
            }
            final String placeId = params[0];
            AttributedPhoto attributedPhoto = null;

            PlacePhotoMetadataResult result = Places.GeoDataApi
                    .getPlacePhotos(mGoogleApiClient, placeId).await();

            if (result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                    // Get the first bitmap and its attributions.
                    PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
                    CharSequence attribution = photo.getAttributions();
                    // Load a scaled bitmap for this photo.
                    Bitmap image = photo.getScaledPhoto(mGoogleApiClient, mWidth, mHeight).await()
                            .getBitmap();

                    attributedPhoto = new AttributedPhoto(attribution, image);
                }
                // Release the PlacePhotoMetadataBuffer.
                photoMetadataBuffer.release();
            }
            return attributedPhoto;
        }

        /**
         * Holder for an image and its attribution.
         */
        class AttributedPhoto {

            public final CharSequence attribution;

            public final Bitmap bitmap;

            public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
                this.attribution = attribution;
                this.bitmap = bitmap;
            }
        }
    }

    private void placePhotosAsyc(String placeId) {

        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask(mBackgroundPhotoIV.getWidth(), mBackgroundPhotoIV.getHeight()) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                mBackgroundPhotoIV.setImageResource(R.drawable.ic_school_black_24dp);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                Log.i(LOG_TAG, "ON_POST_EXECUTE CALLED");
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    mBackgroundPhotoIV.setImageBitmap(attributedPhoto.bitmap);

//                     Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                        mAttrTV.setVisibility(View.GONE);
                    } else {
                        mAttrTV.setVisibility(View.VISIBLE);
                        mAttrTV.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
                    }

                }
            }
        }.execute(placeId);
    }
}