package com.scholar.dollar.android.dollarscholarbenlewis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CollegeMainFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scholar.dollar.android.dollarscholarbenlewis.fragments.CollegeMainFragment.PUBLIC_COLLEGES_BOOLEAN_KEY;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener
//        CollegeMainFragment.OnStateSelectedListener
{

//    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static final String ANONYMOUS = "anonymous";
    public static final String FAVORITE_COLLEGES_KEY = "favorites";
    public static final String PUBLIC_COLLEGES_KEY = "public";
    public static final String STATE_KEY = "state";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    private String mStateSelected;
    private GoogleApiClient mGoogleApiClient;

    private Adapter mAdapter;

    @BindView(R.id.nav_view) NavigationView mNavView;
    @BindView(R.id.drawer) DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout mTabs;

    public final String getState() {
        return mStateSelected;
    }

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
                    .putExtra(PUBLIC_COLLEGES_BOOLEAN_KEY, true);
            startService(new Intent(this, CollegeService.class));
            startService(publicCollegesIntent);
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        setSupportActionBar(mToolbar);
//        setupViewPager(viewPager, createCollegeFragment(false, false), createCollegeFragment(true, false));
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_filter, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mTitle = mDrawerTitle = getTitle();
        Log.i(LOG_TAG, "M_TITLE: " + mTitle);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (!mStateSelected.equals("All")) {
                    mTitle = mStateSelected;
                } else {
                    mTitle = getString(R.string.app_name);
                }
                updatePages();
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        Spinner spinner = (Spinner) mNavView.getMenu().findItem(R.id.navigation_drawer_item3).getActionView();
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this, R.array.states_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStateSelected = parent.getSelectedItem().toString();
                Log.i(LOG_TAG, "STATE SELECTED: " + mStateSelected);
                if (mStateSelected.equals("All")){
                    startService(new Intent(getApplicationContext(), CollegeService.class));
                    Intent publicCollegesIntent = new Intent(getApplicationContext(), CollegeService.class)
                            .putExtra(PUBLIC_COLLEGES_BOOLEAN_KEY, true);
                    startService(publicCollegesIntent);
                } else{
                    startService(new Intent(getApplicationContext(), CollegeService.class)
                            .putExtra(STATE_KEY, mStateSelected));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new Adapter(getSupportFragmentManager());

        Bundle publicBundle = new Bundle();
        publicBundle.putBoolean(PUBLIC_COLLEGES_KEY, true);
        CollegeMainFragment publicCollegeFragment = new CollegeMainFragment();
        publicCollegeFragment.setArguments(publicBundle);

        Bundle favoriteBundle = new Bundle();
        favoriteBundle.putBoolean(FAVORITE_COLLEGES_KEY, true);
        CollegeMainFragment favoritesFragment = new CollegeMainFragment();
        favoritesFragment.setArguments(favoriteBundle);

        mAdapter.addFragment(new CollegeMainFragment(), getString(R.string.colleges));
        mAdapter.addFragment(publicCollegeFragment, getString(R.string.public_));
        mAdapter.addFragment(favoritesFragment, getString(R.string.favorites));
        viewPager.setAdapter(mAdapter);
    }

    private void updatePages(){
        Log.i(LOG_TAG, "UPDATE_PAGES STATE SELECTED: " + mStateSelected);
        CollegeMainFragment collegeFragment = (CollegeMainFragment) mAdapter.getItem(0);
        CollegeMainFragment publicFragment = (CollegeMainFragment) mAdapter.getItem(1);
        collegeFragment.updateStateSelection(mStateSelected);
        publicFragment.updateStateSelection(mStateSelected);

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
        switch (id){
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