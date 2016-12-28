package com.example.dreader.devilreader.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class StoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.dreader.devilreader";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STORY = "story";

    public static final class StoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STORY).build();

        private static final String AUTH_PATH_STORY =  "/" + CONTENT_AUTHORITY + "/" + PATH_STORY;

        public static final String CONTENT_TYPE_DIR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + AUTH_PATH_STORY;

        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + AUTH_PATH_STORY;

        public static final String TABLE_NAME = "story";

        // required
        public static final String COL_FIREBASE_KEY = "firebase_key";
        public static final String COL_TITLE = "title";
        public static final String COL_LINK = "link";
        public static final String COL_PUBDATE = "pubdate";
        public static final String COL_SOURCE = "source";

        // optional
        public static final String COL_AUTHOR = "author";
        public static final String COL_TAGLINE = "tagline";
        public static final String COL_ATTACHMENT = "attachment";
        public static final String COL_MEDIA = "media";

        public static final String COL_IS_READ = "is_read";
        public static final String COL_IS_SAVED = "is_saved";

        public static Uri buildUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
