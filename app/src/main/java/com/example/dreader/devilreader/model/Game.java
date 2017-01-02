package com.example.dreader.devilreader.model;

import android.util.Log;

import java.util.List;

public class Game {

    private String article;
    private long datestring;

    private List<String> goalsAway;
    private List<String> goalsHome;

    private String location; // "H" or "A"
    private long nhl_id;
    private String opponent;
    private String recapImage;
    private String recapVideo;

    private List<String> statsAway;
    private List<String> statsHome;

    private String status;
    private String tagline;


    public Game() {

    }


    public String getArticle() {

        return article;
    }


    public long getDatestring() {

        return datestring;
    }


    public String getLocation() {

        return location;
    }


    public long getNhl_id() {

        return nhl_id;
    }


    public String getOpponent() {

        return opponent;
    }


    public String getRecapImage() {

        return recapImage;
    }

    public String getRecapVideo() {

        return recapVideo;
    }

    public String getStatus() {

        return status;
    }

    public String getTagline() {

        return tagline;
    }

    public List<String> getGoalsAway() {

        return goalsAway;
    }


    public List<String> getGoalsHome() {

        return goalsHome;
    }


    public void print() {

        Log.v("DREADER", "Date: " + datestring);

        if(goalsHome != null) {

            Log.v("DREADER", "Goals (home): " + goalsHome.toString());

        } else {

            Log.v("DREADER", "NOGOALS");
        }

    }
}
