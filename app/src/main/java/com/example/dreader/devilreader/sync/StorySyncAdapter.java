package com.example.dreader.devilreader.sync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.util.Log;

import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.Util;
import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.firebase.FirebaseCallback;
import com.example.dreader.devilreader.firebase.FirebaseUtil;
import com.example.dreader.devilreader.model.Game;
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

        syncNews();
        initNotification();
    }

    private void syncNews() {

        FirebaseUtil.queryStory(FirebaseUtil.ORDER_BY, "pubdate", new FirebaseCallback() {

            @Override
            public void onStoryResult(List<Story> list) {

                List<ContentValues> values = new ArrayList<>();

                for(Story item : list) {

                    values.add(item.getInsertValues());
                }

                getContext().getContentResolver().bulkInsert(StoryEntry.CONTENT_URI,
                        values.toArray(new ContentValues[ values.size() ]));
            }
        });
    }


    private void initNotification() {

        final Intent intent = new Intent(getContext(), PuckdropAlarmReceiver.class);
        intent.setAction(PuckdropAlarmReceiver.PUCK_DROP_ALARM);

        if (PendingIntent.getBroadcast(getContext(), 0, intent,
                PendingIntent.FLAG_NO_CREATE) != null) {

            return;
        }

        FirebaseUtil.queryGame(FirebaseUtil.ORDER_BY, "datestring", new FirebaseCallback() {

            @Override
            public void onGameResult(List<Game> list) {

                Calendar current = Calendar.getInstance();
                long msCurrent = current.getTimeInMillis();

                Calendar alarmTime = null;

                for(Game game : list) {

                    String time = game.getPuckdrop();

                    if (time == null) {

                        continue;
                    }

                    Calendar gameDate = Calendar.getInstance(TimeZone.getTimeZone("EST"));

                    int date = (int) game.getDatestring();

                    gameDate.set(Calendar.YEAR, date / 10000);
                    gameDate.set(Calendar.MONTH, (date / 100 % 100) - 1);
                    gameDate.set(Calendar.DATE, date % 100);

                    int split = time.indexOf(":");
                    int hour = Integer.parseInt(time.substring(0, split));
                    int min = Integer.parseInt(time.substring(split + 1, time.length() - 3));

                    boolean isAm = time.indexOf("AM") > -1;

                    if(isAm && hour == 12) {

                        hour = 0;

                    } else if (!isAm && hour < 12) {

                        hour += 12;
                    }

                    gameDate.set(Calendar.HOUR_OF_DAY, hour);
                    gameDate.set(Calendar.MINUTE, min);
                    gameDate.set(Calendar.SECOND, 0);

                    if(gameDate.getTimeInMillis() - msCurrent > 0) {

                        intent.putExtra(Game.PARAM_GAME_PARCEL, game);

                        alarmTime = gameDate;
                        alarmTime.add(Calendar.MINUTE, -5);

                        break;
                    }
                }

                if(alarmTime != null) {

                    PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager manager =
                            (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                    manager.set(AlarmManager.RTC_WAKEUP,
                            //alarmTime.getTimeInMillis(),
                            msCurrent + (30*1000),
                            alarmIntent);
                }
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
