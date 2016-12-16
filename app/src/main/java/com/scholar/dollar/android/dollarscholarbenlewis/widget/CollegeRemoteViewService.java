package com.scholar.dollar.android.dollarscholarbenlewis.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.scholar.dollar.android.dollarscholarbenlewis.R;
import com.scholar.dollar.android.dollarscholarbenlewis.data.CollegeContract;
import com.scholar.dollar.android.dollarscholarbenlewis.fragments.CollegeMainFragment;

public class CollegeRemoteViewService extends RemoteViewsService {

    private static final String LOG_TAG = CollegeRemoteViewService.class.getSimpleName();
    private static final String[] REMOTE_COLUMNS =
            {CollegeContract.CollegeMainEntry.COLLEGE_ID, CollegeContract.CollegeMainEntry.NAME};
    private static final int COLLEGE_ID = 0;
    private static final int NAME = 1;
    public CollegeRemoteViewService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null){
                    data.close();
                }
                final long idToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(CollegeContract.CollegeMainEntry.COLLEGE_MAIN_CONTENT_URI,
                        REMOTE_COLUMNS,
                        CollegeContract.CollegeMainEntry.IS_FAVORITE + " = ? ",
                        new String[]{"1"},
                        CollegeMainFragment.SORT_HIGHEST_EARNINGS);
                Binder.restoreCallingIdentity(idToken);
            }

            @Override
            public void onDestroy() {
                if (data != null){
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_list_item);
                remoteViews.setTextViewText(R.id.widget_name_tv, data.getString(NAME));
                int collegeId = data.getInt(COLLEGE_ID);
                Uri uri = CollegeContract.CollegeMainEntry.buildMainWithCollegeId(collegeId);
                Intent intent = new Intent().setData(uri)
                        .putExtra("collegeIdKey", collegeId);
                remoteViews.setOnClickFillInIntent(R.id.widget_list_item, intent);
                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getInt(COLLEGE_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
