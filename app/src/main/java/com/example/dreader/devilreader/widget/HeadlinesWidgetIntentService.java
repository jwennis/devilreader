package com.example.dreader.devilreader.widget;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class HeadlinesWidgetIntentService extends IntentService {

    public HeadlinesWidgetIntentService() {

        super("HeadlinesWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v("DREADER", "onHandleIntent() fired");

    }
}