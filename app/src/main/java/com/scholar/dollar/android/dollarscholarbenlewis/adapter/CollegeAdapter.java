package com.scholar.dollar.android.dollarscholarbenlewis.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.ui.CollegeMainFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bplewis5 on 10/5/16.
 */

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.CollegeAdapterViewHolder> {
    private static final String LOG_TAG = CollegeAdapter.class.getSimpleName();
    final private Context mContext;
    final private CollegeAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    public CollegeAdapter(Context mContext, CollegeAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public CollegeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college, parent, false);
            view.setFocusable(true);
            return new CollegeAdapterViewHolder(view);
        } else {
            throw new RuntimeException("NOT BOUND TO RECYCLER_VIEW");
        }
    }

    @Override
    public void onBindViewHolder(CollegeAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String name = mCursor.getString(CollegeMainFragment.NAME);
        holder.mNameTV.setText(name);
        holder.mNameTV.setContentDescription(name);

        String city = mCursor.getString(CollegeMainFragment.CITY);
        String state = mCursor.getString(CollegeMainFragment.STATE);
        String cityState = mContext.getString(R.string.city_state, city, state);
        holder.mCityStateTV.setText(cityState);
        holder.mCityStateTV.setContentDescription(cityState);

        int ownership = mCursor.getInt(CollegeMainFragment.OWNERSHIP);
        String schoolType = ownership == 1 ?
                mContext.getString(R.string.public_) : mContext.getString(R.string.private_);
        holder.mOwnershipTV.setText(schoolType);
        holder.mOwnershipTV.setContentDescription(schoolType);

        int tuitionIs = mCursor.getInt(CollegeMainFragment.TUITION_IN_STATE);
        String tuitionInState =  mContext.getString(R.string.tuition_cardview, tuitionIs);
        holder.mTuitionIsTV.setText(tuitionInState);
        holder.mTuitionIsTV.setContentDescription(tuitionInState);

        int tuitionOs = mCursor.getInt(CollegeMainFragment.TUITION_OUT_STATE);
        String tuitionOutState = mContext.getString(R.string.tuition_cardview, tuitionOs);
        holder.mTuitionOsTV.setText(tuitionOutState);
        holder.mTuitionOsTV.setContentDescription(tuitionOutState);

        int earnings = mCursor.getInt(CollegeMainFragment.EARNINGS);
        String income = mContext.getString(R.string.earnings_cardview, earnings);
        holder.mEarningsTV.setText(income);
        holder.mEarningsTV.setContentDescription(income);

        double gradRate = mCursor.getDouble(CollegeMainFragment.GRAD_RATE_6_YEARS);
        String sixYearGradRate = mContext.getString(R.string.graduation_rate_cardview, gradRate);
        holder.mGradRateTV.setText(sixYearGradRate);
        holder.mGradRateTV.setContentDescription(sixYearGradRate);

        String logoUrl = mCursor.getString(CollegeMainFragment.LOGO);
        Picasso.with(mContext).load(logoUrl).placeholder(R.drawable.ic_school_black_24dp).into(holder.mLogoIV);
        holder.mLogoIV.setContentDescription(mContext.getString(R.string.logo));

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {return 0;}
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface CollegeAdapterOnClickHandler {
        void onClick(int collegeId, CollegeAdapterViewHolder vh);
    }

    public class CollegeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name_tv) TextView mNameTV;
        @BindView(R.id.city_state_tv) TextView mCityStateTV;
        @BindView(R.id.ownership_tv) TextView mOwnershipTV;
        @BindView(R.id.tuition_is_tv) TextView mTuitionIsTV;
        @BindView(R.id.tuition_os_tv) TextView mTuitionOsTV;
        @BindView(R.id.earnings_tv) TextView mEarningsTV;
        @BindView(R.id.grad_rate_tv) TextView mGradRateTV;
        @BindView(R.id.logo_iv) ImageView mLogoIV;

        public CollegeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int collegeIdInd = mCursor.getColumnIndex(CollegeContract.CollegeMainEntry.COLLEGE_ID);
            mClickHandler.onClick(mCursor.getInt(collegeIdInd), this);
        }
    }


}