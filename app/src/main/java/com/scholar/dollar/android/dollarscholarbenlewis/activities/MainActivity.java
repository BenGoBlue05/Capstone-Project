package com.scholar.dollar.android.dollarscholarbenlewis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.facebook.stetho.Stetho;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.adapter.PageAdapter;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CollegeMainFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeService;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    public static final String ANONYMOUS = "anonymous";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    public GoogleApiClient mGoogleApiClient;
    private String mState;
    private PageAdapter mPageAdapter;
    private Bundle mFavoriteArgs;
    private Bundle mPublicArgs;
    private Spinner mSpinner;
    private AdapterView.OnItemSelectedListener mStateListener;
    private TabLayout.ViewPagerOnTabSelectedListener mTabListener;

    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            Intent publicCollegesIntent = new Intent(this, CollegeService.class)
                    .putExtra(Utility.PUBLIC_COLLEGE_KEY, true);
            startService(new Intent(this, CollegeService.class));
            startService(publicCollegesIntent);
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mPhotoUrl = mFirebaseUser.getPhotoUrl() != null ? mFirebaseUser.getPhotoUrl().toString() : null;
        }

        if (savedInstanceState == null) {
            Intent publicCollegesIntent = new Intent(this, CollegeService.class)
                    .putExtra(Utility.PUBLIC_COLLEGE_KEY, true);
            startService(new Intent(this, CollegeService.class));
            startService(publicCollegesIntent);
        }
        mPublicArgs = new Bundle();
        mPublicArgs.putBoolean(Utility.PUBLIC_COLLEGE_KEY, true);
        mFavoriteArgs = new Bundle();
        mFavoriteArgs.putBoolean(Utility.FAVORITE_COLLEGE_KEY, true);

        setSupportActionBar(mToolbar);
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_filter_list_black_24dp, getTheme());
            if (indicator != null) {
                indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            }
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        mSpinner = (Spinner) mNavView.getMenu().findItem(R.id.navigation_drawer_item3).getActionView();
        ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.states_array, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        mTabListener = new TabLayout.ViewPagerOnTabSelectedListener(mViewPager);
        mStateListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getSelectedItem().toString().equals(mState)) {
                    mState = parent.getSelectedItem().toString();
                    Log.i(LOG_TAG, "STATE: " + mState);
                    if (!mState.equals("All")) {
                        startService(new Intent(getApplicationContext(), CollegeService.class)
                                .putExtra(Utility.STATE_KEY, mState));
                    }
                    CollegeMainFragment collegeMainFragment = (CollegeMainFragment) mPageAdapter.getItem(0);
                    CollegeMainFragment publicFragment = (CollegeMainFragment) mPageAdapter.getItem(1);

                    collegeMainFragment.setState(mState);
                    publicFragment.setState(mState);
                }
                mToolbar.setTitle(mState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(LOG_TAG, "NOTHING SELECTED FOR SPINNER");
            }
        };


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    private void setupViewPager(ViewPager viewPager) {
        mPageAdapter = new PageAdapter(getSupportFragmentManager());
        Bundle stateArg = new Bundle();
        Bundle publicArgs = new Bundle(mPublicArgs);
        Bundle favoriteArgs = new Bundle(mFavoriteArgs);
        stateArg.putString(Utility.STATE_KEY, mState);
        publicArgs.putString(Utility.STATE_KEY, mState);
        favoriteArgs.putString(Utility.STATE_KEY, mState);

        CollegeMainFragment mainFragment = new CollegeMainFragment();
        CollegeMainFragment publicFragment = new CollegeMainFragment();
        CollegeMainFragment favoriteFragment = new CollegeMainFragment();

        mainFragment.setArguments(stateArg);
        publicFragment.setArguments(publicArgs);
        favoriteFragment.setArguments(favoriteArgs);

        mPageAdapter.addFragment(mainFragment, getString(R.string.all));
        mPageAdapter.addFragment(publicFragment, getString(R.string.public_));
        mPageAdapter.addFragment(favoriteFragment, getString(R.string.favorites));
        viewPager.setAdapter(mPageAdapter);
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sign_out:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                mUsername = ANONYMOUS;
                mPhotoUrl = null;
                startActivity(new Intent(this, SignInActivity.class));
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
//            case R.id.fb:
//                startService(new Intent(this, CollegeBasicService.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpinner.setOnItemSelectedListener(mStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}