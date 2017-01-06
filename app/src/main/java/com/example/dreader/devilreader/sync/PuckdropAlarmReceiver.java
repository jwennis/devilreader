package com.example.dreader.devilreader.sync;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dreader.devilreader.MainActivity;
import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.Util;
import com.example.dreader.devilreader.model.Game;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PuckdropAlarmReceiver extends BroadcastReceiver {

    public static final String PUCK_DROP_ALARM = "PUCK_DROP_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {

        Game game = intent.getParcelableExtra(Game.PARAM_GAME_PARCEL);

        if(game == null || !Util.getPreferences(context)
                .getBoolean("pref_general_puckdrop_notification", true)) {

            return;
        }

        String title = "Puck drop in 5 minutes";
        String message = String.format("Devils %1$s %2$s",
                game.isHome() ? "vs" : "@", Util.getTeamName(game.getOpponent()));

        Intent discoverIntent = new Intent(context, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, discoverIntent, 0);

        NotificationCompat.Builder notifBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pending)
                        .setSmallIcon(R.drawable.ic_notif_sticks);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, notifBuilder.build());
    }
}
