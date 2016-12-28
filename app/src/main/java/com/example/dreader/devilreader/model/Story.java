package com.example.dreader.devilreader.model;

import android.util.Log;

import java.util.List;

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
    private boolean isSaved;


    public Story() {

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

        return isSaved;
    }


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
        Log.v(LOG_TAG, "isSaved? = " + isSaved);

        Log.v(LOG_TAG, "=====");
    }
}
