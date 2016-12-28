package com.example.dreader.devilreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;

public class StoryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "story.db";
    private static final int DATABASE_VERSION = 1;

    public StoryDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_STORY_TABLE = "CREATE TABLE " +

                StoryEntry.TABLE_NAME + " (" +
                StoryEntry._ID + " INTEGER PRIMARY KEY, " +

                StoryEntry.COL_ID + " TEXT UNIQUE NOT NULL, " +
                StoryEntry.COL_TITLE + " TEXT NOT NULL, " +
                StoryEntry.COL_LINK + " TEXT NOT NULL, " +
                StoryEntry.COL_PUBDATE + " INTEGER NOT NULL, " +
                StoryEntry.COL_SOURCE + " TEXT NOT NULL, " +

                StoryEntry.COL_AUTHOR + " TEXT, " +
                StoryEntry.COL_TAGLINE + " TEXT, " +
                StoryEntry.COL_ATTACHMENT + " TEXT, " +
                StoryEntry.COL_MEDIA + " TEXT, " +

                StoryEntry.COL_IS_READ + " INTEGER NOT NULL, " +
                StoryEntry.COL_IS_SAVED + " INTEGER NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_STORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StoryEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
