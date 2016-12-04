package com.scholar.dollar.android.dollarscholarbenlewis.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.activities.DetailActivity;
import com.scholar.dollar.android.dollarscholarbenlewis.adapter.CollegeAdapter;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.model.CollegeBasic;
import com.scholar.dollar.android.dollarscholarbenlewis.model.User;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class CollegeMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = CollegeMainFragment.class.getSimpleName();
//    OnStateSelectedListener mCallback;


    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    public static final String SORT_HIGHEST_EARNINGS = CollegeContract.CollegeMainEntry.MED_EARNINGS_2012 + " DESC";
    public static final int COLLEGE_LOADER = 1000;
    private CollegeAdapter mCollegeAdapter;
    private boolean mIsPublic;
    private boolean mIsFavorite;
    private String mState;
    private Bundle mArgs;
    private String mUid;

    public CollegeMainFragment() {
    }

    public String getState() {
        return mState;
    }

    public void setState(String mState) {
        this.mState = mState;
        onStateChanged();
    }

    private void onStateChanged() {
        Bundle args = mArgs != null ? (Bundle) mArgs.clone() : null;
        if (args != null) {
            args.putString(Utility.STATE_KEY, mState);
        }
        getLoaderManager().restartLoader(COLLEGE_LOADER, args, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mArgs = getArguments() != null ? getArguments() : null;
        mIsPublic = mArgs != null && mArgs.getBoolean(Utility.PUBLIC_COLLEGE_KEY, false);
        mIsFavorite = mArgs != null && mArgs.getBoolean(Utility.PUBLIC_COLLEGE_KEY, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUid = user != null ? user.getUid() : null;
        mCollegeAdapter = new CollegeAdapter(getContext(), new CollegeAdapter.CollegeAdapterOnClickHandler() {
            @Override
            public void onClick(int collegeId, boolean isPublic, CollegeAdapter.CollegeAdapterViewHolder vh) {
                Log.i(LOG_TAG, "COLLEGE ID: " + collegeId);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(collegeId));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                startActivity(new Intent(getContext(), DetailActivity.class)
                        .putExtra(Utility.COLLEGE_ID_KEY, collegeId)
                        .putExtra(Utility.PUBLIC_COLLEGE_KEY, isPublic));
            }
        }, new CollegeAdapter.StarClickHandler() {
            @Override
            public void onStarClick(int collegeId, boolean isFavorite, ImageView star) {
                int newFavorite = !isFavorite ? 1 : 0;
                ContentValues values = new ContentValues();
                values.put(CollegeContract.CollegeMainEntry.IS_FAVORITE, newFavorite);
                getContext().getContentResolver().update(CollegeContract.CollegeMainEntry.buildMainWithCollegeId(collegeId),
                        values, null, null);
                if (!isFavorite) {
                    star.setImageResource(R.drawable.ic_star_yellow_24dp);
                } else {
                    star.setImageResource(R.drawable.ic_star_gray_24dp);
                }

                DatabaseReference userFavRef = mDatabase.child("users").child(mUid);
                DatabaseReference collegeRef = mDatabase.child("csc-ref").child(String.valueOf(collegeId));
                onStarClicked(userFavRef, true, collegeId, !isFavorite);
                onStarClicked(collegeRef, false, collegeId, !isFavorite);
            }
        });
        recyclerView.setAdapter(mCollegeAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getLoaderManager().initLoader(COLLEGE_LOADER, mArgs, this);
        return rootView;
    }

    private void onStarClicked(DatabaseReference ref, boolean isUser, final int collegeId, final boolean newFavorite) {
        if (!isUser) {
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    CollegeBasic college = mutableData.getValue(CollegeBasic.class);

                    if (college == null) {
                        return Transaction.success(mutableData);
                    }

                    if (newFavorite) {
                        if (!college.stars.containsKey(mUid)) {
                            college.starCount = college.starCount + 1;
                            college.stars.put(mUid, true);
                        }
                    } else {
                        if (college.stars.containsKey(mUid)) {
                            college.starCount = college.starCount - 1;
                            college.stars.remove(mUid);
                        }
                    }
                    // Set value and report transaction success
                    mutableData.setValue(college);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(LOG_TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        } else {
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    User user = mutableData.getValue(User.class);
                    if (user == null) {
                        return Transaction.success(mutableData);
                    }

                    if (newFavorite){
                        if (!user.favorites.containsKey(String.valueOf(collegeId))){
                            user.favoritesCount = user.favoritesCount + 1;
                            user.favorites.put(String.valueOf(collegeId), true);
                        }
                    } else{
                        if (user.favorites.containsKey(String.valueOf(collegeId))){
                            user.favoritesCount = user.favoritesCount - 1;
                            user.favorites.remove(String.valueOf(collegeId));
                        }
                    }
                    // Set value and report transaction success
                    mutableData.setValue(user);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Log.d(LOG_TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        }

    }


    @Override
    public void onLoaderReset(Loader loader) {
        mCollegeAdapter.swapCursor(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;
        List<String> selectList = new ArrayList<>();
        List<String> argList = new ArrayList<>();

        if (args != null) {
            String state = args.getString(Utility.STATE_KEY, "All");
            Log.i(LOG_TAG, "ON_CREATE_LOADER STATE: " + state);
            if (!state.equals("All")) {
                selectList.add(CollegeContract.CollegeMainEntry.STATE + " = ? ");
                argList.add(state);
            }
            if (args.getBoolean(Utility.PUBLIC_COLLEGE_KEY, false)) {
                Log.i(LOG_TAG, "ON CREATE LOADER: PUBLIC");
                selectList.add(CollegeContract.CollegeMainEntry.OWNERSHIP + " = ? ");
                argList.add("1");
            } else if (args.getBoolean(Utility.FAVORITE_COLLEGE_KEY)) {
                selectList.add(CollegeContract.CollegeMainEntry.IS_FAVORITE + " = ? ");
                argList.add("1");
            }
        }
        int size = selectList.size();
        if (size > 0) {
            selection = "";
            selectionArgs = new String[size];
            for (int i = 0; i < size; i++) {
                selection += selectList.get(i);
                selectionArgs[i] = argList.get(i);
                Log.i(LOG_TAG, "SELECTION ARG: " + selectionArgs[i]);
                if (i < size - 1) {
                    selection += " AND ";
                }
            }
            Log.i(LOG_TAG, "SELECTION: " + selection);
        } else {
            selection = null;
            selectionArgs = null;
        }

        return new CursorLoader(getContext(), CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI,
                Utility.COLLEGE_COLUMNS, selection, selectionArgs, SORT_HIGHEST_EARNINGS);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCollegeAdapter.swapCursor(data);
    }


}