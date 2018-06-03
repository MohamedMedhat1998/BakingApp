package com.andalus.abomed7at55.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    private static final String DEFAULT_LABEL = "No Recipe Is Selected";
    private static final String DEFAULT_BODY = "Please open the application and choose a desired recipe";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String label, String body) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setImageViewResource(R.id.iv_widget_icon1, R.drawable.chef);
        views.setImageViewResource(R.id.iv_widget_icon2, R.drawable.chef);
        views.setTextViewText(R.id.tv_widget_label,
                PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.preferences_label), DEFAULT_LABEL));
        views.setTextViewText(R.id.tv_widget_body,
                PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.preferences_body), DEFAULT_BODY));
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    public static void customUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String label, String body) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, label, body);
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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

    }
}

