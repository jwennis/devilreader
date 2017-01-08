package com.example.dreader.devilreader.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.dreader.devilreader.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Player implements Parcelable {

    public static final String PARAM_PLAYER_PARCEL = "PARAM_PLAYER_PARCEL";

    private static final String PLAYER_STATUS_CAPTAIN = "C";
    private static final String PLAYER_STATUS_ASSISTANT = "A";
    private static final String CURRENT_SEASON = "2016-17";

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

    private List<PlayerContract> mContracts;


    public Player() {

    }


    public Player(Parcel in) {

        nhl_id = in.readLong();

        name = in.readString();
        team = in.readString();
        position = in.readString();
        hand= in.readString();
        number= in.readLong();

        is_assistant = in.readInt() == 1;
        is_captain= in.readInt() == 1;
        is_injured = in.readInt() == 1;
        is_roster = in.readInt() == 1;
        is_drafted = in.readInt() == 1;

        if(is_drafted) {

            draft_team = in.readString();
            draft_year = in.readLong();
            draft_round = in.readLong();
            draft_position = in.readLong();
        }

        dob = in.readLong();
        height= in.readLong();
        weight= in.readLong();

        int numContracts = in.readInt();

        if(numContracts > 0) {

            mContracts = new ArrayList<>();

            for(int i = 0; i < numContracts; i++) {

                String type = in.readString();
                String expiry = in.readString();

                PlayerContract contract = new PlayerContract(type, expiry);

                int numYears = in.readInt();

                for(int j = 0; j < numYears; j++) {

                    String season = in.readString();
                    long nSalary = in.readLong();
                    long aSalary = in.readLong();
                    long sBonus = in.readLong();
                    long pBonus = in.readLong();
                    boolean mClause = in.readInt() == 1;
                    boolean tClause = in.readInt() == 1;

                    contract.addYear(season, nSalary, aSalary, sBonus, pBonus, mClause, tClause);
                }

                mContracts.add(contract);
            }
        }
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


    public String getStatus() {

        if(is_captain) {

            return PLAYER_STATUS_CAPTAIN;

        } else if (is_assistant) {

            return PLAYER_STATUS_ASSISTANT;
        }

        return null;
    }


    public String getDobString() {

        String dob_string = Long.toString(dob);

        String y = dob_string.substring(0, 4);
        int m = Integer.parseInt(dob_string.substring(4, 6));
        int d = Integer.parseInt(dob_string.substring(6, 8));

        return Util.SHORT_MONTH_NAMES[ m-1 ] + " " + d + ", " + y;
    }


    public int getAge() {

        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDate = c.get(Calendar.DAY_OF_MONTH);

        String dob_string = Long.toString(dob);

        int birthYear = Integer.parseInt(dob_string.substring(0, 4));
        int birthMonth = Integer.parseInt(dob_string.substring(4, 6));
        int birthDate = Integer.parseInt(dob_string.substring(6, 8));

        int age = currentYear - birthYear;

        if(currentMonth > birthMonth ||
                (currentMonth == birthMonth && currentDate <= birthDate)) {

            age--;
        }

        return age;
    }


    public int getCapHit() {

        for(PlayerContract contract : mContracts) {

            for(PlayerContract.ContractYear year : contract.getYears()) {

                if(year.getSeason().equals(CURRENT_SEASON)) {

                    return contract.getCapHit();
                }
            }
        }

        return 0;
    }


    public List<String> getCapLabelValues() {

        List<String> values = new ArrayList<>();
        String expiryStatus = "";

        for(PlayerContract contract : mContracts) {

            for(PlayerContract.ContractYear year : contract.getYears()) {

                int contractSeason = Integer.parseInt(year.getSeason().substring(0, 4));

                if(contractSeason >= 2016) {

                    values.add(Util.format$(contract.getCapHit()));
                    expiryStatus = contract.getExpiry();
                }
            }
        }

        values.add(expiryStatus);

        return values;
    }


    public List<PlayerContract> getContracts() {

        return mContracts;
    }


    public void setContracts(List<PlayerContract> contracts) {

        mContracts = contracts;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong(nhl_id);

        out.writeString(name);
        out.writeString(team);
        out.writeString(position);
        out.writeString(hand);
        out.writeLong(number);

        out.writeInt(is_assistant ? 1 : 0);
        out.writeInt(is_captain ? 1 : 0);
        out.writeInt(is_injured ? 1 : 0);
        out.writeInt(is_roster ? 1 : 0);
        out.writeInt(is_drafted ? 1 : 0);

        if(is_drafted) {

            out.writeString(draft_team);
            out.writeLong(draft_year);
            out.writeLong(draft_round);
            out.writeLong(draft_position);
        }

        out.writeLong(dob);
        out.writeLong(height);
        out.writeLong(weight);

        if(mContracts != null && mContracts.size() > 0) {

            out.writeInt(mContracts.size());

            for(PlayerContract contract : mContracts) {

                out.writeString(contract.getType());
                out.writeString(contract.getExpiry());

                out.writeInt(contract.getYears().size());

                for(PlayerContract.ContractYear year : contract.getYears()) {

                    out.writeString(year.getSeason());
                    out.writeLong(year.getNhlSalary());
                    out.writeLong(year.getAhlSalary());
                    out.writeLong(year.getSigningBonus());
                    out.writeLong(year.getPerformanceBonus());
                    out.writeInt(year.isNoMove() ? 1 : 0);
                    out.writeInt(year.isNoTrade() ? 1 : 0);
                }
            }
        }
    }


    @Override
    public int describeContents() {

        return 0;
    }


    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {

        public Player createFromParcel(Parcel in) {

            return new Player(in);
        }

        public Player[] newArray(int size) {

            return new Player[size];
        }
    };
}
