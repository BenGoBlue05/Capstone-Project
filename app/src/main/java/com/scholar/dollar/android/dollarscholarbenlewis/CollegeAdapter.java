package com.scholar.dollar.android.dollarscholarbenlewis;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.fragment.CollegeMainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bplewis5 on 10/5/16.
 */

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.CollegeAdapterViewHolder> {

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
        String logoUrl = mCursor.getString(CollegeMainFragment.LOGO);
        String city = mCursor.getString(CollegeMainFragment.CITY);
        String state = mCursor.getString(CollegeMainFragment.STATE);
        int ownership = mCursor.getInt(CollegeMainFragment.OWNERSHIP);

        String schoolType;
        if (ownership == 1){
            schoolType = mContext.getString(R.string.public_);
        } else{
            schoolType = mContext.getString(R.string.private_);
        }

        int tuitionIs = mCursor.getInt(CollegeMainFragment.TUITION_IN_STATE);
        int tuitionOs = mCursor.getInt(CollegeMainFragment.TUITION_OUT_STATE);
        int earnings = mCursor.getInt(CollegeMainFragment.EARNINGS);
        double gradRate = mCursor.getDouble(CollegeMainFragment.GRAD_RATE);

        holder.mNameTV.setText(name);
        holder.mCityStateTV.setText(mContext.getString(R.string.city_state, city, state));
        holder.mOwnershipTV.setText(schoolType);
        holder.mTuitionIsTV.setText(mContext.getString(R.string.tuition_cardview, tuitionIs));
        holder.mTuitionOsTV.setText(mContext.getString(R.string.tuition_cardview, tuitionOs));
        holder.mEarningsTV.setText(mContext.getString(R.string.earnings_cardview, earnings));
        holder.mGradRateTV.setText(mContext.getString(R.string.graduation_rate_cardview, gradRate));

        Glide.with(mContext).load(logoUrl).into(holder.mLogoIV);


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

    public Cursor getCursor(){return mCursor;}

    public static interface CollegeAdapterOnClickHandler {
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
