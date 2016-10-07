package com.scholar.dollar.android.dollarscholarbenlewis;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.fragment.CollegeMainFragment;
import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeDetailService;

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
    @BindView(R.id.detail_college_name)
    TextView mNameTV;
    @BindView(R.id.detail_college_url) TextView mSchoolUrl;
    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollegeId = getActivity().getIntent().getIntExtra("collegeIdKey", -1);
        if (mCollegeId != -1){
            getContext().startService(new Intent(getContext(), CollegeDetailService.class)
                    .putExtra("collegeIdKey", mCollegeId));
            getLoaderManager().initLoader(MAIN_INFO_CURSOR_ID, null, this);
            getLoaderManager().initLoader(DETAIL_CURSOR_ID, null, this);
        }
    }

    public static final String[] COLLEGE_DETAIL_COLUMNS = {
            CollegeContract.CollegeDetailEntry.SCHOOL_URL
    };

    public static final int SCHOOL_URL = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
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
                Log.i(LOG_TAG, "MAIN COLUMNS: " + data.getColumnCount());
                mNameTV.setText(data.getString(CollegeMainFragment.NAME));
                break;
            case DETAIL_CURSOR_ID:
                Log.i(LOG_TAG, "DETAIL COLUMNS: " + data.getColumnCount());
                String schoolUrl = data.getString(SCHOOL_URL);
                Log.i(LOG_TAG, schoolUrl);
                mSchoolUrl.setText(data.getString(SCHOOL_URL));
                break;
            default:
                Log.i(LOG_TAG, "CURSOR LOADER ID NOT FOUND");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
