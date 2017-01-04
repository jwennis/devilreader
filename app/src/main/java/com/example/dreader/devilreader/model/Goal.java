package com.example.dreader.devilreader.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Goal implements Parcelable {

    public static final String PARAM_GOAL_PARCEL = "PARAM_GOAL_PARCEL";

    private long game_id;
    private long player_id;

    private String name;
    private long count;

    private String assists;

    private String time;
    private String video;


    public Goal() {

    }


    public Goal(Parcel in) {

        game_id = in.readLong();
        player_id = in.readLong();

        name = in.readString();
        count = in.readLong();

        assists = in.readString();
        time = in.readString();
        video = in.readString();
    }


    public String getAssists() {

        return assists;
    }


    public long getCount() {

        return count;
    }


    public long getGame_id() {

        return game_id;
    }


    public String getName() {

        return name;
    }


    public long getPlayer_id() {

        return player_id;
    }


    public String getTime() {

        return time;
    }


    public String getVideo() {

        return video;
    }


    public boolean hasVideo() {

        return video != null;
    }


    public static final Creator<Goal> CREATOR = new Creator<Goal> () {

        public Goal createFromParcel(Parcel in) {

            return new Goal(in);
        }

        public Goal[] newArray(int size) {

            return new Goal[size];
        }
    };


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong(game_id);
        out.writeLong(player_id);

        out.writeString(name);
        out.writeLong(count);

        out.writeString(assists);
        out.writeString(time);
        out.writeString(video);
    }
}
