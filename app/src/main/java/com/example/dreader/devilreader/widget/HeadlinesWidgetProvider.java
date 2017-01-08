package com.example.dreader.devilreader.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.dreader.devilreader.sync.StorySyncAdapter;

public class HeadlinesWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        context.startService(new Intent(context, HeadlinesWidgetIntentService.class));
    }


    @Override
    public void onEnabled(Context context) {

    }


    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        switch (intent.getAction()) {

            case StorySyncAdapter.ACTION_DB_UPDATE: {

                context.startService(new Intent(context, HeadlinesWidgetIntentService.class));
            }
        }
    }
}

