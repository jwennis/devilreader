package com.example.dreader.devilreader.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class StoryAuthenticatorService extends Service {

    private StoryAuthenticator mAuthenticator;


    @Override
    public void onCreate() {

        mAuthenticator = new StoryAuthenticator(this);
    }


    @Override
    public IBinder onBind(Intent i) {

        return mAuthenticator.getIBinder();
    }
}
