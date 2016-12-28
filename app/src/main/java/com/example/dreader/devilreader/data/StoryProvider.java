package com.example.dreader.devilreader.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;

public class StoryProvider extends ContentProvider {

    public static final int STORY = 100;
    public static final int STORY_FROM_SOURCE = 101;
    public static final int STORY_BY_ID = 102;

    private static final UriMatcher sUriMatcher;

    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(StoryContract.CONTENT_AUTHORITY, StoryContract.PATH_STORY, STORY);
        sUriMatcher.addURI(StoryContract.CONTENT_AUTHORITY, StoryContract.PATH_STORY + "/#", STORY_BY_ID);
        sUriMatcher.addURI(StoryContract.CONTENT_AUTHORITY, StoryContract.PATH_STORY + "/*", STORY_FROM_SOURCE);
    }

    private StoryDbHelper StoryDB;

    @Override
    public boolean onCreate() {

        StoryDB = new StoryDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case STORY: { // story

                return StoryEntry.CONTENT_TYPE_DIR;
            }

            case STORY_FROM_SOURCE: { // story/*

                return StoryEntry.CONTENT_TYPE_DIR;
            }

            case STORY_BY_ID: { // story/#

                return StoryEntry.CONTENT_TYPE_ITEM;
            }

            default: {

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case STORY: {

                cursor = StoryDB.getReadableDatabase().query(StoryEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);

                break;
            }

            case STORY_BY_ID: {

                cursor = StoryDB.getReadableDatabase().query(StoryEntry.TABLE_NAME,
                        projection, "_id = ?", new String[] { uri.getPathSegments().get(1) }, null, null, sortOrder);

                break;
            }

            case STORY_FROM_SOURCE: {

                // TODO: implement proper functionality

                cursor = StoryDB.getReadableDatabase().query(StoryEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);

                break;
            }

            default: {

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = StoryDB.getWritableDatabase();

        Uri rUri;

        switch (sUriMatcher.match(uri)) {

            case STORY: {

                long _id = db.insert(StoryEntry.TABLE_NAME, null, values);

                if (_id > 0) {

                    rUri = StoryEntry.buildUri(_id);

                } else {

                    throw new SQLException("Failed to insert row into " + uri);
                }

                break;
            }

            default: {

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = StoryDB.getWritableDatabase();
        int numUpdated;

        switch (sUriMatcher.match(uri)) {

            case STORY: {

                numUpdated = db.update(StoryEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            }

            case STORY_BY_ID: {

                numUpdated = db.update(StoryEntry.TABLE_NAME, values, "_id = ?", new String[] { uri.getPathSegments().get(1) });

                break;
            }

            default: {

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = StoryDB.getWritableDatabase();
        int numDeleted;

        if (selection == null) {

            selection = "1";
        }

        switch (sUriMatcher.match(uri)) {

            case STORY: {

                numDeleted = db.delete(StoryEntry.TABLE_NAME, selection, selectionArgs);

                break;
            }

            default: {

                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = StoryDB.getWritableDatabase();

        switch(sUriMatcher.match(uri)) {

            case STORY: {

                db.beginTransaction();

                int count = 0;

                try {

                    for (ContentValues value : values) {

                        try {

                            if (db.insert(StoryEntry.TABLE_NAME, null, value) != -1) {

                                count++;
                            }

                        } catch (SQLiteConstraintException e) {

                            // TODO: compare & update if necessary
                            // use insertOrThrow(), then db.update() instead
                        }
                    }

                    db.setTransactionSuccessful();

                } finally {

                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return count;
            }

            default: {

                return super.bulkInsert(uri, values);
            }
        }
    }
}
