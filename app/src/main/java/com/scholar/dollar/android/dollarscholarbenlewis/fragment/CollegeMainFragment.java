package com.scholar.dollar.android.dollarscholarbenlewis.fragment;

import android.database.Cursor;
import android.net.Uri;
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

import com.scholar.dollar.android.dollarscholarbenlewis.CollegeAdapter;
import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;

/**
 * Created by bplewis5 on 10/3/16.
 */

public class CollegeMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = CollegeMainFragment.class.getSimpleName();

    private Uri mUri;
    private static final int COLLEGE_LOADER = 1000;
    private CollegeAdapter mCollegeAdapter;
    private static final String[] COLLEGE_COLUMNS = {
        CollegeContract.CollegeMainEntry.COLLEGE_ID,
                CollegeContract.CollegeMainEntry.NAME,
                CollegeContract.CollegeMainEntry.LOGO_URL,
                CollegeContract.CollegeMainEntry.CITY,
                CollegeContract.CollegeMainEntry.STATE,
                CollegeContract.CollegeMainEntry.OWNERSHIP,
                CollegeContract.CollegeMainEntry.TUITION_IN_STATE,
                CollegeContract.CollegeMainEntry.TUITION_OUT_STATE,
                CollegeContract.CollegeMainEntry.MED_EARNINGS_2012,
                CollegeContract.CollegeMainEntry.GRADUATION_RATE_6_YEAR,
    };

    public static final int COLLEGE_ID = 0;
    public static final int NAME = 1;
    public static final int LOGO = 2;
    public static final int CITY = 3;
    public static final int STATE = 4;
    public static final int OWNERSHIP = 5;
    public static final int TUITION_IN_STATE = 6;
    public static final int TUITION_OUT_STATE = 7;
    public static final int EARNINGS = 8;
    public static final int GRAD_RATE = 9;


    public CollegeMainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mCollegeAdapter = new CollegeAdapter(getContext(), new CollegeAdapter.CollegeAdapterOnClickHandler() {
            @Override
            public void onClick(int collegeId, CollegeAdapter.CollegeAdapterViewHolder vh) {
                Log.i(LOG_TAG, "CLICKED");
            }
        });

        recyclerView.setAdapter(mCollegeAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(COLLEGE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        mCollegeAdapter.swapCursor(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = CollegeContract.CollegeMainEntry.MED_EARNINGS_2012 + " DESC";
        Uri uri = CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI;
        return new CursorLoader(getContext(), uri, COLLEGE_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCollegeAdapter.swapCursor(data);
    }
}
