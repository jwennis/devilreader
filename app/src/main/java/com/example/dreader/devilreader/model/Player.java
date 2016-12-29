package com.example.dreader.devilreader.model;


import android.util.Log;

public class Player {

    private long nhl_id;

    private String name;

    private String team;
    private String position;
    private String hand;
    private long number;

    private boolean is_assistant;
    private boolean is_captain;
    private boolean is_injured;
    private boolean is_roster;

    private boolean is_drafted;
    private String draft_team;
    private long draft_year;
    private long draft_round;
    private long draft_position;

    private long dob;
    private long height;
    private long weight;


    public Player() {

    }


    public long getNhl_id() {

        return nhl_id;
    }


    public String getName() {

        return name;
    }


    public String getTeam() {

        return team;
    }


    public String getPosition() {

        return position;
    }


    public String getHand() {

        return hand;
    }


    public long getNumber() {

        return number;
    }


    public boolean getIs_assistant() {

        return is_assistant;
    }


    public boolean getIs_captain() {

        return is_captain;
    }


    public boolean getIs_injured() {

        return is_injured;
    }


    public boolean getIs_roster() {

        return is_roster;
    }


    public boolean getIs_drafted() {

        return is_drafted;
    }


    public String getDraft_team() {

        return draft_team;
    }


    public long getDraft_year() {

        return draft_year;
    }


    public long getDraft_round() {

        return draft_round;
    }


    public long getDraft_position() {

        return draft_position;
    }


    public long getDob() {

        return dob;
    }


    public long getHeight() {

        return height;
    }


    public long getWeight() {

        return weight;
    }


    public void print() {

        Log.v("DREADER", "ID = " + nhl_id);

        Log.v("DREADER", "Name = " + name);

        Log.v("DREADER", "Team = " + team);
        Log.v("DREADER", "Position = " + position);
        Log.v("DREADER", "Hand = " + hand);
        Log.v("DREADER", "Number = " + number);

        Log.v("DREADER", "isAssistant? = " + is_assistant);
        Log.v("DREADER", "isCaptain? = " + is_captain);
        Log.v("DREADER", "isInjured? = " + is_injured);
        Log.v("DREADER", "isRoster? = " + is_roster);
        Log.v("DREADER", "isDrafted? = " + is_drafted);

        Log.v("DREADER", "Draft team = " + (draft_team != null ? draft_team : "N/A"));
        Log.v("DREADER", "Draft team = " + (draft_year > 0 ? draft_year : "N/A"));
        Log.v("DREADER", "Draft team = " + (draft_round > 0 ? draft_round : "N/A"));
        Log.v("DREADER", "Draft team = " + (draft_position > 0 ? draft_position : "N/A"));

        Log.v("DREADER", "DOB = " + dob);
        Log.v("DREADER", "HEIGHT = " + height);
        Log.v("DREADER", "WEIGHT = " + weight);
    }
}
