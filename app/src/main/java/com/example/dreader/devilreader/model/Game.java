package com.example.dreader.devilreader.model;

import android.util.Log;

import com.example.dreader.devilreader.Util;

import java.util.Calendar;
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

    private String puckdrop;
    private String networks;

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


    public String getNetworks() {

        return networks;
    }


    public String getPuckdrop() {

        return puckdrop;
    }


    public List<String> getStatsHome() {

        return statsHome;
    }


    public List<String> getStatsAway() {

        return statsAway;
    }


    public void print() {

        Log.v("DREADER", "Date: " + datestring);


    }

    public String getDate() {

        String date = Long.toString(datestring);
        int y = Integer.parseInt(date.substring(0, 4));
        int m = Integer.parseInt(date.substring(4, 6)) - 1;
        int d = Integer.parseInt(date.substring(6, 8));

        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d);

        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;

        return String.format("%s, %s %d",
                Util.DAY_OF_WEEK_NAMES[day], Util.SHORT_MONTH_NAMES[m], d);
    }

    public boolean isPending() {

        return status.equals("P");
    }

    public boolean isRegulation() {

        return status.equals("F");
    }

    public boolean isHome() {

        return location.equals("H");
    }

    public int getFinalScoreHome() {

        String total = goalsHome.get(goalsHome.size() - 1);

        return Integer.parseInt(total);
    }

    public int getFinalScoreAway() {

        String total = goalsAway.get(goalsAway.size() - 1);

        return Integer.parseInt(total);
    }
}
