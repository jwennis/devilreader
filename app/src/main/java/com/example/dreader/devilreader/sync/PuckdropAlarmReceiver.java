package com.example.dreader.devilreader.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PuckdropAlarmReceiver extends BroadcastReceiver {

    public static final String PUCK_DROP_ALARM = "PUCK_DROP_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("DREADER", "PUCKDROP ALARM FIRED");

    }
}
