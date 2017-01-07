package com.example.dreader.devilreader.firebase;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class FirebaseListener extends AsyncTask<DataSnapshot, Void, Boolean>
        implements ValueEventListener {


    @Override
    public void onDataChange(DataSnapshot data) {

        execute(data);
    }


    @Override
    public void onCancelled(DatabaseError error) {

        // TODO: handle this gracefully
    }


    @Override
    protected Boolean doInBackground(DataSnapshot... data) {

        onDataResult(data[0]);

        return false;
    }


    protected void onDataResult(DataSnapshot data) {

    }

    protected void resolve() {

        publishProgress();
    }

    @Override
    protected void onProgressUpdate(Void... voids) {

        onPostExecute(true);
    }


    @Override
    protected void onPostExecute(Boolean isTaskComplete) {

        if(isTaskComplete) {

            sendResult();
        }
    }


    protected void sendResult() {

    }
}