package com.andalus.abomed7at55.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    private static final String DEFAULT_LABEL = "No Recipe Is Selected";
    private static final String DEFAULT_BODY = "Please open the application and choose a desired recipe";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        Log.d("State", "A");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setImageViewResource(R.id.iv_widget_icon1, R.drawable.chef);
        views.setImageViewResource(R.id.iv_widget_icon2, R.drawable.chef);
        Log.d("Label", PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.preferences_label), DEFAULT_LABEL));
        Log.d("Body", PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.preferences_body), DEFAULT_BODY));
        views.setTextViewText(R.id.tv_widget_label,
                PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.preferences_label), DEFAULT_LABEL));
        views.setTextViewText(R.id.tv_widget_body,
                PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.preferences_body), DEFAULT_BODY));
        Log.d("State", "B");
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d("State", "C");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

