package com.scholar.dollar.android.dollarscholarbenlewis.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.utility.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.CollegeAdapterViewHolder> {
    private static final String LOG_TAG = CollegeAdapter.class.getSimpleName();
    final private Context mContext;
    final private CollegeAdapterOnClickHandler mClickHandler;
    final private StarClickHandler mStarClickHandler;
    private Cursor mCursor;

    public CollegeAdapter(Context mContext, CollegeAdapterOnClickHandler mClickHandler, StarClickHandler starClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
        mStarClickHandler = starClickHandler;
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
    public void onBindViewHolder(final CollegeAdapterViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }
        mCursor.moveToPosition(position);

        String name = mCursor.getString(Utility.NAME);
        holder.mNameTV.setText(name);
        holder.mNameTV.setContentDescription(name);

        String city = mCursor.getString(Utility.CITY);
        String state = mCursor.getString(Utility.STATE);
        String cityState = mContext.getString(R.string.city_state, city, state);
        holder.mCityStateTV.setText(cityState);
        holder.mCityStateTV.setContentDescription(cityState);

        int ownership = mCursor.getInt(Utility.OWNERSHIP);
        String schoolType = ownership == 1 ?
                mContext.getString(R.string.public_) : mContext.getString(R.string.private_);
        holder.mOwnershipTV.setText(schoolType);
        holder.mOwnershipTV.setContentDescription(schoolType);

        boolean isFavorite = mCursor.getInt(Utility.FAVORITE) == 1;
        if (isFavorite) {
            holder.mStarTV.setImageResource(R.drawable.star_yellow);
        } else{
            holder.mStarTV.setImageResource(R.drawable.star_gray);
        }

        float earnings = (float) mCursor.getInt(Utility.EARNINGS);
        String income = "" + Math.round((earnings/1000f));
        holder.mEarningsTV.setText(income);
        holder.mEarningsTV.setContentDescription(income);
        if (position < 50){
            holder.mRankTV.setText(String.valueOf(position + 1));
            holder.mRankTV.setVisibility(View.VISIBLE);
        } else{ holder.mRankTV.setVisibility(View.GONE);
        }
        String logoUrl = mCursor.getString(Utility.LOGO);
        Picasso.with(mContext).load(logoUrl).placeholder(R.drawable.gray)
                .error(R.drawable.ic_school_black_24dp).into(holder.mLogoIV);
        holder.mLogoIV.setContentDescription(mContext.getString(R.string.logo));

        if (ownership == 1){
            holder.mInStateLL.setVisibility(View.VISIBLE);
            float tuitionIs = (float) mCursor.getInt(Utility.TUITION_IN_STATE);
            String tuitionInState = "" + Math.round((tuitionIs/1000f));
            holder.mTuitionIsTV.setText(tuitionInState);
            holder.mTuitionIsTV.setContentDescription(tuitionInState);
            holder.mOutStateLabelTV.setVisibility(View.VISIBLE);
        } else{
            holder.mInStateLL.setVisibility(View.GONE);
            holder.mOutStateLabelTV.setVisibility(View.GONE);
        }
        float tuitionOs = (float) mCursor.getInt(Utility.TUITION_OUT_STATE);
        String tuitionOutState = "" + Math.round((tuitionOs/1000f));
        holder.mTuitionOsTV.setText(tuitionOutState);
        holder.mTuitionOsTV.setContentDescription(tuitionOutState);
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
        void onClick(int collegeId, boolean isPublic, CollegeAdapterViewHolder vh);
    }
    public interface StarClickHandler{
        void onStarClick(int collegeId, boolean isFavorite, ImageView star);
    }

    public class CollegeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name_tv) TextView mNameTV;
        @BindView(R.id.city_state_tv) TextView mCityStateTV;
        @BindView(R.id.ownership_tv) TextView mOwnershipTV;
        @BindView(R.id.main_tuition_is_tv) TextView mTuitionIsTV;
        @BindView(R.id.tuition_os_tv) TextView mTuitionOsTV;
        @BindView(R.id.earnings_tv) TextView mEarningsTV;
        @BindView(R.id.college_logo) ImageView mLogoIV;
        @BindView(R.id.main_is_ll) LinearLayout mInStateLL;
        @BindView(R.id.main_os_label_tv) TextView mOutStateLabelTV;
        @BindView(R.id.item_star_tv) ImageView mStarTV;
        @BindView(R.id.rank_tv) TextView mRankTV;

        public CollegeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mStarTV.setOnClickListener(this);
        }

        public View getLogoIV(){
            return mLogoIV;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int collegeId = mCursor.getInt(Utility.COLLEGE_ID);
            if (v.getId() == mStarTV.getId()){
                mStarClickHandler.onStarClick(collegeId, mCursor.getInt(Utility.FAVORITE) == 1, mStarTV);
            } else{
                mClickHandler.onClick(collegeId, mCursor.getInt(Utility.OWNERSHIP) == 1, this);
            }
        }
    }


}