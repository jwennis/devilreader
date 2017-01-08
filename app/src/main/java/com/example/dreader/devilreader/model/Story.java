package com.example.dreader.devilreader.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.dreader.devilreader.Util;
import com.example.dreader.devilreader.data.StoryContract.StoryEntry;


public class Story implements Parcelable {

    public static final String PARAM_STORY_PARCEL = "PARAM_STORY_PARCEL";
    public static final String ATTR_PUBDATE = "pubdate";

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
    public Story(Cursor data) {

        mData = data;

        _id = getCursorLong(StoryEntry._ID);
        id = getCursorString(StoryEntry.COL_ID);
        title = getCursorString(StoryEntry.COL_TITLE);
        link = getCursorString(StoryEntry.COL_LINK);
        pubdate = getCursorLong(StoryEntry.COL_PUBDATE);
        source = getCursorString(StoryEntry.COL_SOURCE);

        isRead = getCursorLong(StoryEntry.COL_IS_READ) == 1;
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


    /**
     * Construct StoryModel from a Parcel for the purpose of passing data from
     * NewsActivity to StoryActivity, as well as restoring StoryActivity state
     *
     * @param in Parcel containing StoryModel data
     */
    public Story(Parcel in) {

        _id = in.readLong();
        id = in.readString();
        title = in.readString();
        link = in.readString();
        pubdate = in.readLong();
        source = in.readString();
        isRead = in.readLong() == 1;
        isSaved = in.readLong();

        String _author = in.readString();
        if (!_author.isEmpty()) { author = _author; }

        String _tagline = in.readString();
        if (!_tagline.isEmpty()) { tagline = _tagline; }

        String _attachment = in.readString();
        if (!_attachment.isEmpty()) { attachment = _attachment; }

        String _media = in.readString();
        if (!_media.isEmpty()) { media = _media; }

        String[] _content = in.createStringArray();
        if(_content.length > 0) { content = Arrays.asList(_content); }
    }


    private String getCursorString(String columnName) {

        int index = mData.getColumnIndex(columnName);

        return index > -1 ? mData.getString(index) : null;
    }


    private long getCursorLong(String columnName) {

        int index = mData.getColumnIndex(columnName);

        return index > -1 ? mData.getLong(index) : -1;
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


    public String getLongByline() {

        StringBuilder builder = new StringBuilder()
                .append(getSourceName())
                .append(" | ")
                .append(getDate());

        if(author != null) {

            builder.append("\nWritten by ");
            builder.append(author);
        }

        return builder.toString();
    }


    public String getShortByline() {

        return String.format("%1$s / %2$s", getSource(), getElapsed());
    }


    private String getDate() {

        String datestring = Long.toString(pubdate);

        String y = datestring.substring(0, 4);
        int m = Integer.parseInt(datestring.substring(4, 6));
        int d = Integer.parseInt(datestring.substring(6, 8));
        int h = Integer.parseInt(datestring.substring(8, 10));
        String min = datestring.substring(10, 12);
        String ampm = h < 12 ? "am" : "pm";

        h = h > 12 ? h - 12 : (h == 0 ? 12 : h);

        StringBuilder builder = new StringBuilder()
                .append(Util.SHORT_MONTH_NAMES[m - 1])
                .append(" " + d)
                .append(" " + h)
                .append(":" + min)
                .append(" " + ampm);

        return builder.toString();
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


    private String getSourceName() {

        switch(source) {

            case "FIB": { return "Fire and Ice"; }
            case "YT": { return "Youtube"; }
            case "PPF": { return "Pucks and Pitchforks"; }
            case "HAD": { return "Have Another Donut"; }
            default: { return source; }
        }
    }


    public boolean hasAttachment() {

        return attachment != null && !attachment.isEmpty();
    }


    public boolean hasMedia() {

        return media != null && !media.isEmpty();
    }


    public boolean hasContent() {

        return content != null && content.size() > 0;
    }


    // Mutators


    public void setContent(List<String> paragraphs) {

        content = paragraphs;
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


    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong(_id);
        out.writeString(id);
        out.writeString(title);
        out.writeString(link);
        out.writeLong(pubdate);
        out.writeString(source);
        out.writeLong(isRead ? 1 : 0);
        out.writeLong(isSaved);

        out.writeString(author != null ? author : "");
        out.writeString(tagline != null ? tagline : "");
        out.writeString(attachment != null ? attachment : "");
        out.writeString(media != null ? media : "");

        out.writeStringArray(content != null ? content.toArray(new String[content.size()]) : new String[0]);
    }


    @Override
    public int describeContents() {

        return 0;
    }


    public static final Creator<Story> CREATOR = new Creator<Story> () {

        public Story createFromParcel(Parcel in) {

            return new Story(in);
        }

        public Story[] newArray(int size) {

            return new Story[size];
        }
    };
}
