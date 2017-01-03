package com.example.dreader.devilreader.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.dreader.devilreader.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Game implements Parcelable {

    public static final String PARAM_GAME_PARCEL = "PARAM_GAME_PARCEL";

    private long nhl_id;
    private long datestring;

    private String opponent;
    private String location;

    private String status;
    private String puckdrop;
    private String networks;

    private String article;
    private String tagline;

    private String recapImage;
    private String recapVideo;

    private List<String> goalsAway;
    private List<String> goalsHome;

    private List<String> statsAway;
    private List<String> statsHome;


    public Game() {

    }


    public Game(Parcel in) {

        nhl_id = in.readLong();
        datestring = in.readLong();

        opponent = in.readString();
        location = in.readString();

        status = in.readString();

        if(status.equals("P")) {

            puckdrop = in.readString();
            networks = in.readString();

        } else {

            article = in.readString();
            tagline = in.readString();

            recapImage = in.readString();
            recapVideo = in.readString();

            goalsAway = new ArrayList<>();
            goalsHome = new ArrayList<>();

            int numFrames = status.equals("F") ? 4 : 5;

            for(int i = 0; i < numFrames; i ++) {

                goalsAway.add(in.readString());
            }

            for(int i = 0; i < numFrames; i ++) {

                goalsHome.add(in.readString());
            }

            statsAway = new ArrayList<>();
            statsHome = new ArrayList<>();

            for(int i = 0; i < 7; i ++) {

                statsAway.add(in.readString());
            }

            for(int i = 0; i < 7; i ++) {

                statsHome.add(in.readString());
            }
        }
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


    public String getLongDate() {

        String date = Long.toString(datestring);
        int y = Integer.parseInt(date.substring(0, 4));
        int m = Integer.parseInt(date.substring(4, 6)) - 1;
        int d = Integer.parseInt(date.substring(6, 8));

        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d);

        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;

        return String.format("%s, %s %d, %d",
                Util.DAY_OF_WEEK_NAMES[day], Util.LONG_MONTH_NAMES[m], d, y);
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


    @Override
    public int describeContents() {

        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong(nhl_id);
        out.writeLong(datestring);

        out.writeString(opponent);
        out.writeString(location);

        out.writeString(status);

        if(isPending()) {

            out.writeString(puckdrop);
            out.writeString(networks);

            return;
        }

        out.writeString(article);
        out.writeString(tagline);

        out.writeString(recapImage);
        out.writeString(recapVideo);

        for(String goal : goalsAway) {

            out.writeString(goal);
        }

        for(String goal : goalsHome) {

            out.writeString(goal);
        }

        for(String stat : statsAway) {

            out.writeString(stat);
        }

        for(String stat : statsHome) {

            out.writeString(stat);
        }
    }


    public static final Creator<Game> CREATOR = new Creator<Game> () {

        public Game createFromParcel(Parcel in) {

            return new Game(in);
        }

        public Game[] newArray(int size) {

            return new Game[size];
        }
    };
}
