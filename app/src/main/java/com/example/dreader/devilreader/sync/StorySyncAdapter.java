package com.example.dreader.devilreader.sync;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Story;


public class StorySyncAdapter extends AbstractThreadedSyncAdapter {

    private static final int SYNC_INTERVAL = 1 * 60 * 60;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;


    public StorySyncAdapter(Context context, boolean autoInit) {

        super(context, autoInit);
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        FirebaseUtil.queryStory(FirebaseUtil.ORDER_BY, "pubdate", new FirebaseCallback() {

            @Override
            public void onStoryResult(List<Story> list) {

                List<ContentValues> values = new ArrayList<>();

                for(Story item : list) {

                    values.add(item.getInsertValues());
                }

                getContext().getContentResolver().bulkInsert(StoryEntry.CONTENT_URI,
                        values.toArray(new ContentValues[ values.size() ]));

                //getContext().sendBroadcast(new Intent(ACTION_DB_UPDATE));
            }
        });
    }


    public static void syncImmediately(Context context) {

        Bundle args = new Bundle();
        args.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        args.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), args);
    }


    public static Account getSyncAccount(Context context) {

        AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (manager.getPassword(account) == null) {

            if (!manager.addAccountExplicitly(account, "", null)) {

                return null;
            }

            onAccountCreated(account, context);
        }

        return account;
    }


    private static void onAccountCreated(Account account, Context context) {

        StorySyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(account, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }


    public static void configurePeriodicSync(Context context, int interval, int flex) {

        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {

            SyncRequest req = new SyncRequest.Builder()
                    .syncPeriodic(interval, flex)
                    .setSyncAdapter(account, authority)
                    .setExtras(new Bundle()).build();

            ContentResolver.requestSync(req);

        } else {

            ContentResolver.addPeriodicSync(account, authority, new Bundle(), interval);
        }
    }


    public static void initializeSyncAdapter(Context context) {

        getSyncAccount(context);
    }
}
