package com.example.dreader.devilreader.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dreader.devilreader.MainActivity;
import com.example.dreader.devilreader.R;
import com.example.dreader.devilreader.StoryActivity;
import com.example.dreader.devilreader.data.StoryContract;
import com.example.dreader.devilreader.model.Story;

import java.util.ArrayList;
import java.util.List;

public class HeadlinesWidgetIntentService extends IntentService {

    public HeadlinesWidgetIntentService() {

        super("HeadlinesWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] widgetIds = manager.getAppWidgetIds(new ComponentName(this, HeadlinesWidgetProvider.class));

        Uri storyUri = StoryContract.StoryEntry.CONTENT_URI;

        String[] projection = new String[] { };
        String selection = "";
        String[] selectionArgs = new String[] { };
        String sortOrder = String.format("%1$s DESC LIMIT 3", StoryContract.StoryEntry.COL_PUBDATE);

        Cursor data = getContentResolver().query(storyUri,
                projection, selection, selectionArgs, sortOrder);

        if(data == null) { return; }

        List<Story> items = new ArrayList<>();

        try {

            while (data.moveToNext()) {

                items.add(new Story(data));
            }

        } finally {

            data.close();
        }

        for (int id : widgetIds) {

            updateWidget(items, manager, id);
        }
    }

    private void updateWidget(List<Story> items, AppWidgetManager manager, int widgetId) {

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_headlines);

        // Heading

        Intent discoverIntent = new Intent(this, MainActivity.class);
        PendingIntent discoverPending = PendingIntent.getActivity(this, 0, discoverIntent, 0);

        views.setOnClickPendingIntent(R.id.widget_heading, discoverPending);
        views.setImageViewResource(R.id.widget_icon, R.drawable.ic_menu_news_white);

        Resources res = getResources();
        String packageName = getPackageName();

        for(int i = 0; i < items.size(); i++) {

            Story story = items.get(i);

            Intent articleIntent = new Intent(this, StoryActivity.class);
            articleIntent.putExtra(Story.PARAM_STORY_PARCEL, story);
            articleIntent.putExtra("WIDGET_INDEX", i);
            PendingIntent articlePending = PendingIntent.getActivity(this, i, articleIntent, 0);

            int headlineId = res.getIdentifier("widget_headline_" + (i+1), "id", packageName);
            views.setOnClickPendingIntent(headlineId, articlePending);

            int titleId = res.getIdentifier("widget_headline_title_" + (i+1), "id", packageName);
            views.setTextViewText(titleId, story.getTitle());

            int subtitleId = res.getIdentifier("widget_headline_subtitle_" + (i+1), "id", packageName);
            views.setTextViewText(subtitleId, story.getShortByline());
        }

        manager.updateAppWidget(widgetId, views);
    }
}