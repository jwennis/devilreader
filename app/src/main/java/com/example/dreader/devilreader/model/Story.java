package com.example.dreader.devilreader.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.dreader.devilreader.Util;
import com.example.dreader.devilreader.data.StoryContract.StoryEntry;


public class Story {

    private long _id; // SQLite key
    private String id; // Firebase key

    private String title;
    private String link;
    private String source;
    private long pubdate;

    private String author;
    private String tagline;
    private String attachment;
    private String media;
    private List<String> content;

    private boolean isRead;
    private long isSaved;

    private Cursor mData;

    public Story() {

    }


    /**
     * Construct Story from Cursor (via NewsFragment)
     * for the purposes of populating a RecyclerView
     *
     * @param data Cursor containing Story data
     */
    public Story (Cursor data) {

        mData = data;

        _id = getCursorLong(StoryEntry._ID);
        id = getCursorString(StoryEntry.COL_ID);
        title = getCursorString(StoryEntry.COL_TITLE);
        link = getCursorString(StoryEntry.COL_LINK);
        pubdate = getCursorLong(StoryEntry.COL_PUBDATE);
        source = getCursorString(StoryEntry.COL_SOURCE);

        isRead = getCursorBool(StoryEntry.COL_IS_READ);
        isSaved = getCursorLong(StoryEntry.COL_IS_SAVED);

        String _author = getCursorString(StoryEntry.COL_AUTHOR);
        if(_author != null) { author = _author; }

        String _tagline = getCursorString(StoryEntry.COL_TAGLINE);
        if(_tagline != null) { tagline = _tagline; }

        String _attachment = getCursorString(StoryEntry.COL_ATTACHMENT);
        if(_attachment != null) { attachment = _attachment; }

        String _media = getCursorString(StoryEntry.COL_MEDIA);
        if(_media != null) { media = _media; }
    }


    private String getCursorString(String columnName) {

        int index = mData.getColumnIndex(columnName);

        return index > -1 ? mData.getString(index) : null;
    }


    private long getCursorLong(String columnName) {

        int index = mData.getColumnIndex(columnName);

        return index > -1 ? mData.getLong(index) : -1;
    }


    private boolean getCursorBool(String columnName) {

        return getCursorLong(columnName) == 1;
    }


    // Accessors


    /**
     * Gets the SQLite primary key
     *
     * @return the key
     */
    public long getKey() {

        return _id;
    }


    /**
     * Gets the Firebase key
     *
     * @return the key
     */
    public String getId() {

        return id;
    }


    public String getTitle() {

        return title;
    }


    public String getLink() {

        return link;
    }


    public String getSource() {

        return source;
    }


    public long getPubdate() {

        return pubdate;
    }


    public String getAuthor() {

        return author;
    }


    public String getTagline() {

        return tagline;
    }


    public String getAttachment() {

        return attachment;
    }


    public String getMedia() {

        return media;
    }


    public List<String> getContent() {

        return content;
    }


    public boolean isRead() {

        return isRead;
    }


    public boolean isSaved() {

        return isSaved > 0;
    }


    public long getSavedTimestamp() {

        return Math.abs(isSaved);
    }


    public String getShortByline() {

        return String.format("%1$s / %2$s", getSource(), getElapsed());
    }


    private String getElapsed() {

        final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

        String datestring = Long.toString(pubdate);

        String iso8601 = String.format("%s-%s-%sT%s:%s:00Z",
                datestring.substring(0, 4), // year
                datestring.substring(4, 6), // month
                datestring.substring(6, 8), // date
                datestring.substring(8, 10), // hour
                datestring.substring(10, 12)); // minute

        SimpleDateFormat format = new SimpleDateFormat(ISO8601_FORMAT, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        long current = c.getTimeInMillis();

        try {

            c.setTime(format.parse(iso8601));

            long published = c.getTimeInMillis();
            int diff = (int) ((current - published) / (60 * 1000));

            int value;
            String label;

            if (diff < 60) {

                value = diff;
                label = "minute";

            } else if (diff / 60 < 24) {

                value = diff / 60;
                label = "hour";

            } else if (diff / (60 * 24) < 30) {

                value = diff / (60 * 24);
                label = "day";

            } else {

                value = diff / (60 * 24 * 30);
                label = "month";
            }

            return value + " " + label + (value > 1 ? "s" : "") + " ago";

        } catch(ParseException e) {

            return iso8601;
        }
    }


    public void markAsRead() {

        isRead = true;
    }


    public void toggleIsSaved() {

        isSaved = isSaved()
                ? 0 - Util.getCurrentTimestamp() : Util.getCurrentTimestamp();
    }

    /**
     * Creates ContentValues for the purpose of saving to SQLite database
     *
     * @return ContentValues containing data properties of this instance
     */
    public ContentValues getInsertValues() {

        ContentValues values = new ContentValues();

        values.put(StoryEntry.COL_ID, id);
        values.put(StoryEntry.COL_TITLE, title);
        values.put(StoryEntry.COL_LINK, link);
        values.put(StoryEntry.COL_PUBDATE, pubdate);
        values.put(StoryEntry.COL_SOURCE, source);

        if(author != null) {

            values.put(StoryEntry.COL_AUTHOR, author);
        }

        if(tagline != null) {

            values.put(StoryEntry.COL_TAGLINE, tagline);
        }

        if(attachment != null) {

            values.put(StoryEntry.COL_ATTACHMENT, attachment);
        }

        if(media != null) {

            values.put(StoryEntry.COL_MEDIA, media);
        }

        values.put(StoryEntry.COL_IS_READ, isRead ? 1 : 0);
        values.put(StoryEntry.COL_IS_SAVED, isSaved);

        return values;
    }


    /**
     * Log the Story attributes to console for testing/debug purposes.
     */
    public void print() {

        final String LOG_TAG = "DREADER";

        Log.v(LOG_TAG, "[STORY]");

        Log.v(LOG_TAG, "SQLite ID = " + _id);
        Log.v(LOG_TAG, "Firebase ID = " + id);

        Log.v(LOG_TAG, "Title = " + title);
        Log.v(LOG_TAG, "Link = " + link);
        Log.v(LOG_TAG, "Source = " + source);
        Log.v(LOG_TAG, "Pubdate = " + pubdate);

        Log.v(LOG_TAG, "Author = "
                + (author != null ? author : "None"));

        Log.v(LOG_TAG, "Tagline = "
                + (tagline != null ? tagline : "None"));

        Log.v(LOG_TAG, "Attachment = "
                + (attachment != null ? attachment : "None"));

        Log.v(LOG_TAG, "Media = "
                + (media != null ? media : "None"));

        Log.v(LOG_TAG, "Content = "
                + (content != null ? content.size() : 0)
                + " paragraphs");

        Log.v(LOG_TAG, "isRead? = " + isRead);
        Log.v(LOG_TAG, "isSaved? = " + isSaved());
        Log.v(LOG_TAG, "saved timestamp = " + isSaved);

        Log.v(LOG_TAG, "=====");
    }
}
