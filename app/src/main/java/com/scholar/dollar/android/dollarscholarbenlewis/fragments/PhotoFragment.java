package com.scholar.dollar.android.dollarscholarbenlewis.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.scholar.dollar.android.dollarscholarbenlewis.R;

import java.util.ArrayList;

import static com.scholar.dollar.android.dollarscholarbenlewis.activities.DetailActivity.sGoogleApiClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = PhotoFragment.class.getSimpleName();

    private ArrayList<AttributedPhoto> mPhotos;

    public void photosTask(String placeId){
        new PhotoTask().execute(placeId);
    }

    public PhotoFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        if (mPhotos == null || mPhotos.size() < 1){
            mPhotos = new ArrayList<>();
        }
        PhotoAdapter adapter = new PhotoAdapter(mPhotos, recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView mPhotoIV;
        TextView mAttrTV;

        public PhotoViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_photo, parent, false));
            mPhotoIV = (ImageView) itemView.findViewById(R.id.photo_iv);
            mAttrTV = (TextView) itemView.findViewById(R.id.photo_attr_tv);

        }
    }

    public static class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

        private ArrayList<AttributedPhoto> photos;
        private Context context;

        public PhotoAdapter(ArrayList<AttributedPhoto> photos, Context context) {
            this.photos = photos;
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PhotoViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            AttributedPhoto photo = photos.get(position);
            holder.mPhotoIV.setImageBitmap(photo.getBitmap());
            holder.mAttrTV.setText(photo.getAttribution());
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public class PhotoTask extends AsyncTask<String, Void, Void> {

        private int mHeight;

        private int mWidth;

        public PhotoTask() {
        }

        /**
         * Loads the first photo for a place id from the Geo Data API.
         * The place id must be the first (and only) parameter.
         */
        @Override
        protected Void doInBackground(String... params) {
            if (params.length != 1) {
                return null;
            }
            final String placeId = params[0];
            AttributedPhoto attributedPhoto = null;

            PlacePhotoMetadataResult result = Places.GeoDataApi
                    .getPlacePhotos(sGoogleApiClient, placeId).await();

            if (result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                if (photoMetadataBuffer.getCount() > 1 && !isCancelled()) {
                    // Get the first bitmap and its attributions.
                    int numPhotos = photoMetadataBuffer.getCount();
                    int maxPhotos = numPhotos < 10 ? numPhotos : 10;
                    for (int i = 1; i < numPhotos; i++) {
                        PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
                        CharSequence attribution = photo.getAttributions();
                        // Load a scaled bitmap for this photo.
                        Bitmap image = photo.getPhoto(sGoogleApiClient).await()
                                .getBitmap();
                        attributedPhoto = new AttributedPhoto(attribution, image);
                        if (mPhotos == null || mPhotos.size() < 1){
                            mPhotos = new ArrayList<>();
                        }
                        try{
                            mPhotos.add(attributedPhoto);
                        } catch (NullPointerException e){
                            Log.e(LOG_TAG, "PHOTO LIST IS NULL");
                        }
                    }
                }
                // Release the PlacePhotoMetadataBuffer.
                photoMetadataBuffer.release();
            }
            return null;
        }

    }


    public class AttributedPhoto {

        public final CharSequence attribution;

        public final Bitmap bitmap;

        public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
            this.attribution = attribution;
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public CharSequence getAttribution() {
            return attribution;
        }
    }


}
