//package com.scholar.dollar.android.dollarscholarbenlewis.widget;
//
//import android.app.PendingIntent;
//import android.app.TaskStackBuilder;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.widget.RemoteViews;
//
//import com.scholar.dollar.android.dollarscholarbenlewis.R;
//import com.scholar.dollar.android.dollarscholarbenlewis.activities.DetailActivity;
//import com.scholar.dollar.android.dollarscholarbenlewis.activities.MainActivity;
//import com.scholar.dollar.android.dollarscholarbenlewis.service.CollegeFavoriteService;
//
///**
// * Implementation of App Widget functionality.
// */
//public class CollegeWidget extends AppWidgetProvider {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//        if (intent.getAction().equals(CollegeFavoriteService.ACTION_DATA_UPDATED)){
//            AppWidgetManager manager = AppWidgetManager.getInstance(context);
//            int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, getClass()));
//            manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
//        }
//    }
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        for (int appWidgetId : appWidgetIds) {
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_college);
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//            views.setRemoteAdapter(R.id.widget_list, new Intent(context, CollegeRemoteViewService.class));
//            views.setEmptyView(R.id.widget_list, R.id.widget_empty_textview);
//            Intent clickIntentTemplate = new Intent(context, DetailActivity.class);
//            PendingIntent clickPendingIntent = TaskStackBuilder.create(context)
//                    .addNextIntentWithParentStack(clickIntentTemplate)
//                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
//    }
//
//    @Override
//    public void onEnabled(Context context) {
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//    }
//}
//
