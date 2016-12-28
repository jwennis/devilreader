package com.example.dreader.devilreader.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StorySyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static StorySyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {

        synchronized (sSyncAdapterLock) {

            if (sSyncAdapter == null) {

                sSyncAdapter = new StorySyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent i) {

        return sSyncAdapter.getSyncAdapterBinder();
    }
}
