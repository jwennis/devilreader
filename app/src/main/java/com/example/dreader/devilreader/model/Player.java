package com.example.dreader.devilreader.model;


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

    public boolean is_assistant() {

        return is_assistant;
    }

    public boolean is_captain() {

        return is_captain;
    }

    public boolean is_injured() {

        return is_injured;
    }

    public boolean is_roster() {

        return is_roster;
    }

    public boolean is_drafted() {

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
}
